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
import androidx.recyclerview.widget.GridLayoutManager;
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
    private CategoryAdapter categoryAdapter;
    private Observer<List<Category>> observerCategory;
    private MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            activity.navController.navigate(R.id.auth_nav);
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
        mViewModel = new ViewModelProvider(this).get(CatalogViewModel.class); //TODO: Здесь проверить
        categoryAdapter = new CategoryAdapter(new LinkedList<>(), getContext());
        RecyclerView mRecyclerView = mBinding.cfRecVCategory;
        mRecyclerView.setAdapter(categoryAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);


        categoryAdapter.setOnCategoryClickListener(
                (view, position) -> {
                    if (categoryAdapter.getCategory(position) != null){
                        String id = categoryAdapter.getCategory(position).category_id;
                        String name = categoryAdapter.getCategory(position).category_name;
                        Bundle bundle = new Bundle();
                        bundle.putString(constants.CATEGORY_ID, id);
                        bundle.putString(constants.CATEGORY_NAME, name);
                        activity.navController.navigate(R.id.action_categoryFragment_to_catalogFragment2, bundle); //Переходим в раздел каталога с данной категорией
                    }
                }
        );

        observerCategory = categories -> categoryAdapter.setCategories(categories);
        mViewModel.getAllCategory().observe(this, observerCategory);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}