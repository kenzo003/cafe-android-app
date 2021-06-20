package com.example.cafe.database.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Basket;
import com.example.cafe.models.BasketProduct;
import com.example.cafe.models.Category;
import com.example.cafe.models.CustomerUser;
import com.example.cafe.models.Favorite;
import com.example.cafe.models.News;
import com.example.cafe.models.NewsUser;
import com.example.cafe.models.Product;
import com.example.cafe.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.cafe.utilits.constants.BASKET_ID;
import static com.example.cafe.utilits.constants.BASKET_PRODUCT;
import static com.example.cafe.utilits.constants.BASKET_PRODUCT_COUNT;
import static com.example.cafe.utilits.constants.BASKET_USER;
import static com.example.cafe.utilits.constants.FAVORITE_ID;
import static com.example.cafe.utilits.constants.FAVORITE_PRODUCT;
import static com.example.cafe.utilits.constants.FAVORITE_USER;
import static com.example.cafe.utilits.constants.NODE_BASKET;
import static com.example.cafe.utilits.constants.NODE_FAVORITE;
import static com.example.cafe.utilits.constants.NODE_NEWS_USERS;
import static com.example.cafe.utilits.constants.NODE_PRODUCT;
import static com.example.cafe.utilits.constants.PRODUCT_CATEGORY;
import static com.example.cafe.utilits.constants.PRODUCT_DESC;
import static com.example.cafe.utilits.constants.PRODUCT_ID;
import static com.example.cafe.utilits.constants.PRODUCT_LOGO;
import static com.example.cafe.utilits.constants.PRODUCT_NAME;
import static com.example.cafe.utilits.constants.PRODUCT_OLD_PRICE;
import static com.example.cafe.utilits.constants.PRODUCT_PRICE;
import static com.example.cafe.utilits.constants.PRODUCT_QUANTITY;
import static com.example.cafe.utilits.constants.PRODUCT_UNIT;
import static com.example.cafe.utilits.constants.PRODUCT_VISIBILITY;
import static com.example.cafe.utilits.constants.TAG;
import static com.example.cafe.utilits.constants.USER_ID;
import static com.example.cafe.utilits.constants.VER_CUR_USER;
import static com.example.cafe.utilits.constants.VER_CUR_USER_NEW_PHONE;
import static com.example.cafe.utilits.constants.VER_NEW_USER;

public class AppRepository {
    private final FirebaseAuth mAuth; //Хранилище пользователей
    private final DatabaseReference mReference; //Корневой узел БД
    private final StorageReference storageRef;  //Корень хранилища данных
    private final String UID; //Текущий пользователь

    private final MutableLiveData<List<News>> allNews;
    private final MutableLiveData<FirebaseUser> userMutableLiveData; //LiveData Users ()
    private final MutableLiveData<String> msg;
    private final MutableLiveData<List<Category>> allCategory;
    private final MutableLiveData<List<Product>> allProduct;
    private final MutableLiveData<List<Product>> allProductFavorite;
    private final MutableLiveData<List<BasketProduct>> allProductBasket;

    public MutableLiveData<List<BasketProduct>> getAllProductBasket() {
        return allProductBasket;
    }

    public MutableLiveData<List<Product>> getAllProductFavorite() {
        return allProductFavorite;
    }

    public AppRepository() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        UID = (mAuth.getCurrentUser() != null) ? mAuth.getCurrentUser().getUid() : "";

        allNews = new NewsLiveData();

        userMutableLiveData = new MutableLiveData<>();
        msg = new MutableLiveData<>();
        allCategory = new CategoryLiveData();
        allProduct = new AllProductLiveData();
        allProductBasket = new BasketLiveData();
        allProductFavorite = new FavoriteLiveData();
    }

    public MutableLiveData<List<News>> getAllNews() {
        return allNews;
    }

    public MutableLiveData<List<Category>> getAllCategory() {
        return allCategory;
    }

    public MutableLiveData<List<Product>> getAllProduct() {
        return allProduct;
    }

    public void sendPasswordResetEmail(String email, OnSuccessListener<? super Void> onSuccess) {
        mAuth.sendPasswordResetEmail(email).addOnSuccessListener(onSuccess).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, e.getMessage());
                        msg.postValue(e.getMessage());
                    }
                }
        );
    }

