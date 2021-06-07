package com.example.cafe.screens.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

//TODO Может быть стоит использовать Общий шаблон для создания Fragment (5)
public class NewsFragment extends Fragment {

    private NewsViewModel mViewModel;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity) getActivity()).navController.navigate(R.id.auth_nav);
        }
        return inflater.inflate(R.layout.news_fragment, container, false);
    }


}