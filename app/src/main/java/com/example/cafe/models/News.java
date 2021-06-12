package com.example.cafe.models;

import java.util.Objects;

public class News {
    public  String news_id;
    public String news_title;
    public String news_date;
    public String news_img_url;
    public String news_desc;

    public News() {
    }

    public News(String news_title, String news_date, String news_img_url, String news_desc, String news_id) {
        this.news_id = Objects.requireNonNullElse(news_id, "");
        this.news_title = Objects.requireNonNullElse(news_title,"");
        this.news_date = Objects.requireNonNull(news_date, "");
        this.news_img_url = Objects.requireNonNullElse(news_img_url,"");
        this.news_desc = Objects.requireNonNull(news_desc, "");
    }
}
