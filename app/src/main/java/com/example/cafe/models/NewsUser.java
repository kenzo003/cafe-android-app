package com.example.cafe.models;

public class NewsUser {
    public String news_id;
    public String user_id;
    public String visible;

    public NewsUser(String news_id, String user_id, String visible) {
        this.news_id = news_id;
        this.user_id = user_id;
        this.visible = visible;
    }

    public NewsUser() {
    }
}