//    public void signupUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id, OnCompleteListener<AuthResult> onComplete) {
//        mAuth.createUserWithEmailAndPassword(email_address, password)
//                .addOnCompleteListener(onComplete)
//                .addOnSuccessListener(
//                        authResult -> insertUser(name, surname, email_address, phone_number, city, gender, birth_date, address, password, customer_id)
//                )
//                .addOnFailureListener(
//                        e -> {
//                            Log.d(TAG, e.getMessage());
//                            msg.postValue(e.getMessage());
//                        }
//                );
//    }

    public void loginUser(String email_address, String password, OnCompleteListener<AuthResult> onComplete, OnFailureListener onFailed) {
        mAuth.signInWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(onComplete)
                .addOnFailureListener(onFailed);
    }

    public void updatePhoneNumber(String id, String smsCode, String typeRequest, OnCompleteListener<Void> onComplete) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, smsCode);
        Objects.requireNonNull(mAuth.getCurrentUser()).updatePhoneNumber(credential)
                .addOnCompleteListener(onComplete)
                .addOnFailureListener(
                        e -> {
                            switch (typeRequest) {
                                case VER_CUR_USER: {
                                    mAuth.signOut();
                                    break;
                                }
                                case VER_CUR_USER_NEW_PHONE: {
                                    mAuth.getCurrentUser().delete();
                                    break;
                                }
                                case VER_NEW_USER: {
                                    break;
                                }
                            }
                            Log.d(TAG, e.getMessage());
                            msg.postValue(e.getMessage());
                        }
                );
    }

    public void verificationSMS(String phone_number, String typeRequest, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks onCodeSent) {
//        mAuth.getFirebaseAuthSettings().setAutoRetrievedSmsCodeForPhoneNumber("+79605605389", "123456");
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone_number)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onCodeSent)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void signInWithCredential(String id, String smsCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, smsCode);
        mAuth.signInWithCredential(credential).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        userMutableLiveData.postValue(Objects.requireNonNull(task.getResult()).getUser());
                    } else {
                        Log.d(TAG, Objects.requireNonNull(task.getException()).getMessage());
                        msg.postValue(task.getException().getMessage());
                    }
                }
        );
    }

    public void signInWithCredential(String id, String smsCode, OnCompleteListener<AuthResult> onComplete, OnFailureListener onFailure) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, smsCode);
        mAuth.signInWithCredential(credential).addOnCompleteListener(onComplete)
                .addOnFailureListener(onFailure);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void insertUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        User user = new User(name, surname, "", phone_number, city, gender, birth_date, "");

        if (mAuth.getCurrentUser() != null) {
            mReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user)
                    .addOnSuccessListener(
                            unused -> {

                                String id = customer_id;
                                mReference.child("CustomerUser").child(customer_id).setValue(new CustomerUser(customer_id, mAuth.getCurrentUser().getUid()))
                                        .addOnSuccessListener(onSuccessListener)
                                        .addOnFailureListener(onFailureListener);
                                Log.d(TAG, "Update data success");
                            }
                    )
                    .addOnFailureListener(e -> {
                        Log.d(TAG, e.getMessage());
                        msg.postValue(e.getMessage());
                    });
        }

    }


    public void signOut() {
        mAuth.signOut();
        userMutableLiveData.postValue(mAuth.getCurrentUser());
    }


    public MutableLiveData<String> getMsg() {
        return this.msg;
    }

    public void insertProduct(Product product) {
        String idProduct = mReference.push().getKey();
        HashMap<String, Object> mapProduct = new HashMap<>();
        mapProduct.put(PRODUCT_ID, idProduct);
        mapProduct.put(PRODUCT_NAME, product.product_name);
        mapProduct.put(PRODUCT_UNIT, product.product_unit);
        mapProduct.put(PRODUCT_DESC, product.product_description);
        mapProduct.put(PRODUCT_PRICE, product.product_price);
        mapProduct.put(PRODUCT_OLD_PRICE, product.product_old_price);
        mapProduct.put(PRODUCT_QUANTITY, product.product_quantity);
        mapProduct.put(PRODUCT_VISIBILITY, product.product_visibility);
        mapProduct.put(PRODUCT_CATEGORY, product.product_category);
        mapProduct.put(PRODUCT_LOGO, product.product_logo);

        assert idProduct != null;
        mReference.child(NODE_PRODUCT).child(idProduct)
                .updateChildren(mapProduct)
                .addOnFailureListener(
                        e -> Log.d(TAG, e.getMessage())
                );
    }


    public void insertProductBasket2(String productId, String productCount, String productMaxCount, OnSuccessListener<? super Void> onSuccess) {
        String idBasket = mReference.push().getKey();
        HashMap<String, Object> mapBasket = new HashMap<>();
        mapBasket.put(BASKET_ID, idBasket);
        mapBasket.put(BASKET_PRODUCT, productId);
        mapBasket.put(BASKET_PRODUCT_COUNT, productCount);
        mapBasket.put(BASKET_USER, UID);

        assert idBasket != null;
        mReference.child(NODE_BASKET).child(idBasket)
                .updateChildren(mapBasket)
                .addOnFailureListener(
                        e -> Log.d(TAG, e.getMessage())
                )
                .addOnSuccessListener(onSuccess)
                .addOnCompleteListener(
                        task -> {
                            final int[] countItem = {0};
                            //Здесь проверяме текущий товар в Таблице корзина
                            //Он не должен повторяться и превышать допустимое количсетво товара
                            if (task.isSuccessful()) {
                                Query mBasket = mReference.child(NODE_BASKET).orderByChild(BASKET_USER).equalTo(UID);
                                mBasket.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                try {
                                                    if (snapshot.exists()) {
                                                        int i = 0; //Итерации
                                                        //Перебираем все товары в корзине для текущего пользователя
                                                        for (DataSnapshot item : snapshot.getChildren()) {
                                                            //Если корзина не пуста
                                                            if (item.exists() && item.getValue() != null) {
                                                                Basket basket = item.getValue(Basket.class);

                                                                //Если товар равен добавляему товару
                                                                assert basket != null;
                                                                if (basket.basket_product.equals(productId)) {
                                                                    countItem[0] = countItem[0] + Integer.parseInt(basket.basket_product_count);

                                                                    //Если рассматриваем последний элемент, то не удалем его, а обновляем данные
                                                                    if (i == snapshot.getChildrenCount() - 1) {
                                                                        //Проверяем количество товара
                                                                        if (countItem[0] > Integer.parseInt(productMaxCount))
                                                                            countItem[0] = Integer.parseInt(productMaxCount);

                                                                        mapBasket.put(BASKET_ID, item.getKey());
                                                                        mapBasket.put(BASKET_PRODUCT_COUNT, String.valueOf(countItem[0]));

                                                                        item.getRef().updateChildren(mapBasket);
                                                                        break;
                                                                    }
                                                                    item.getRef().removeValue();
                                                                }
                                                                i++;
                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    Log.d(TAG, e.getMessage());
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                Log.d(TAG, error.getMessage());
                                            }
                                        }
                                );

                            }
                        }
                );
    }

    public void insertProductBasket(String productId, String productCount, String productMaxCount, OnSuccessListener<? super Void> onSuccess) {
        String idBasket = mReference.push().getKey();
        HashMap<String, Object> mapBasket = new HashMap<>();
        mapBasket.put(BASKET_ID, idBasket);
        mapBasket.put(BASKET_PRODUCT, productId);
        mapBasket.put(BASKET_PRODUCT_COUNT, productCount);
        mapBasket.put(BASKET_USER, UID);

        //TODO: NUllPOINTEREXCEPTION
        mReference.child(NODE_BASKET).get().addOnSuccessListener(
                snapshot -> {
                    Boolean found = false;
                    for (DataSnapshot item : snapshot.getChildren()) {
                        if (item.getValue(Basket.class).basket_product.equals(productId) && item.getValue(Basket.class).basket_user.equals(UID)) {
                            if (item.getValue(Basket.class).basket_product.equals(productId)) {
                                int countItem = 0;
                                countItem = Integer.parseInt(productCount) + Integer.parseInt(item.getValue(Basket.class).basket_product_count);
                                if (countItem > Integer.parseInt(productMaxCount))
                                    countItem = Integer.parseInt(productMaxCount);

                                mapBasket.put(BASKET_ID, item.getKey());
                                mapBasket.put(BASKET_PRODUCT_COUNT, String.valueOf(countItem));
                                item.getRef().updateChildren(mapBasket).addOnFailureListener(
                                        e -> Log.d(TAG, e.getMessage())
                                ).addOnSuccessListener(onSuccess);
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        mReference.child(NODE_BASKET).child(idBasket).updateChildren(mapBasket).addOnSuccessListener(onSuccess);
                    }
                }
        );
    }

    public void updateProductBasket(Product product, Basket basket, String countProduct) {

        HashMap<String, Object> mapBasket = new HashMap<>();
        mapBasket.put(BASKET_ID, basket.basket_id);
        mapBasket.put(BASKET_PRODUCT, product.product_id);
        mapBasket.put(BASKET_PRODUCT_COUNT, countProduct);
        mapBasket.put(BASKET_USER, UID);
        mReference.child(NODE_BASKET).child(basket.basket_id).updateChildren(mapBasket).addOnSuccessListener(
                unused -> Log.d(TAG, "Success")
        );

    }

    public void deleteProductBasket(Basket basket, OnSuccessListener<? super Void> onSuccess) {
        mReference.child(NODE_BASKET).child(basket.basket_id).removeValue().addOnSuccessListener(onSuccess);
    }

    public void deleteNews(News news, OnCompleteListener<Void> OnComplete) {
        mReference.child(NODE_NEWS_USERS)
                .orderByChild(USER_ID).equalTo(UID)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    NewsUser other = dataSnapshot.getValue(NewsUser.class);
                                    String id = (other != null) ? other.news_id : "";
                                    if (id.equals(news.news_id)) { //TODO: Если здесь сработает, то не нужно юудет удалять из адаптера, LiveData справится
                                        dataSnapshot.getRef().removeValue().addOnCompleteListener(OnComplete);
                                        return;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        }
                );
    }

    public void updateProductFavorite(Product product, OnSuccessListener<? super Void> onSuccess, OnSuccessListener<? super Void> onDelete) {
        if (product != null) {
            String idFavorite = mReference.push().getKey();
            HashMap<String, Object> mapProduct = new HashMap<>();
            mapProduct.put(FAVORITE_ID, idFavorite);
            mapProduct.put(FAVORITE_PRODUCT, product.product_id);
            mapProduct.put(FAVORITE_USER, UID);

            mReference.child(NODE_FAVORITE).get().addOnSuccessListener(
                    new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot snapshot) {
                            Boolean found = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot item : snapshot.getChildren()) {
                                    Favorite favorite = item.getValue(Favorite.class);
                                    if (favorite.favorite_product.equals(product.product_id) && favorite.favorite_user.equals(UID)) {
                                        item.getRef().removeValue().addOnFailureListener(
                                                e -> Log.d(TAG, e.getMessage())
                                        ).addOnSuccessListener(onDelete);
                                        found = true;
                                    }
                                }
                                if (!found) {
                                    mReference.child(NODE_FAVORITE).child(idFavorite).updateChildren(mapProduct).addOnSuccessListener(onSuccess);
                                }
                            } else {
                                mReference.child(NODE_FAVORITE).child(idFavorite).updateChildren(mapProduct).addOnSuccessListener(onSuccess);
                            }
                        }
                    }
            );

        }
    }

    public void isProductFavorite(Product product, ValueEventListener onComplete) {
        if (product != null) {
            mReference.child(NODE_FAVORITE).orderByChild(FAVORITE_USER).equalTo(UID).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                String key = item.getKey();
                                if (item.getValue(Favorite.class).favorite_product.equals(product.product_id)){
                                    mReference.child(NODE_FAVORITE).child(key).get().addOnSuccessListener(null);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    }
            );
        }
    }

    public void isProductFavorite(Product product, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener) {
        if (product != null) {
            mReference.child(NODE_FAVORITE).orderByChild(FAVORITE_USER).equalTo(UID).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                String key = item.getKey();
                                if (item.getValue(Favorite.class).favorite_product.equals(product.product_id)){
                                    mReference.child(NODE_FAVORITE).child(key).get().addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    }
            );
        }
    }

}