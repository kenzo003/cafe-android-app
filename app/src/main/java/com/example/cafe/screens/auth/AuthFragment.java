package com.example.cafe.screens.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.AuthFragmentBinding;

public class AuthFragment extends Fragment {

    private AuthViewModel mViewModel;
    private AuthFragmentBinding mBinding;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = AuthFragmentBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    private void init() {
        mViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        mBinding.afBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.navController.navigate(R.id.action_authFragment2_to_loginFragment3);
            }
        });

        mBinding.afBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.navController.navigate(R.id.action_authFragment2_to_enterPinFragment);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        this.init();
    }
}