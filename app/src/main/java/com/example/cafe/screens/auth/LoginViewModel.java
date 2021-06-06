package com.example.cafe.screens.auth;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.cafe.database.firebase.AppFirebaseRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import static com.example.cafe.utilits.constants.TYPE_FIREBASE;

public class LoginViewModel extends ViewModel {
    private Application context;
    AppFirebaseRepository repository = new AppFirebaseRepository();


    public void initDataBase(String type, OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail) {
        switch (type) {
            case TYPE_FIREBASE: {
                repository.logInInToDataBase(onSuccess, onFail);
                break;
            }
        }
    }

    public void updateUserInfo(String name, String surname, String email_address, String phone_number, String city, String gender, OnSuccessListener<? super Void> onSuccess, OnFailureListener onFail) {
        repository.updateUserInfo(name, surname, email_address, phone_number, city, gender, onSuccess, onFail);
    }

    public void verifyPhoneNumber(PhoneAuthProvider.OnVerificationStateChangedCallbacks onCallback){
        repository.verifyPhoneNumber(onCallback);
    }

    // TODO: Implement the ViewModel
}