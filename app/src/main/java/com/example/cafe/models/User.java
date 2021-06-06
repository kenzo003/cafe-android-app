package com.example.cafe.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String surname;
    //    private String password;
    public String email_address;
    public String phone_number;
    //    private LocalDate birth_date;
    public String city;
    public String gender;
//    private int address;

    public User(String name, String surname, String email_address, String phone_number, String city, String gender) {
        this.name = name;
        this.surname = surname;
        this.email_address = email_address;
        this.phone_number = phone_number;
//        this.birth_date = birth_date;
        this.city = city;
        this.gender = gender;
//        this.address = address;
    }
}
