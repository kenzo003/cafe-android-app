package com.example.cafe.screens.catalog;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.FragmentProductBinding;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class ProductFragment extends Fragment {
    private FragmentProductBinding mBinding;
    private Product product;
    private CatalogViewModel mViewModel;
    private MainActivity activity;
    private int productCount;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        mBinding = FragmentProductBinding.inflate(inflater);
        init();
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressLint({"CheckResult", "SetTextI18n"})
    private void init() {
        productCount = 0;
        mViewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        try {
            assert getArguments() != null;
            product = (Product) getArguments().getSerializable(constants.NODE_PRODUCT);
            if (product != null) {
                mBinding.fpTxtVProductName.setText(product.product_name);
                mBinding.fpTxtVProductDesc.setText(product.product_description);
                mBinding.fpTxtVProductPrice.setText(product.product_price + " Р");
                mBinding.fpTxtVProductCount.setText(String.valueOf(productCount));

                mViewModel.isProductFavorite(product,
                        new ValueEventListener() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                    mBinding.ciBtnProductFavoriteAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                                else
                                    mBinding.ciBtnProductFavoriteAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                            }

                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                mBinding.ciBtnProductFavoriteAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                            }
                        });

                mBinding.ciBtnProductFavoriteAdd.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mViewModel.insertProductFavorite(product,
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                mBinding.ciBtnProductFavoriteAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black));
                                            }
                                        },
                                        new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                mBinding.ciBtnProductFavoriteAdd.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite));
                                            }
                                        });
                            }
                        }
                );

                mBinding.fpBtnPlus.setOnClickListener(v -> {
                    if (productCount < Integer.parseInt(product.product_quantity)) {
                        productCount++;
                        mBinding.fpTxtVProductCount.setText(String.valueOf(productCount));
                    }
                });

                mBinding.fpBtnMinus.setOnClickListener(v -> {
                    if (productCount > 0)
                        productCount--;
                    mBinding.fpTxtVProductCount.setText(String.valueOf(productCount));
                });

                mBinding.fpBtnFab.setOnClickListener(v -> activity.navController.navigateUp());

                //Добавлеие товара в корзину
                mBinding.btnBasketAdd.setOnClickListener(v -> {
                    if (!product.product_id.isEmpty() && productCount > 0) { //TODO: Добавить проверку на количество товара
                        mViewModel.insertProductBasket(product.product_id, String.valueOf(productCount), product.product_quantity,
                                unused -> {
                                    Toast t = Toast.makeText(getContext(), "Товар добавлен в корзину", Toast.LENGTH_SHORT);
                                    t.setGravity(Gravity.TOP, 10, 0);
                                    t.show();
                                    productCount = 0;
                                    mBinding.fpTxtVProductCount.setText(String.valueOf(productCount));
                                });
                    } else {
                        Toast t = Toast.makeText(getContext(), "Количество товара не может быть равно 0", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.TOP, 10, 0);
                        t.show();
                    }
                });
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

                //TODO: Нужно учитывать товар в избранном и сделать отметку на значке

                Glide.with(this).asBitmap()
                        .load(product.product_logo)
                        .apply(requestOptions)
                        .addListener(
                                new RequestListener<Bitmap>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                        resource.getPixel(0, 0);
                                        mBinding.fpImgVProductLogo.setBackgroundColor(resource.getPixel(0, 0));
                                        return false;
                                    }
                                }
                        )
                        .into(mBinding.fpImgVProductLogo);
            }
        } catch (Exception exception) {
            Log.d(constants.TAG, exception.getMessage());
        }
    }

}