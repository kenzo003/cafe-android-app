package com.example.cafe.screens.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.CategoryFragmentBinding;
import com.example.cafe.models.Category;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private CatalogViewModel mViewModel;
    private CategoryFragmentBinding mBinding;
    private RecyclerView mRecyclerView;
    private CategoryAdapter categoryAdapter;
    private Observer<List<Category>> observerCategory;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity) getActivity()).navController.navigate(R.id.auth_nav);
        }
        mBinding = CategoryFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        mViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        categoryAdapter = new CategoryAdapter(new LinkedList<>(), getContext());
        mRecyclerView = mBinding.cfRecVCategory;
        mRecyclerView.setAdapter(categoryAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        categoryAdapter.setOnCategoryClickListener(
                new CategoryAdapter.OnItemClickListener() {
                    @Override
                    public void onNewsClick(View view, int position) {
                        String id = categoryAdapter.getCategory(position).id;
                        Bundle bundle = new Bundle();
                        bundle.putString(constants.CATEGORY_ID, id);
                        constants.CURRENT_CATEGORY_ID = id;
                        ((MainActivity) getActivity()).navController.navigate(R.id.action_categoryFragment_to_catalogFragment2, bundle);
                    }
                }
        );

        observerCategory = new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                categoryAdapter.setCategories(categories);
            }
        };

//        mViewModel.getAllNews().observe(this, observerNews);
        mViewModel.getAllCategory().observe(this, observerCategory);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}