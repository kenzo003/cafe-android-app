package com.example.cafe.screens.catalog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.CatalogFragmentBinding;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class CatalogFragment extends Fragment {
    private CatalogViewModel mViewModel;
    private CatalogFragmentBinding mBinding;
    private CatalogAdapter catalogAdapter;
    private Observer<List<Product>> observerCatalog;
    private String id; //id текущего каталога
    private MainActivity activity;

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        id = "";
        mViewModel = new ViewModelProvider(this).get(CatalogViewModel.class); //TODO; проверить
        catalogAdapter = new CatalogAdapter(new LinkedList<>(), getContext());
        RecyclerView mRecyclerView = mBinding.cfRecVCatalog;
        mRecyclerView.setAdapter(catalogAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

//        mViewModel.insertProduct(new Product("", "Виски", "гр", "40 летний", "1212", "1200", "23", "VISIBLE", "dfsfsdfdsfsdfds", "" ));
        catalogAdapter.setOnCatalogClickListener(
                (view, position) -> {
                    try {
                        Product product = catalogAdapter.getProduct(position);
                        if (product != null) {
                            //Передаем модель и переходим к карточке новости
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(constants.NODE_PRODUCT, product);
                            activity.navController.navigate(R.id.action_catalogFragment2_to_productFragment, bundle);
                        }
                    } catch (Exception e) {
                        Log.d(constants.TAG, e.getMessage()); //Если вдруг что-то пошло не так, но должно работать как часы
                    }
                }
        );

        catalogAdapter.setOnBasketClickListener(
                (view, position) -> {

                    Product product = catalogAdapter.getProduct(position);
                    if (product != null) {
                        //Передаем модель и переходим к карточке новости
                        mViewModel.insertProductBasket(product.product_id, "1", product.product_quantity,
                                unused -> Snackbar.make(requireContext(), mBinding.getRoot(), "Товар добавлен в корзину", Snackbar.LENGTH_LONG).show());
                    }
                }
        );

        assert getArguments() != null;
        id = Objects.requireNonNullElse(getArguments().getString(constants.CATEGORY_ID), "");

        observerCatalog = products -> catalogAdapter.setCatalog(products);
        mViewModel.getAllProduct(id).observe(this, observerCatalog);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity)getActivity();
        mBinding = CatalogFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }
}