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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class AuthFragment extends Fragment {

    private UserViewModel userViewModel;
    private AuthFragmentBinding mBinding;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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

        mBinding.afBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_authFragment2_to_loginFragment);
            }
        });

        mBinding.afBtnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = mBinding.afEditTxtEmail.getText().toString().trim();
                        String password = mBinding.afEditTxtPassword.getText().toString().trim();

                        if (!email.isEmpty() && !password.isEmpty()) {
                            userViewModel.loginUser(email, password,
                                    new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                userViewModel.getUserMutableLiveData().postValue(FirebaseAuth.getInstance().getCurrentUser()); //TODO убрать, используется для отключения смс

//                                                if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() == null){
//                                                    FirebaseAuth.getInstance().getCurrentUser().delete();
//                                                    userViewModel.getMsg().postValue("Пустой номер телефона");
//                                                    return;
//                                                }

//                                                //TODO вернуть, для включения верикфикации смс
//                                                userViewModel.verificationSMS(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
//                                                        constants.VER_CUR_USER, getActivity(),
//                                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//                                                            @Override
//                                                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
//                                                                userViewModel.getUserMutableLiveData().postValue(FirebaseAuth.getInstance().getCurrentUser());
//                                                            }
//                                                            @Override
//                                                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
//
//                                                                switch (constants.VER_CUR_USER) {
//                                                                    case constants.VER_CUR_USER: {
//                                                                        FirebaseAuth.getInstance().signOut();
//                                                                        break;
//                                                                    }
//                                                                    case constants.VER_CUR_USER_NEW_PHONE: {
//                                                                        FirebaseAuth.getInstance().getCurrentUser().delete();
//                                                                        break;
//                                                                    }
//                                                                    case constants.VER_NEW_USER: {
//                                                                        break;
//                                                                    }
//                                                                }
//                                                                userViewModel.getMsg().postValue(e.getMessage());
//                                                                Log.d(constants.TAG, e.getMessage());
//                                                            }
//
//                                                            @Override
//                                                            public void onCodeSent(@NonNull @NotNull String id, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                                                super.onCodeSent(id, forceResendingToken);
//                                                                Bundle bundle = new Bundle();
//                                                                bundle.putString("id", id);
//                                                                constants.APP_ACTIVITY.navController.navigate(R.id.action_authFragment2_to_enterPinFragment2, bundle);
//                                                            }
//
//                                                            @Override
//                                                            public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
//                                                                super.onCodeAutoRetrievalTimeOut(s);
//                                                            }
//                                                        });
                                            }
                                        }
                                    });
                        } else {
                            Snackbar.make(getContext(), mBinding.getRoot(), "Заполните поля", Snackbar.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }

    public UserViewModel getUserViewModel() {
        return userViewModel;
    }


}