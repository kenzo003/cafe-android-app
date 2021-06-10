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

    //User
    public static final String USER_ID = "user_id";


    //News
    public static final String NODE_NEWS = "news";
    public static final String NEWS_ID = "news_id";
    public static final String NEWS_HEADER = "header_text";
    public static final String NEWS_DATA = "data";
    public static final String NEWS_MEDIA = "media_id";
    public static final String NEWS_DESC = "description";
    public static final String NODE_NEWS_USERS = "NewsUser";
    public static final String STORAGE_NODE_NEWS = "news/news_photo/";

    //Product
    public static final String NODE_PRODUCT = "product";
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String PRODUCT_UNIT = "product_unit";
    public static final String PRODUCT_DESC = "product_desc";
    public static final String PRODUCT_PRICE = "product_price";
    public static final String PRODUCT_CATEGORY_ID = "ProductCategory";
    public static final String PRODUCT_VISIBILITY = "product_visibility";
    public static final String PRODUCT_QUANTITY = "quantity";

    //Category
    public static final String NODE_CATEGORY = "product";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";





    public static void initFireBase() {
        AUTH = FirebaseAuth.getInstance();
        REF_DATABASE_ROOT = FirebaseDatabase.getInstance().getReference();
    }
}
