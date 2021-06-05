package com.example.cafe.screens.auth;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding mBinding;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LoginFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        mBinding.lfBtnSignupEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity)getActivity();
                activity.navController.popBackStack();
                activity.navController.navigate(R.id.newsFragment);
            }
        });
    }

    public void onClick(View view) {
        MainActivity activity = (MainActivity)getActivity();
        activity.navController.navigate(R.id.newsFragment);
    }

}