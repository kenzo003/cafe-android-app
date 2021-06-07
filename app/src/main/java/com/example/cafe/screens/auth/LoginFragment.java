package com.example.cafe.screens.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.LoginFragmentBinding;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

public class LoginFragment extends Fragment {

    private UserViewModel userViewModel;
    private LoginFragmentBinding mBinding;
    private String mPhoneNumber;

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
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();
        mBinding.lfBtnSignupEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void loginUser() {
        String name = mBinding.lfEditTxtName.getText().toString().trim();
        String surname = mBinding.lfEditTxtSurname.getText().toString().trim();
        String password = mBinding.lfEditTxtPassword.getText().toString();
        String email_address = mBinding.lfEditTxtEmail.getText().toString().trim();
        String phone_number = mBinding.lfEditTxtPhoneNumber.getText().toString().trim();
        String birth_date = mBinding.lfEditTxtDateOfBirth.getText().toString();
        String city = mBinding.lfEditTxtCity.getText().toString().trim();
        String gender = "male";
        String customers = constants.ID_CUSTOMER;
        String address = "";

        if (!email_address.isEmpty() && !password.isEmpty()) {
            userViewModel.signupUser(name,surname, email_address, phone_number,city,gender,birth_date,address,password,customers,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userViewModel.verificationSMS(phone_number, constants.VER_NEW_USER, getActivity(),
                                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                            @Override
                                            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
//                                                if (phoneAuthCredential.getSmsCode())
                                                userViewModel.getUserMutableLiveData().postValue(FirebaseAuth.getInstance().getCurrentUser());
                                            }

                                            @Override
                                            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                                switch (constants.VER_NEW_USER) {
                                                    case constants.VER_CUR_USER: {
                                                        FirebaseAuth.getInstance().signOut();
                                                        break;
                                                    }
                                                    case constants.VER_CUR_USER_NEW_PHONE: {
                                                        break;
                                                    }
                                                    case constants.VER_NEW_USER: {
                                                        FirebaseAuth.getInstance().getCurrentUser().delete();
                                                        break;
                                                    }
                                                }
                                                Log.d(constants.TAG, e.getMessage());
                                                userViewModel.getMsg().postValue(e.getMessage());
                                            }

                                            @Override
                                            public void onCodeSent(@NonNull @NotNull String id, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                                super.onCodeSent(id, forceResendingToken);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("id", id);
                                                ((MainActivity) requireActivity()).navController.navigate(R.id.action_loginFragment_to_enterPinFragment2, bundle);
                                            }
                                        });
                            }
                        }
                    });
        }
    }
}