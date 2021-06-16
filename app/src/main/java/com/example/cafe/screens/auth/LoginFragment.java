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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.LoginFragmentBinding;
import com.example.cafe.utilits.constants;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {

    private UserViewModel userViewModel;
    private OTPVewModel otpVewModel;
    private LoginFragmentBinding mBinding;
    private String mPhoneNumber;
    private String id;
    private String sms;
    private MainActivity activity;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = ((MainActivity) getActivity());
        mBinding = LoginFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();
        otpVewModel = new ViewModelProvider(this).get(OTPVewModel.class);
        hide();
        mBinding.lfBtnSignupEnter.setOnClickListener(
                v -> {

                    String phone_number = mBinding.lfEditTxtPhoneNumber.getText().toString().trim();
                    String birth_date = mBinding.lfEditTxtDateOfBirth.getText().toString();
                    String city = mBinding.lfEditTxtCity.getText().toString().trim();
                    String gender = "male";
                    String customers = constants.ID_CUSTOMER;
                    String address = "";
                    String name = mBinding.lfEditTxtName.getText().toString().trim();
                    String surname = mBinding.lfEditTxtSurname.getText().toString().trim();

                    sms = mBinding.lfEditCode.getText().toString();
                    hideSoftKeyboard(getActivity());

                    if (!id.isEmpty() && !sms.isEmpty())
//                        mBinding.fpProgressBar.setVisibility(View.VISIBLE);
                        userViewModel.signInWithCredential(id, sms,
                                task -> {
                                    if (task.isSuccessful()) {
                                        userViewModel.insertUser(name, surname, "", phone_number, city, gender, birth_date, "", "", customers,
                                                o -> activity.navController.navigate(R.id.news_nav),
                                                e -> {
                                                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                                    }
                                                    Snackbar.make(getContext(), mBinding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                                });
                                    }

                                },
                                e -> {
                                    Log.d(constants.TAG, e.getMessage());
                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                    Snackbar.make(getContext(), mBinding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                });
                }
        );
        mBinding.lfBtnQueryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loginUser() {
        String phone_number = mBinding.lfEditTxtPhoneNumber.getText().toString().trim();
        mBinding.fpProgressBar.setVisibility(View.VISIBLE);

        if (!phone_number.isEmpty()) {
            PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                    .setPhoneNumber(phone_number)
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
                                    mBinding.lfBtnRepeat.setEnabled(false);
                                    otpVewModel.getTick().cancel();
                                    otpVewModel.getTick().start();
                                }

                                @Override
                                public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                                    super.onCodeAutoRetrievalTimeOut(s);
                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                    mBinding.lfBtnRepeat.setEnabled(true);
                                }
                            }
                    ).build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }

        otpVewModel.getTimer().observe(this,
                new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        mBinding.lfTimer.setText(s);
                    }
                });
    }

    private void show() {
        mBinding.lfEditCode.setVisibility(View.VISIBLE);
        mBinding.lfBtnQueryCode.setVisibility(View.GONE);
        mBinding.lfBtnSignupEnter.setVisibility(View.VISIBLE);
        mBinding.lfPanelRepeat.setVisibility(View.VISIBLE);
    }

    private void hide() {
        mBinding.lfEditCode.setVisibility(View.GONE);
        mBinding.lfBtnQueryCode.setVisibility(View.VISIBLE);
        mBinding.lfBtnSignupEnter.setVisibility(View.GONE);
        mBinding.lfPanelRepeat.setVisibility(View.GONE);
    }

    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}