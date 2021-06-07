package com.example.cafe.screens.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.database.firebase.AppRepository;
import com.google.firebase.auth.*;

import org.jetbrains.annotations.NotNull;

public class UserViewModel extends AndroidViewModel {
    AppRepository repository = new AppRepository();
    private MutableLiveData<FirebaseUser> userMutableLiveData;

    public UserViewModel(@NonNull @NotNull Application application) {
        super(application);
        userMutableLiveData = repository.getUserMutableLiveData();
    }

    @NonNull
    @NotNull
    @Override
    public <T extends Application> T getApplication() {
        return super.getApplication();
    }

    public void signupUser(String email_address, String phone_number){
        repository.signupUser(email_address, phone_number);
    }

    public MutableLiveData<FirebaseUser> getUserMutableLiveData() {
        return userMutableLiveData;
    }
}
