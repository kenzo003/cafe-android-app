package com.example.cafe.database.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class AllProductLiveData extends MutableLiveData<List<Product>> {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mInstance;
    private String CategoryId;


    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public AllProductLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mInstance = FirebaseDatabase.getInstance();
        CategoryId = "";
    }

    final Object listener = new ValueEventListener() {
        LinkedList product = new LinkedList();

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

            product.clear();
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
            try {
                snapshot.getChildren().forEach(item ->
                        product.add(item.getValue(Product.class)));

                if (!product.isEmpty() && product != null){
                    postValue(product);
                }

            } catch (Exception e) {
                Log.d(constants.TAG, e.getMessage());
            }

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }

    };

    @Override
    protected void onActive() {
        super.onActive();
        mReference.child(constants.NODE_PRODUCT).orderByChild(constants.CATEGORY_ID).equalTo(CategoryId).addValueEventListener((ValueEventListener) listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mReference.removeEventListener((ValueEventListener) listener);
    }
}
