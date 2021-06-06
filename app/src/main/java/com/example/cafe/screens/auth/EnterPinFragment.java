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
import androidx.navigation.NavGraph;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.EnterPinFragmentBinding;
import com.example.cafe.screens.auth.passcodeview.PasscodeView;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;


public class EnterPinFragment extends Fragment {

    private EnterPinFragmentBinding mBinding;
    private EnterPinViewModel mViewModel;
    private PasscodeView passcodeView;

    public static EnterPinFragment newInstance() {
        return new EnterPinFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = EnterPinFragmentBinding.inflate(inflater);
        passcodeView = mBinding.passcodeView;
        setPasscodeView(passcodeView);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EnterPinViewModel.class);
    }

    private void enterCode(String code) {
        //Создаем credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(getArguments().getString("id").toString(), code);

        if (constants.AUTH.getCurrentUser().getPhoneNumber() == null) {

            //Пытаемся авторизоваться под номером телефона
            constants.AUTH.getCurrentUser().updatePhoneNumber(credential).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            //Пользователь ввел корректный номер телефона
                            if (task.isSuccessful()) {
                                constants.AUTH.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                        //Пользователь успешно зарегестрировался
                                        if (task.isSuccessful()) {
                                            MainActivity activity = (MainActivity) getActivity();
                                            activity.navController.popBackStack();
                                            NavGraph navGraph = activity.navController.getNavInflater().inflate(R.navigation.navigation_graph);
                                            navGraph.setStartDestination(R.id.newsFragment);
                                            activity.navController.setGraph(navGraph);
                                            activity.showToolbarAndNavBar();
                                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), "Здравствуйте, " + constants.AUTH.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            Log.d(constants.TAG, task.getException().getMessage());
                                        }
                                    }
                                });
                                //C номером телефона что-то не то
                            } else {
                                if (constants.AUTH.getCurrentUser().getPhoneNumber().isEmpty()) {
                                    constants.AUTH.getCurrentUser().delete(); //Удаляем промежуточного пользователя
                                }
                                //Возвращаемся к окну регистрации
                                ((MainActivity) getActivity()).navController.popBackStack(R.id.loginFragment3, false);
                                Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                Log.d(constants.TAG, task.getException().getMessage());
                            }
                        }
                    }
            );
        } else if (!constants.AUTH.getCurrentUser().getPhoneNumber().isEmpty()){
            constants.AUTH.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    //Пользователь успешно зарегестрировался
                    if (task.isSuccessful()) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.navController.popBackStack();
                        NavGraph navGraph = activity.navController.getNavInflater().inflate(R.navigation.navigation_graph);
                        navGraph.setStartDestination(R.id.newsFragment);
                        activity.navController.setGraph(navGraph);
                        activity.showToolbarAndNavBar();
                        Toast.makeText(getContext(), "Здравствуйте, " + constants.AUTH.getCurrentUser().getEmail().toString(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(constants.APP_ACTIVITY.getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.d(constants.TAG, task.getException().getMessage());
                    }
                }
            });
        }


    }

    public void setPasscodeView(PasscodeView passcodeView) {
        passcodeView.setPasscodeLength(6)
                .setLocalPasscode("111111")
                .setLocaleCredential(PhoneAuthProvider.getCredential(getArguments().getString("id").toString(), "123"))
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail(String wrongNumber) {
                    }

                    @Override
                    public void onSuccess(String number) {
                        enterCode(passcodeView.getPasscode());
                    }
                });
    }


}