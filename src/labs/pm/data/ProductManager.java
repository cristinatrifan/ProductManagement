/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

    Map<Product, List<Review>> products = new HashMap<>();
    private static Map<String, ResourceFormatter> formatters = new HashMap<>(Map.of(
            "en-GB", new ResourceFormatter(Locale.UK),
            "en-US", new ResourceFormatter(Locale.US),
            "fr-FR", new ResourceFormatter(Locale.FRANCE),
            "ru-RU", new ResourceFormatter(new Locale("ru-RU")),
            "zh-CN", new ResourceFormatter(Locale.CHINA),
            "ro-RO", new ResourceFormatter(new Locale("ro-RO"))
    ));
    private ResourceFormatter formatter;

    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());

    private ResourceBundle config = ResourceBundle.getBundle("labs.pm.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String languageTag) {
        changeLocale(languageTag);
    }

    public void changeLocale(String languageTag) {
        formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
        Product product = new Food(id, name, price, rating, bestBefore);
        products.putIfAbsent(product, new ArrayList<>());
        return product;

    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Drink(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage());
        }

        return null;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) { //returns updated version of the product with new rating
        /*if (reviews[reviews.length - 1] != null) {
            reviews = Arrays.copyOf(reviews, reviews.length + 5);
        }*/

        List<Review> reviews = products.get(product);
        products.remove(product, reviews);
        reviews.add(new Review(rating, comments));

        product = product.applyRating(Rateable.convert((int) Math.round(reviews.stream().
                        mapToInt(p -> p.getRating().ordinal()).
                        average().orElse(0))));

        products.put(product, reviews);
        return product;
    }

    public Product findProduct(int id) throws ProductManagerException {
    Product result = null;

    return result = products.keySet().stream().filter(p -> p.getId() == id).findFirst()
            .orElseThrow(() -> new ProductManagerException("Product with id "+ id +" not found"));
    }

    public void printProductReport(int id) throws ProductManagerException {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public void printProductReport(Product product) {
        List<Review> reviews = products.get(product);
        StringBuilder txt = new StringBuilder();
        txt.append(formatter.formatProduct(product));
        txt.append('\n');

        Collections.sort(reviews); //will use compareTo method implemented in Review class

        if (reviews.isEmpty()) {
            txt.append(formatter.getText("no.reviews"));
            txt.append('\n');
        } else {
            txt.append(reviews.stream().map(p -> formatter.formatReview(p) + '\n').collect(Collectors.joining()));
        }

        System.out.println(txt);
    }

    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        /*List<Product> productList = new ArrayList<>(products.keySet());
        productList.sort(sorter);*/

        StringBuilder txt = new StringBuilder();
        /*for (Product product : productList) {
            txt.append(formatter.formatProduct(product));
            txt.append('\n');
        }*/

        //could also use forEach instead of map
        txt.append(products.keySet().stream()
                .sorted(sorter)
                .filter(filter)
                .map(p -> formatter.formatProduct(p) + '\n').collect(Collectors.joining()));

        System.out.print(txt);
    }

    public void parseReview(String text) throws ParseException {
        try {
            Object[] values = reviewFormat.parse(text);//comma-delimited parsing
            reviewProduct(Integer.parseInt((String) values[0]),
                    Rateable.convert(Integer.parseInt((String) values[1])),
                    (String) values[2]);
        } catch (ParseException | NumberFormatException ex) {
            logger.log(Level.WARNING, ex.getMessage() + "error parsing review");
        }
    }

    public void parseProduct(String text) throws ParseException {
        try {
            Object[] values = productFormat.parse(text);//comma-delimited parsing

            int id = Integer.parseInt((String) values[1]);
            String name = (String) values[2];
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
            Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
            //create either Drink or Food
            switch((String) values[0]) {
                case "D": this.createProduct(id, name, price, rating);
                    break;
                case "F":
                    LocalDate bestBefore = LocalDate.parse((String) values[5]);
                    this.createProduct(id, name, price, rating, bestBefore);
            }

        } catch (ParseException | NumberFormatException | DateTimeParseException ex) {
            logger.log(Level.WARNING, ex.getMessage() + " error parsing review");
        }
    }

    //get sum of discounts for each rating
    //stream can be parallelized
    public Map<String, String> getDiscounts() {
    return products.keySet()
        .stream()
        .collect(Collectors.groupingBy(
                product -> product.getRating().getStars(),
                Collectors.collectingAndThen(
                        Collectors.summingDouble(
                                product -> product.getDiscount().doubleValue()),
                        discount -> formatter.moneyFormat.format(discount)
                        )
                )
        );
    }

    //class meant for locale specific formatting
    public static class ResourceFormatter {
        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        public ResourceFormatter(Locale locale) {
            this.locale = locale;
            resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    dateFormat.format(product.getBestBefore())
            );
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"),
                    review.getRating().getStars(),
                    review.getComments());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
