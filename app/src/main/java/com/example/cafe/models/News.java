package com.example.cafe.models;

public class News {
    public String header_text;
    public String date;
    public String media_id;
    public String description;

    public News(String header_text,String date, String media_id, String description) {
        this.header_text = header_text;
        this.date = date;
        this.media_id = media_id;
        this.description = description;
    }
}
