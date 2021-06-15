package com.example.cafe.models;

import java.io.Serializable;

public class BasketProduct implements Serializable {
    Basket basket;
    Product product;

    public BasketProduct(Basket basket, Product product) {
        this.basket = basket;
        this.product = product;
    }

    public String getPriceBasket() {
        int price = Integer.parseInt(basket.basket_product_count) * Integer.parseInt(product.product_price);
        return String.valueOf(price);
    }


    public String getCountProduct() {
        return basket.basket_product_count;
    }

    public Product getProduct() {
        return product;
    }

    public Basket getBasket() {
        return basket;
    }
}
