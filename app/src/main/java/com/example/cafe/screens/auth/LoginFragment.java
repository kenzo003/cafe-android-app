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
import com.example.cafe.databinding.LoginFragmentBinding;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentBinding mBinding;
    private String mPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = LoginFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    private void init() {
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mBinding.lfBtnSignupEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void registerUser() {
        String name = mBinding.lfEditTxtName.getText().toString().trim();
        String surname = mBinding.lfEditTxtSurname.getText().toString().trim();
        String password = mBinding.lfEditTxtPassword.getText().toString();
        String email_address = mBinding.lfEditTxtEmail.getText().toString().trim();
        String phone_number = mBinding.lfEditTxtPhoneNumber.getText().toString().trim();
        String birth_date = mBinding.lfEditTxtDateOfBirth.getText().toString();
        String city = mBinding.lfEditTxtCity.getText().toString().trim();
        String gender = "male";
        int address;

        //TODO Здесь должна быть валидация данных
        if (!email_address.isEmpty() && !password.isEmpty()) {
            constants.EMAIL = email_address;
            constants.PASSWORD = password;
            //Создание аккаунта на основе почты
            mViewModel.initDataBase(constants.TYPE_FIREBASE,
                    new OnSuccessListener<AuthResult>() {
                //Акк создан успешно
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mViewModel.updateUserInfo(name, surname, email_address, phone_number, city, gender,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d(constants.TAG, "User from email and password for signUp. Create Success");
                                            constants.PHONE_NUMBER = phone_number;
                                            //Проводим провреку по номеру телефона
                                            mViewModel.verifyPhoneNumber(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                                //Код отправлен
                                                @Override
                                                public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                                    super.onCodeSent(s, forceResendingToken);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("id", s);
                                                    bundle.putString("phone", phone_number);
                                                    bundle.putString("user", constants.AUTH.getCurrentUser().getUid());
                                                    constants.APP_ACTIVITY.navController.navigate(R.id.enterPinFragment, bundle);
                                                }


                                                @Override
                                                public void onCodeAutoRetrievalTimeOut(@NonNull @NotNull String s) {
                                                    super.onCodeAutoRetrievalTimeOut(s);
                                                    Log.d(constants.TAG, "onCodeAutoRetrievalTimeOut");
                                                    //TODO повторить отправку смс
                                                }

                                                @Override
                                                public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                                    constants.AUTH.getCurrentUser().updatePhoneNumber(phoneAuthCredential);
                                                    Log.d(constants.TAG, "onVerificationCompleted");
                                                }


                                                @Override
                                                public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                                    Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    constants.AUTH.getCurrentUser().delete();
                                                    Log.d(constants.TAG, e.getMessage());
                                                }
                                            });
                                        }
                                    },
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
                                            Log.d(constants.TAG, e.getMessage());
                                        }
                                    }
                            );
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(),e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("falll", e.getMessage());
                            constants.AUTH.signOut();
                        }
                    }
            );

        } else {
            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), "Enter data", Toast.LENGTH_LONG);
        }
    }


}