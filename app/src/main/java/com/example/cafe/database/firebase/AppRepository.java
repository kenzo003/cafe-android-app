package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.*;

import org.jetbrains.annotations.NotNull;

public class AppRepository {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    public AppRepository() {
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        userMutableLiveData = new MutableLiveData<>();
    }


    public void signupUser(String email_address, String phone_number) {
        mAuth.signInWithEmailAndPassword(constants.EMAIL, constants.PASSWORD)
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                userMutableLiveData.postValue(mAuth.getCurrentUser());
                            }
                        }
                )
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(constants.TAG, e.getMessage());
                            }
                        }
                );
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
