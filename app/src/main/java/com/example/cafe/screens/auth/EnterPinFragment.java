package com.example.cafe.screens.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.EnterPinFragmentBinding;
import com.example.cafe.screens.auth.passcodeview.PasscodeView;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;


public class EnterPinFragment extends Fragment {

    private EnterPinFragmentBinding mBinding;
    private EnterPinViewModel mViewModel;
    private PasscodeView passcodeView;
    private UserViewModel userViewModel;

    public static EnterPinFragment newInstance() {
        return new EnterPinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = EnterPinFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
//        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel = ((MainActivity) getActivity()).getUserViewModel();
        passcodeView = mBinding.passcodeView;
        setPasscodeView(passcodeView);
    }


    public void setPasscodeView(PasscodeView passcodeView) {
        passcodeView.setPasscodeLength(6)
                .setLocalPasscode("111111")
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail(String wrongNumber) {
                    }

                    @Override
                    public void onSuccess(String number) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() == null) {
                            userViewModel.updatePhoneNumber(getArguments().getString("id"), number, constants.VER_CUR_USER_NEW_PHONE,
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                userViewModel.signInWithCredential(getArguments().getString("id"), number);
                                            }
                                        }
                                    });
                        } else {
                            userViewModel.signInWithCredential(getArguments().getString("id"), number);
                        }

                    }
                });
    }


}