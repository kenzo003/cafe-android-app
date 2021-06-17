package com.example.cafe.utilits;

import android.util.Patterns;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

public class Validate {

    private static final String emptyField = "Поле не должно быть пустым";

    public static boolean emailValid(View view, String email) {
        TextInputEditText item = (TextInputEditText) view;
        if (email.isEmpty()) {
            item.setError(emptyField);
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            item.setError("Email введен не корректно");
            return false;
        } else {
            item.setError(null);
            return true;
        }
    }


    public static boolean passwordValid(View view, String password) {
        TextInputEditText item = (TextInputEditText) view;
        if (password.isEmpty()) {
            item.setError(emptyField);
            return false;
        } else if (password.length() < 6) {
            item.setError("Пароль должен содержать минимуv 6 символов");
            return false;
        } else {
            item.setError(null);
            return true;
        }
    }

    public static boolean phoneValid(View view, String phone) {
        TextInputEditText item = (TextInputEditText) view;
        if (phone.isEmpty()) {
            item.setError(emptyField);
            return false;
        } else if (phone.length() < 10) {
            item.setError("Номер должен состоять минимум из 10 цифр");
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            item.setError("Номер введен не корректно");
            return false;
        } else {
            item.setError(null);
            return true;
        }
    }

    public static boolean nameValid(View view, String name) {
        TextInputEditText item = (TextInputEditText) view;
        if (name.isEmpty()) {
            item.setError(emptyField);
            return false;
        } else if (!name.matches("[а-яА-яa-zA-z\\s]*")) {
            item.setError("Поле заполнено некорректно");
            return false;
        } else {
            item.setError(null);
            return true;
        }
    }

    //TODO Поправить
    public static boolean dateValid(View view, String date) {
        TextInputEditText item = (TextInputEditText) view;
        if (date.isEmpty()) {
            item.setError(emptyField);
            return false;
        } else if (!date.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$")) {
            item.setError("Поле заполнено некорректно");
            return false;
        } else {
            item.setError(null);
            return true;
        }
    }

}