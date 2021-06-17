package com.example.cafe.screens.auth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.FragmentSignInPhoneBinding;
import com.example.cafe.utilits.Validate;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;


public class SignInPhoneFragment extends Fragment {
    private OTPVewModel otpVewModel;
    private FragmentSignInPhoneBinding mBinding;
    private UserViewModel userViewModel;
    private MainActivity activity;
    String id;
    String sms;
    String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignInPhoneBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        otpVewModel = new ViewModelProvider(this).get(OTPVewModel.class);
        activity = ((MainActivity) getActivity());
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();
        hide();

        mBinding.fpRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.queryCode.callOnClick();
            }
        });
        mBinding.fpLogin.setOnClickListener(
                v -> {
                    sms = mBinding.fpEditSms.getText().toString();
                    hideSoftKeyboard(getActivity());
                    if (!id.isEmpty() && !sms.isEmpty())
                        mBinding.fpProgressBar.setVisibility(View.VISIBLE);
                    userViewModel.signInWithCredential(id, sms,
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                        activity.navController.navigate(R.id.news_nav);
                                    if (task.isCanceled()) {
                                        Log.d(constants.TAG, task.getException().getMessage());
                                    }
                                }
                            },
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    Log.d(constants.TAG, e.getMessage());
                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                    Snackbar.make(getContext(), mBinding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                }
                            });
                }
        );

        mBinding.queryCode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phone = mBinding.fpEditPhone.getText().toString().trim();

                        if (Validate.phoneValid(mBinding.fpEditPhone, phone)) {
                            mBinding.fpProgressBar.setVisibility(View.VISIBLE);
                            hideSoftKeyboard(activity);

                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phone)
                                    .setTimeout(60L, TimeUnit.SECONDS)
                                    .setActivity(requireActivity())
                                    .setCallbacks(
                                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                @Override
                                                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                                    Log.d(constants.TAG, "complete");
                                                }

                                                @Override
                                                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                                    Log.d(constants.TAG, e.getMessage());
                                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                                    Snackbar.make(getContext(), mBinding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                    super.onCodeSent(s, forceResendingToken);
                                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                                    show();
                                                    id = s;
                                                    mBinding.fpRepeat.setEnabled(false);
                                                    otpVewModel.getTick().cancel();
                                                    otpVewModel.getTick().start();
                                                }

                                                @Override
                                                public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                                                    super.onCodeAutoRetrievalTimeOut(s);
                                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                                    mBinding.fpRepeat.setEnabled(true);
                                                }
                                            }
                                    )
                                    .build();
                            PhoneAuthProvider.verifyPhoneNumber(options);
                        }
                    }
                }
        );


        otpVewModel.getTimer().observe(requireActivity(),
                s -> mBinding.fpTimer.setText(s));


    }

    private void show() {
        mBinding.fpEditSms.setVisibility(View.VISIBLE);
        mBinding.fpEditPhone.setVisibility(View.GONE);
        mBinding.queryCode.setVisibility(View.GONE);
        mBinding.fpLogin.setVisibility(View.VISIBLE);
        mBinding.fpPanelRepeat.setVisibility(View.VISIBLE);
    }

    private void hide() {
        mBinding.fpEditSms.setVisibility(View.GONE);
        mBinding.fpEditPhone.setVisibility(View.VISIBLE);
        mBinding.queryCode.setVisibility(View.VISIBLE);
        mBinding.fpLogin.setVisibility(View.GONE);
        mBinding.fpPanelRepeat.setVisibility(View.GONE);
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}