package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AllProductLiveData extends MutableLiveData<List<Product>> {
    private final DatabaseReference mReference;
    private Query mProduct;     //Получаем все продукты для текущего каталога;
    private final LinkedList<Product> products; //Список, в который пушим данные с сервака

    private String categoryId;


    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        setProductRef();
    }

    private void setProductRef() {
        mProduct = mReference.child(constants.NODE_PRODUCT)
                .orderByChild(constants.PRODUCT_CATEGORY).equalTo(categoryId);
    }

    public AllProductLiveData() {
        categoryId = "";
        products = new LinkedList<>();
        mReference = FirebaseDatabase.getInstance().getReference();
        mProduct = mReference.child(constants.NODE_PRODUCT)
                .orderByChild(constants.PRODUCT_CATEGORY).equalTo(categoryId);
    }


    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            products.clear();
            try {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        if (item.exists() && item.getValue() != null) {
                            if (Objects.requireNonNull(item.getValue(Product.class)).product_visibility.equals(constants.VISIBLE))
                                products.add(item.getValue(Product.class));
                        }
                    }
                    postValue(products);
                }

            } catch (Exception e) {
                Log.d(constants.TAG, e.getMessage());
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.d(constants.TAG, error.getMessage());
        }
    };

    @Override
    protected void onActive() {
        super.onActive();
        mProduct.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mProduct.removeEventListener(valueEventListener);
    }
}
