package com.example.cafe.models;

public class Favorite {
    public String favorite_id;
    public String favorite_product;
    public String favorite_user;

    public Favorite() {
    }

    public Favorite(String favorite_id, String favorite_product, String favorite_user) {
        this.favorite_id = favorite_id;
        this.favorite_product = favorite_product;
        this.favorite_user = favorite_user;
    }
}
