package com.example.cafe.database.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Category;
import com.example.cafe.models.CustomerUser;
import com.example.cafe.models.News;
import com.example.cafe.models.NewsUser;
import com.example.cafe.models.Product;
import com.example.cafe.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.cafe.utilits.constants.CATEGORY_ID;
import static com.example.cafe.utilits.constants.NODE_NEWS_USERS;
import static com.example.cafe.utilits.constants.NODE_PRODUCT;
import static com.example.cafe.utilits.constants.PRODUCT_DESC;
import static com.example.cafe.utilits.constants.PRODUCT_ID;
import static com.example.cafe.utilits.constants.PRODUCT_NAME;
import static com.example.cafe.utilits.constants.PRODUCT_PRICE;
import static com.example.cafe.utilits.constants.PRODUCT_QUANTITY;
import static com.example.cafe.utilits.constants.PRODUCT_UNIT;
import static com.example.cafe.utilits.constants.PRODUCT_VISIBILITY;
import static com.example.cafe.utilits.constants.STORAGE_NODE_NEWS;
import static com.example.cafe.utilits.constants.TAG;
import static com.example.cafe.utilits.constants.USER_ID;
import static com.example.cafe.utilits.constants.VER_CUR_USER;
import static com.example.cafe.utilits.constants.VER_CUR_USER_NEW_PHONE;
import static com.example.cafe.utilits.constants.VER_NEW_USER;

public class AppRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<String> msg;
    private MutableLiveData<List<News>> allNews;
    private MutableLiveData<List<Category>> allCategory;
    private MutableLiveData<List<Product>> allProduct;
    private FirebaseUser user;
    private StorageReference storageRef;

    public AppRepository() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();
        userMutableLiveData = new MutableLiveData<>();
        msg = new MutableLiveData<>();
        allNews = new AllNewsLiveData();
        allCategory = new AllCategoryLiveData();
        allProduct = new AllProductLiveData();
        storageRef = FirebaseStorage.getInstance().getReference();
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

    public void insertProduct(Product product) {
        String idProduct = mReference.push().getKey();
        HashMap<String, Object> mapProduct = new HashMap<>();
        mapProduct.put(PRODUCT_ID, idProduct);
        mapProduct.put(PRODUCT_NAME, product.product_name);
        mapProduct.put(PRODUCT_UNIT, product.product_unit);
        mapProduct.put(PRODUCT_DESC, product.product_description);
        mapProduct.put(PRODUCT_PRICE, product.product_price);
        mapProduct.put(PRODUCT_QUANTITY, product.product_quantity);
        mapProduct.put(PRODUCT_VISIBILITY, product.product_visibility);
        mapProduct.put(CATEGORY_ID, product.category_id);

        assert idProduct != null;
        mReference.child(NODE_PRODUCT).child(idProduct)
                .updateChildren(mapProduct)
                .addOnFailureListener(
                        e -> Log.d(TAG, e.getMessage())
                );
    }

    public void signupUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id, OnCompleteListener<AuthResult> onComplete) {
        mAuth.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener((OnCompleteListener<AuthResult>) onComplete)
                .addOnSuccessListener(
                        authResult -> insertUser(name, surname, email_address, phone_number, city, gender, birth_date, address, password, customer_id)
                )
                .addOnFailureListener(
                        e -> {
                            Log.d(TAG, e.getMessage());
                            msg.postValue(e.getMessage());
                        }
                );
    }

    public void loginUser(String email_address, String password, OnCompleteListener<AuthResult> onComplete) {
        mAuth.signInWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(onComplete)
                .addOnFailureListener(
                        e -> {
                            Log.d(TAG, e.getMessage());
                            msg.postValue(e.getMessage());
                        }
                );
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

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void insertUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id) {
        User user = new User(name, surname, email_address, phone_number, city, gender, birth_date, address);

        mReference.child("users").child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(user)
                .addOnSuccessListener(
                        unused -> {
                            mAuth.getCurrentUser().getUid();
                            String id = UUID.randomUUID().toString();
                            mReference.child("CustomerUser").child(id).setValue(new CustomerUser(customer_id, mAuth.getCurrentUser().getUid()));
                            Log.d(TAG, "Update data success");
                        }
                )
                .addOnFailureListener(e -> {
                    Log.d(TAG, e.getMessage());
                    msg.postValue(e.getMessage());
                });
    }

    public void insertNews(News news) {

    }

    public void signOut() {
        mAuth.signOut();
        userMutableLiveData.postValue(mAuth.getCurrentUser());
    }


    public MutableLiveData<String> getMsg() {
        return this.msg;
    }

    public void deleteNews(News news, OnCompleteListener<Void> OnComplete) {
        mReference.child(NODE_NEWS_USERS)
                .orderByChild(USER_ID).equalTo(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    if (Objects.requireNonNull(dataSnapshot.getValue(NewsUser.class)).news_id.equals(news.news_id))
                                        dataSnapshot.getRef().removeValue().addOnCompleteListener((OnCompleteListener<Void>) OnComplete);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        }
                );
    }

    public void getImage(String url) {
        storageRef.child(STORAGE_NODE_NEWS + url).getBytes(1024 * 1024)
                .addOnSuccessListener(
                        bytes -> {

                        }
                );
    }
}
