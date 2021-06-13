package com.example.cafe.models;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class News implements Serializable {
    public  String news_id;
    public String news_title;
    public String news_date;
    public String news_img_url;
    public String news_desc;

    public News() {
    }

    public static News cloneNews(News news){
        News other = new News();
        other.news_id = Objects.requireNonNullElse(news.news_id,"");
        other.news_title = Objects.requireNonNullElse(news.news_title, "");
        other.news_date = Objects.requireNonNullElse(news.news_date,"");
        other.news_desc = Objects.requireNonNullElse(news.news_desc, "");
        other.news_img_url = Objects.requireNonNullElse(news.news_img_url,"");
        return other;
    }
    public News(String news_title, String news_date, String news_img_url, String news_desc, String news_id) {
        this.news_id = Objects.requireNonNullElse(news_id, "");
        this.news_title = Objects.requireNonNullElse(news_title,"");
        this.news_date = Objects.requireNonNull(news_date, "");
        this.news_img_url = Objects.requireNonNullElse(news_img_url,"");
        this.news_desc = Objects.requireNonNull(news_desc, "");
    }

    @NonNull
    @NotNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
