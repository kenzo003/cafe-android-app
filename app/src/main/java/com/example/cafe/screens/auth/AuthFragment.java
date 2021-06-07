package com.example.cafe.screens.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.R;
import com.example.cafe.databinding.AuthFragmentBinding;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class AuthFragment extends Fragment {

    private AuthViewModel mViewModel;
    private AuthFragmentBinding mBinding;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String mPhoneNumber;


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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");

        mBinding.afBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mBinding.afEditTxtPhone.getText().toString().trim();
                String password = mBinding.afEditTxtPassword.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    constants.EMAIL = email;
                    constants.PASSWORD = password;
                    mViewModel.initDataBase(constants.TYPE_FIREBASE, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
//                            constants.APP_ACTIVITY.navController.navigate(R.id.action_authFragment2_to_enterPinFragment);
                            mViewModel.verifyPhoneNumber(
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                        @Override
                                        public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                            super.onCodeSent(s, forceResendingToken);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("id", s);
                                            bundle.putString("phone", constants.AUTH.getCurrentUser().getPhoneNumber().toString());
                                            bundle.putString("user", constants.AUTH.getCurrentUser().getUid());
                                            constants.APP_ACTIVITY.navController.navigate(R.id.enterPinFragment, bundle);
                                        }

                                        @Override
                                        public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {

                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                            constants.AUTH.signOut();
                                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(constants.TAG, e.getMessage());
                                        }
                                    }
                            );
//                            Toast.makeText(getContext(), "Здравствуйте " + authResult.getUser().getEmail(), Toast.LENGTH_LONG).show();
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(constants.TAG, e.getMessage());
                        }
                    });

                } else {
                    Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), "Заполните поля", Toast.LENGTH_LONG).show();
                }
            }
        });


        mBinding.afBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constants.APP_ACTIVITY.navController.navigate(R.id.action_authFragment2_to_loginFragment3);
            }
        });

//        mBinding.afBtnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity activity = (MainActivity) getActivity();
//                activity.navController.navigate(R.id.action_authFragment2_to_enterPinFragment);
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        this.init();

        constants.AUTH = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull @NotNull String id, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(id, token);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("phone", mPhoneNumber);
                constants.APP_ACTIVITY.navController.navigate(R.id.action_authFragment2_to_enterPinFragment, bundle);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                constants.AUTH.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
            }
        };
    }

    private void authUser() {
        mPhoneNumber = mBinding.afEditTxtPhone.getText().toString();
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(constants.AUTH)
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS)
                .setActivity(constants.APP_ACTIVITY)
                .setCallbacks(mCallback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}