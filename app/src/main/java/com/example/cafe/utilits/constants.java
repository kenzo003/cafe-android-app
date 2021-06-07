package com.example.cafe.utilits;

import com.example.cafe.activities.MainActivity;
import com.example.cafe.database.DataBaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class constants {
    public static final String ID_CUSTOMER = "zYIxy03gxtajuqUKtnmUidQOJ0A2";
    public static final String TAG = "cafe_test";
    public static MainActivity APP_ACTIVITY;
    public static DataBaseRepository REPOSITORY;
    public static String TYPE_DATABASE = "type_database";
    public static final String TYPE_FIREBASE = "type_firebase";
    public static DatabaseReference REF_DATABASE_ROOT;
    public static FirebaseAuth AUTH;
    public static String UID;

    public static final String VER_NEW_USER = "VER_NEW_USER";
    public static final String VER_CUR_USER = "VER_CUR_USER";
    public static final String VER_CUR_USER_NEW_PHONE = "VER_CUR_USER_NEW_PHONE";

    public static String EMAIL;
    public static String PASSWORD;

    public static final String NODE_USERS = "users";
    public static String PHONE_NUMBER = "+16505550000";

    public static void initFireBase() {
        AUTH = FirebaseAuth.getInstance();
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
    }
}
