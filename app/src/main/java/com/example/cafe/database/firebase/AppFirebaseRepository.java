package com.example.cafe.database.firebase;

import androidx.lifecycle.LiveData;

import com.example.cafe.database.DataBaseRepository;
import com.example.cafe.models.User;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class AppFirebaseRepository implements DataBaseRepository {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    LiveData<List<User>> user;

    @Override
    public void insert(User user, Function onSuccess, Function onFailed) {

    }

    @Override
    public void updateUserInfo(String name, String surname, String email_address, String phone_number, String city, String gender, OnSuccessListener<? super Void> onSuccess, OnFailureListener onFail) {
        mDatabaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFail);
    }


    @Override
    public void delete(User user) {

    }

    @Override
    public void signInToDataBase(OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail) {
        mAuth.signInWithEmailAndPassword(constants.EMAIL, constants.PASSWORD)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFail);
    }

    @Override
    public void logInInToDataBase(OnSuccessListener<? super AuthResult> onSuccess, OnFailureListener onFail) {
        mAuth.createUserWithEmailAndPassword(constants.EMAIL, constants.PASSWORD)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFail);
    }

    @Override
    public void verifyPhoneNumber(PhoneAuthProvider.OnVerificationStateChangedCallbacks onCallback) {
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(constants.PHONE_NUMBER)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(constants.APP_ACTIVITY)
                .setCallbacks(onCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    @Override
    public void signOut() {
        mAuth.signOut();
    }
}
