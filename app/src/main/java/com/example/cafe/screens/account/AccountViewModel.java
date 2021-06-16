package com.example.cafe.screens.account;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.database.firebase.AppRepository;
import com.example.cafe.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MutableLiveData<List<Product>> productFavorite;

    public AccountViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new AppRepository();
        productFavorite = repository.getAllProductFavorite();
    }

    public MutableLiveData<List<Product>> getAllProductFavorite() {
        return productFavorite;
    }

    public void insertProductBasket(String productId, String productCount, String productMaxCount, OnSuccessListener<? super Void> onSuccess){
        repository.insertProductBasket(productId, productCount,productMaxCount, onSuccess);
    }

    public void insertProductFavorite(Product product, OnSuccessListener<? super Void> onSuccess, OnSuccessListener<? super Void> onDelete) {
        repository.updateProductFavorite(product, onSuccess, onDelete);
    }
}