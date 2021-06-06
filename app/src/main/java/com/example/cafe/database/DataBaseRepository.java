package com.example.cafe.database;

import androidx.lifecycle.LiveData;

import com.example.cafe.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.List;
import java.util.function.Function;

public interface DataBaseRepository {
    LiveData<List<User>> allUsers = null;


    public void verifyPhoneNumber(PhoneAuthProvider.OnVerificationStateChangedCallbacks onCallback);

    public void insert(User user, Function onSuccess, Function onFailed);

    public void updateUserInfo(String name, String surname, String email_address, String phone_number, String city, String gender, OnSuccessListener<? super Void> onSuccess, OnFailureListener onFail);

    public void delete(User user);

    void signInToDataBase(OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail);

    void logInInToDataBase(OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail);

    public void signOut();
}
