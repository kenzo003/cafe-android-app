package com.example.cafe.screens.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.AccountFragmentBinding;
import com.example.cafe.screens.auth.UserViewModel;

public class AccountFragment extends Fragment {

    private UserViewModel mViewModel;
    private AccountFragmentBinding mBinding;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = AccountFragmentBinding.inflate(inflater);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mViewModel = ((MainActivity) getActivity()).getUserViewModel();
        mBinding.afBtnSignout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.signOut();
                        ((MainActivity)requireActivity()).navController.navigate(R.id.auth_nav);
                    }
                }
        );

    }
}