package com.example.cafe.screens.account;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.FragmentFavoriteBinding;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.LinkedList;
import java.util.List;

public class FavoriteFragment extends Fragment {
    private AccountViewModel mViewModel;
    private FragmentFavoriteBinding mBinding;
    private FavoriteAdapter mAdapter;
    private Observer<List<Product>> observerFavorite;
    private String id; //id текущего каталога
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        mBinding = FragmentFavoriteBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    private void init() {
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class); //TODO; проверить
        mAdapter = new FavoriteAdapter(new LinkedList<>(), getContext());
        RecyclerView mRecyclerView = mBinding.ffRecVCatalog;
        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnBasketClickListener(
                new FavoriteAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = mAdapter.getProduct(position);
                        if (product != null) {
                            //Передаем модель и переходим к карточке новости
                            mViewModel.insertProductBasket(product.product_id, "1", product.product_quantity,
                                    unused -> {
                                        Toast t = Toast.makeText(getContext(), "Товар добавлен в корзину", Toast.LENGTH_SHORT);
                                        t.setGravity(Gravity.TOP, 10, 0);
                                        t.show();
                                    });
                        }
                    }
                }
        );

        mAdapter.setOnFavoriteClickListener(
                new FavoriteAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = mAdapter.getProduct(position);
                        if (product != null) {
                            mViewModel.insertProductFavorite(product,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            System.out.println("ss");
                                        }
                                    }, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    }
                }
        );

        mAdapter.setOnItemClickListener(
                new FavoriteAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            Product product = mAdapter.getProduct(position);
                            if (product != null) {
                                //Передаем модель и переходим к карточке новости
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(constants.NODE_PRODUCT, product);
                                activity.navController.navigate(R.id.action_favoriteFragment_to_productFragment2, bundle);
                            }
                        } catch (Exception e) {
                            Log.d(constants.TAG, e.getMessage()); //Если вдруг что-то пошло не так, но должно работать как часы
                        }

                    }
                }
        );
        observerFavorite = products -> mAdapter.setFavorite(products);
        mViewModel.getAllProductFavorite().observe(this, observerFavorite);
    }
}