package com.example.cafe.screens.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.databinding.CatalogFragmentBinding;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;

import java.util.LinkedList;
import java.util.List;


public class CatalogFragment extends Fragment {
    private CatalogViewModel mViewModel;
    private CatalogFragmentBinding mBinding;
    private RecyclerView mRecyclerView;
    private CatalogAdapter catalogAdapter;
    private Observer<List<Product>> observerCatalog;

    public CatalogFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        mViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        catalogAdapter = new CatalogAdapter(new LinkedList<>(), getContext());
        mRecyclerView = mBinding.cfRecVCatalog;
        mRecyclerView.setAdapter(catalogAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mViewModel.insertProduct(new Product("", "Пиво","","","", "", "", "dfsfsdfdsfsdfds"));

        catalogAdapter.setOnCatalogClickListener(
                new CatalogAdapter.OnItemClickListener() {

                    @Override
                    public void onNewsClick(View view, int position) {

                    }
                }
        );

//        catalogAdapter.setOnCatalogClickListener(
//                new CategoryAdapter.OnItemClickListener() {
//                    @Override
//                    public void onNewsClick(View view, int position) {
//                        ((MainActivity) requireActivity()).navController.navigate(R.id.action_categoryFragment_to_catalogFragment2);
//                        String id = catalogAdapter.getProduct(position).id;
//                        Toast.makeText(getContext(), id, Toast.LENGTH_LONG).show();
//                    }
//                }
//        );

        observerCatalog = new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                catalogAdapter.setCatalog(products);
            }
        };

//        mViewModel.getAllNews().observe(this, observerNews);
        String id = getArguments().getString(constants.CATEGORY_ID).toString();
        mViewModel.getAllProduct(id).observe(this, observerCatalog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = CatalogFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }
}