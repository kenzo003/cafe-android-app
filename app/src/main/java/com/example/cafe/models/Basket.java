package com.example.cafe.models;

import java.io.Serializable;

public class Basket implements Serializable {
    public String basket_id;
    public String basket_user;
    public String basket_product;
    public String basket_product_count;

    public Basket(String basket_id, String basket_user, String basket_product, String basket_product_count) {
        this.basket_id = basket_id;
        this.basket_user = basket_user;
        this.basket_product = basket_product;
        this.basket_product_count = basket_product_count;
    }

    public Basket() {
    }
}
