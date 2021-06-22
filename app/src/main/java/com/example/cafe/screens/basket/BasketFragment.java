package com.example.cafe.screens.basket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
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
import com.example.cafe.databinding.BasketFragmentBinding;
import com.example.cafe.models.Basket;
import com.example.cafe.models.BasketProduct;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.LinkedList;
import java.util.List;

public class BasketFragment extends Fragment {
    private BasketViewModel mViewModel;
    private BasketFragmentBinding mBinding;
    private BasketAdapter basketAdapter;
    private Observer<List<BasketProduct>> observerBasket;
    private MainActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        mBinding = BasketFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mViewModel = new ViewModelProvider(this).get(BasketViewModel.class);
        basketAdapter = new BasketAdapter(new LinkedList<>(), getContext(), mViewModel);
        RecyclerView mRecyclerView = mBinding.bfRecycler;
        mRecyclerView.setAdapter(basketAdapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        hideDownPanel();

        mBinding.bfBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.navController.navigate(R.id.action_basketFragment2_to_orderFragment);
            }
        });

        basketAdapter.setOnItemMinusClickListener(
                (view, position) -> {
                    try {
                        BasketProduct basketProduct = basketAdapter.getProduct(position);
                        if (basketProduct != null) {
                            Product product = basketProduct.getProduct();
                            Basket basket = basketProduct.getBasket();
                            int count = Integer.parseInt(basket.basket_product_count) - 1;
                            if (count > 0) {
                                mViewModel.updateProductBasket(product, basket, String.valueOf(count));
                            }
                        }
                    } catch (Exception e) {
                        Log.d(constants.TAG, e.getMessage());
                    }

                }
        );

        basketAdapter.setOnBasketClickListener(
                (view, position) -> {
                    try {
                        BasketProduct basketProduct = basketAdapter.getProduct(position);
                        if (basketProduct != null) {
                            Basket basket = basketProduct.getBasket();
                            mViewModel.deleteProductBasket(basket, unused -> {
                                if (basketAdapter.getItemCount() == 0)
                                    hideDownPanel();
                            });
                        }
                    } catch (Exception exception) {
                        Log.d(constants.TAG, exception.getMessage());
                    }
                }
        );

        basketAdapter.setOnItemPlusClickListener(
                (view, position) -> {
                    try {
                        BasketProduct basketProduct = basketAdapter.getProduct(position);
                        if (basketProduct != null) {
                            Product product = basketProduct.getProduct();
                            Basket basket = basketProduct.getBasket();
                            int count = Integer.parseInt(basket.basket_product_count) + 1;
                            if (count <= Integer.parseInt(product.product_quantity))
                                mViewModel.updateProductBasket(product, basket, String.valueOf(count));
                        }
                    } catch (Exception e) {
                        Log.d(constants.TAG, e.getMessage());
                    }
                }
        );
        basketAdapter.setOnItemClickListener(
                (view, position) -> {
                    try {
                        Product product = basketAdapter.getProduct(position).getProduct();
                        if (product != null) {
                            //Передаем модель и переходим к карточке новости
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(constants.NODE_PRODUCT, product);
                            activity.navController.navigate(R.id.action_basketFragment2_to_productFragment, bundle);
                        }
                    } catch (Exception e) {
                        Log.d(constants.TAG, e.getMessage()); //Если вдруг что-то пошло не так, но должно работать как часы
                    }
                }
        );

        basketAdapter.setOnFavoriteClickListener(
                new BasketAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Product product = basketAdapter.getProduct(position).getProduct();
                        if (product != null) {
                            mViewModel.insertProductFavorite(product,
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            basketAdapter.notifyItemChanged(position);
                                        }
                                    },
                                    new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            basketAdapter.notifyItemChanged(position);
                                        }
                                    });
                        }
                    }
                }
        );

        observerBasket = basketProduct -> {
            basketAdapter.setBasket(basketProduct);
            if (basketProduct.size() != 0){
                mBinding.bfTxtVPrice.setText(basketAdapter.getPrice() + " Р");
                showDownPanel();
            }else {
                hideDownPanel();
            }
        };
        mViewModel.getAllProduct().observe(this, observerBasket);

    }

    private void hideDownPanel() {
        mBinding.bfDownPanel.setVisibility(View.GONE);
        mBinding.bpTxtVBasketEmpty.setVisibility(View.VISIBLE);
    }

    private void showDownPanel() {
        mBinding.bfDownPanel.setVisibility(View.VISIBLE);
        mBinding.bpTxtVBasketEmpty.setVisibility(View.GONE);
    }


}