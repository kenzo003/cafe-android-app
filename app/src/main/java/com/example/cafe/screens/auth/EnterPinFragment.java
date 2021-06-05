package com.example.cafe.screens.auth;

import android.os.Bundle;
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
        // TODO: Use the ViewModel
    }

    public void setPasscodeView(PasscodeView passcodeView) {
        passcodeView.setPasscodeLength(5)
                .setLocalPasscode("12345")
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail(String wrongNumber) {
                        Toast.makeText(getActivity().getApplicationContext(), "Nooooo", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onSuccess(String number) {
                        MainActivity activity = (MainActivity) getActivity();
                        activity.navController.popBackStack();
                        NavGraph navGraph = activity.navController.getNavInflater().inflate(R.navigation.navigation_graph);
                        navGraph.setStartDestination(R.id.newsFragment);
                        activity.navController.setGraph(navGraph);

                        activity.showToolbarAndNavBar();
                    }
                });
    }
}