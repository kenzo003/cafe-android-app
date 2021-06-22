package com.example.cafe.screens.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cafe.databinding.FragmentAddressBinding;


public class AddressFragment extends Fragment {
    private FragmentAddressBinding mBinding;
    private AccountViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddressBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init(){
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

    }



}