/*
 * Copyright (c) 2021. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package labs.pm.data;

import java.math.BigDecimal;
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
public class Product {
    private int id;
    private String name;
    private BigDecimal price;
    /**
     * this is a discount rate of type {@link BigDecimal}
     */
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

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

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

}
