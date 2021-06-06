package com.example.cafe.screens.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.cafe.database.firebase.AppFirebaseRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import static com.example.cafe.utilits.constants.TYPE_FIREBASE;

public class AuthViewModel extends AndroidViewModel {
    private Application mContext;
    AppFirebaseRepository repository = new AppFirebaseRepository();

    public AuthViewModel(@NonNull @NotNull Application application) {
        super(application);
        mContext = application;
    }

    public void initDataBase(String type, OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail) {
        switch (type) {
            case TYPE_FIREBASE: {
//                Toast.makeText(mContext, "FireBase", Toast.LENGTH_SHORT).show();
                repository.signInToDataBase(onSuccess, onFail);
                break;
            }
        }
    }

    public void verifyPhoneNumber(PhoneAuthProvider.OnVerificationStateChangedCallbacks onCallback) {
        repository.verifyPhoneNumber(onCallback);
    }
}