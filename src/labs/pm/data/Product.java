/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.HALF_UP;

/**
 * {@code Product} class represents behaviours and properties of the Product object
 * <br>
 *     each object has an id, name, price
 * <br>
 * each price has a discount rate of {@link DISCOUNT_RATE discount rate}
 * @author oracle
 * @version 1.0.0
 */
public abstract class Product {
    private int id;
    private String name;
    private BigDecimal price;
    private Rating rating;
    /**
     * this is a discount rate of type {@link BigDecimal}
     */
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    };

    Product(int id, String name, BigDecimal price) {
        this(id, name, price, Rating.NOT_RATED);
    }

    //Product() { //constructor for default values, if you want any
      //  this(0, "no name", BigDecimal.ZERO);
    //}

    public abstract Product applyRating(Rating newRating);//this method doesn't have a generic fallback behaviour

    public LocalDate getBestBefore() { //this method has a generic fallback behaviour
        return LocalDate.now();
    }

    /**
     * calculates discount based on price discount
     * @return {@link BigDecimal}
     */
    public BigDecimal getDiscount(){
        return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Rating getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return id+" "+name+" "+price+" "+this.getDiscount()+" "+this.getRating().getStars()+" "+this.getBestBefore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        //if (o == null || getClass() != o.getClass()) return false;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return id == product.id &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
