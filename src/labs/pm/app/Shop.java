/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.app;

import labs.pm.data.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

/**
 *
 * {@code Shop} class represents an application that manages products
 * @author oracle
 * @version 1.0.0
 */
public class Shop {

    public static void main(String[] args) {
        ProductManager manager = new ProductManager(Locale.UK);

	Product p1 = manager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
	manager.printProductReport(101);
	manager.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cuppa");
	manager.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cuppa");
	manager.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cuppa");
	manager.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cuppa tea");
	manager.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cuppa");
	manager.reviewProduct(p1, Rating.FOUR_STAR, "Nice hot cuppa");
	manager.printProductReport(p1);

	Product p2 = manager.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.FOUR_STAR);
	Product p3 = manager.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
	Product p4 = manager.createProduct(105, "Cookie", BigDecimal.valueOf(3.99), Rating.TWO_STAR, LocalDate.now().plusDays(2));
	Product p5 = p3.applyRating(Rating.THREE_STAR);
	Product p6 = manager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR);
	Product p7 = manager.createProduct(104, "Chocolate", BigDecimal.valueOf(2.99), Rating.FIVE_STAR, LocalDate.now().plusDays(2));
    System.out.println(p6.equals(p7));

	//System.out.println(p1.getId()+" "+p1.getName()+" "+p1.getPrice()+" "+p1.getDiscount()+" "+p1.getRating().getStars());//the run button compiles the code, it runs javac and then java

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);
        System.out.println(p6);
        System.out.println(p7);
    }
}
