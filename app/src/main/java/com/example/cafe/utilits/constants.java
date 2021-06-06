package com.example.cafe.utilits;

import com.example.cafe.activities.MainActivity;
import com.example.cafe.database.DataBaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class constants {
    public static final String TAG = "cafe_test";
    public static MainActivity APP_ACTIVITY;
    public static DataBaseRepository REPOSITORY;
    public static String TYPE_DATABASE = "type_database";
    public static final String TYPE_FIREBASE = "type_firebase";
    public static DatabaseReference REF_DATABASE_ROOT;
    public static FirebaseAuth AUTH;
    public static String UID;

    public static String EMAIL;
    public static String PASSWORD;

    public static final String NODE_USERS = "users";
    public static String PHONE_NUMBER = "+16505551234";

    public static void initFireBase() {
        AUTH = FirebaseAuth.getInstance();
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
//        if (AUTH.getCurrentUser().getUid().isEmpty() && AUTH.getCurrentUser().getUid() != null){
//            UID = AUTH.getCurrentUser().getUid().toString();
//        }
    }
}
