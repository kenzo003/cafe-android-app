package com.example.cafe.models;

import java.io.Serializable;

public class Product implements Serializable {
    public String product_id;
    public String product_name;
    public String product_unit; //Единицы измерения
    public String product_description;
    public String product_old_price;
    public String product_price;
    public String product_quantity; //Количество
    public String product_visibility; //
    public String product_category;
    public String product_logo;



    public Product() {
    }

    public Product(String product_id, String product_name, String product_unit, String product_description, String product_old_price, String product_price, String product_quantity, String product_visibility, String product_category, String product_logo) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_unit = product_unit;
        this.product_description = product_description;
        this.product_old_price = product_old_price;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_visibility = product_visibility;
        this.product_category = product_category;
        this.product_logo = product_logo;
    }
}
