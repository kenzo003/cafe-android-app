package com.example.cafe.screens.basket;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.cafe.database.firebase.AppRepository;
import com.example.cafe.models.Basket;
import com.example.cafe.models.BasketProduct;
import com.example.cafe.models.Product;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class BasketViewModel extends ViewModel {
    private AppRepository repository;
    private MutableLiveData<List<BasketProduct>> allProduct;


    public BasketViewModel() {
        repository = new AppRepository();
        allProduct = repository.getAllProductBasket();
    }

    public MutableLiveData<List<BasketProduct>> getAllProduct() {
        return allProduct;
    }
    public void deleteProductBasket(Basket basket, OnSuccessListener<? super Void> onSuccess){
        repository.deleteProductBasket(basket, onSuccess);
    }

    public void updateProductBasket(Product product, Basket basket, String countProduct) {
        repository.updateProductBasket(product, basket, countProduct);
    }
}