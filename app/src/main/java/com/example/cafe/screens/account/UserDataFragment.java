package com.example.cafe.screens.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.databinding.FragmentUserDataBinding;
import com.example.cafe.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;


public class UserDataFragment extends Fragment {
    private FragmentUserDataBinding mBinding;
    private AccountViewModel mViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentUserDataBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        mViewModel.getUserData(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot snapshot) {
                if (snapshot.exists()){
                    User user = snapshot.getValue(User.class);
                    mBinding.fudTxtVBirthDate.setText(user.birth_date);
                    mBinding.fudTxtVCity.setText(user.city);
                    String gender = "";
                    if (user.gender.equals("man")){
                        gender = "Мужчина";
                    }
                    else {
                        gender = "Женщина";
                    }
                    mBinding.fudTxtVGender.setText(gender);
                    String name = user.name + " " + user.surname;
                    mBinding.fudTxtVName.setText(name);
                    mBinding.fudTxtVPhone.setText(user.phone_number);
                    mBinding.fudTxtVEmail.setText(user.email_address);
                }
            }
        });

    }
}