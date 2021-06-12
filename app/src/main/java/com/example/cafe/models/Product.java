package com.example.cafe.models;

public class Product {
    public String product_id;
    public String product_name;
    public String product_unit; //Единицы измерения
    public String product_description;
    public String product_price;
    public String product_quantity; //Количество
    public String product_visibility; //
    public String category_id;


    public Product(String product_id, String product_name, String product_unit, String product_description, String product_price, String product_quantity, String product_visibility, String category_id) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_unit = product_unit;
        this.product_description = product_description;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_visibility = product_visibility;
        this.category_id = category_id;
    }

    public Product() {
    }
}
