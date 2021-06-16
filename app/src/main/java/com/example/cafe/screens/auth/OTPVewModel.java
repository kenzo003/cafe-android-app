package com.example.cafe.screens.auth;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OTPVewModel extends ViewModel {
    MutableLiveData<String> timer;
    CountDownTimer tick;

    public CountDownTimer getTick() {
        return tick;
    }

    public OTPVewModel() {
        this.timer = new MutableLiveData<>();
        tick =  new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String s = String.valueOf(millisUntilFinished/1000);
                timer.postValue(s);
            }

            @Override
            public void onFinish() {

            }
        };

    }

    public MutableLiveData<String> getTimer() {
        return timer;
    }
}
