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

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.FragmentEmailBinding;
import com.example.cafe.utilits.Validate;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class SignInEmailFragment extends Fragment {
    private FragmentEmailBinding mBinding;
    private UserViewModel userViewModel;
    private MainActivity activity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEmailBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        activity = ((MainActivity) getActivity());
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();

        mBinding.feBtnFab.setOnClickListener(v -> activity.navController.navigateUp());
        mBinding.feBtnForgot.setOnClickListener(
                v -> {
                    String email = mBinding.feEditEmail.getText().toString().trim();
                    if (Validate.emailValid(mBinding.feEditEmail, email)) {
                        userViewModel.sendPasswordResetEmail(email, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Snackbar.make(getContext(), mBinding.getRoot(), "На почту " + email + " напралено письмо с инструкцией по восстановлению пароля", Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }
        );

        mBinding.feBtnLogin.setOnClickListener(
                v -> {
                    String password = mBinding.feEditPassword.getText().toString().trim();
                    String email = mBinding.feEditEmail.getText().toString().trim();
                    if (Validate.emailValid(mBinding.feEditEmail, email)
                            && Validate.passwordValid(mBinding.feEditPassword, password)) {

                        mBinding.fpProgressBar.setVisibility(View.VISIBLE);
                        hideSoftKeyboard(getActivity());

                        userViewModel.loginUser(email, password,
                                task -> {
                                    if (task.isSuccessful()) {
                                        activity.navController.navigate(R.id.news_nav);
                                        mBinding.fpProgressBar.setVisibility(View.GONE);
                                    }
                                },
                                e -> {
                                    mBinding.fpProgressBar.setVisibility(View.GONE);
                                    Snackbar.make(getContext(), mBinding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
                                    Log.d(constants.TAG, e.getMessage());
                                });
                    }


                }
        );
    }


    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}