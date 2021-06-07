package com.example.cafe.database.firebase;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.CustomerUser;
import com.example.cafe.models.User;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AppRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private MutableLiveData<FirebaseUser> userMutableLiveData;
    private MutableLiveData<String> msg;
    private FirebaseUser user;

    public AppRepository() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();
        userMutableLiveData = new MutableLiveData<>();
        msg = new MutableLiveData<>();

    }


    public void signupUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id, OnCompleteListener<AuthResult> onComplete) {
        mAuth.createUserWithEmailAndPassword(email_address, password)
                .addOnCompleteListener((OnCompleteListener<AuthResult>) onComplete)
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                insertUser(name, surname, email_address, phone_number, city, gender, birth_date, address, password, customer_id);
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(constants.TAG, e.getMessage());
                                msg.postValue(e.getMessage());
                            }
                        }
                );
    }

    public void loginUser(String email_address, String password, OnCompleteListener<AuthResult> onComplete) {
        mAuth.signInWithEmailAndPassword(email_address, password)
                .addOnCompleteListener(onComplete)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(constants.TAG, e.getMessage());
                                msg.postValue(e.getMessage());
                            }
                        }
                );
    }

    public void updatePhoneNumber(String id, String smsCode, String typeRequest, OnCompleteListener<Void> onComplete) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id, smsCode);
        mAuth.getCurrentUser().updatePhoneNumber(credential)
                .addOnCompleteListener(onComplete)
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
//                                if (mAuth.getCurrentUser().getPhoneNumber() == null) {
//                                    mAuth.getCurrentUser().delete();
//                                }
                                switch (typeRequest) {
                                    case constants.VER_CUR_USER: {
                                        mAuth.signOut();
                                        break;
                                    }
                                    case constants.VER_CUR_USER_NEW_PHONE: {
                                        mAuth.getCurrentUser().delete();
                                        break;
                                    }
                                    case constants.VER_NEW_USER: {
                                        break;
                                    }
                                }
                                Log.d(constants.TAG, e.getMessage());
                                msg.postValue(e.getMessage());
                            }
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
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userMutableLiveData.postValue(task.getResult().getUser());
                        } else {
                            Log.d(constants.TAG, task.getException().getMessage());
                            msg.postValue(task.getException().getMessage());
                        }
                    }
                }
        );
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void insertUser(String name, String surname, String email_address, String phone_number, String city, String gender, String birth_date, String address, String password, String customer_id) {
        User user = new User(name, surname, email_address, phone_number, city, gender, birth_date, address);

        mReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                if (mAuth.getCurrentUser().getUid() != null) {
                                    String id = mAuth.getCurrentUser().getUid() + customer_id;
                                    id = UUID.randomUUID().toString();
                                    mReference.child("CustomerUser").child(id).setValue(new CustomerUser(customer_id, mAuth.getCurrentUser().getUid()));
                                    Log.d(constants.TAG, "Update data success");
                                } else {
                                    return;
                                }

                            }
                        }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(constants.TAG, e.getMessage());
                        msg.postValue(e.getMessage());
                    }
                });
    }

    public void signOut() {
        mAuth.signOut();
        userMutableLiveData.postValue(mAuth.getCurrentUser());
    }

    public MutableLiveData<String> getMsg() {
        return msg;
    }
}
