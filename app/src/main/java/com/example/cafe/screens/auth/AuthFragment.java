package com.example.cafe.screens.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.AuthFragmentBinding;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;

public class AuthFragment extends Fragment {

    private UserViewModel userViewModel;
    private AuthFragmentBinding mBinding;
    private NavController navController;
    private MainActivity activity;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = ((MainActivity) getActivity());
        mBinding = AuthFragmentBinding.inflate(inflater);
        init();
        return mBinding.getRoot();
    }


    private void init() {
        navController = ((MainActivity) getActivity()).navController;
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();

        userViewModel.getUserMutableLiveData().observe(requireActivity(),
                new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser firebaseUser) {
                        if (firebaseUser != null) {
                            navController.navigate(R.id.news_nav);
                        }
                    }
                });

        mBinding.afBtnSignup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.navController.navigate(R.id.action_authFragment2_to_signInPhoneFragment);
                    }
                }
        );
    }

    public UserViewModel getUserViewModel() {
        return userViewModel;
    }


}