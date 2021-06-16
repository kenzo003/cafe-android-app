package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Favorite;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class FavoriteLiveData extends MutableLiveData<List<Product>> {
    private final DatabaseReference mReference;
    private final FirebaseUser mAuth; //Текущий пользователь
    private Query mProduct;     //Получаем все продукты для текущего каталога;
    private final LinkedList<Product> products; //Список, в который пушим данные с сервака
    private final String UID; //UID текущего пользователя

    public FavoriteLiveData() {
        this.products = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        this.mReference = FirebaseDatabase.getInstance().getReference();
        UID = (mAuth != null) ? mAuth.getUid() : "";
        mProduct = mReference.child(constants.NODE_FAVORITE)
                .orderByChild(constants.FAVORITE_USER).equalTo(UID);
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            products.clear();
            try {
                if (snapshot.exists()) {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        if (item.exists() && item.getValue() != null) {
                            String idProduct = item.getValue(Favorite.class).favorite_product;
                            mReference.child(constants.NODE_PRODUCT).child(idProduct).addValueEventListener(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snap) {
                                            if (snap.exists()) {
                                                Product product = snap.getValue(Product.class);
                                                products.add(product);
                                                postValue(products);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                            Log.d(constants.TAG, error.getMessage());
                                        }
                                    }
                            );
                        }
                    }
                } else {
                    products.clear();
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
    protected void onInactive() {
        super.onInactive();
        mProduct.removeEventListener(valueEventListener);
    }

    @Override
    protected void onActive() {
        super.onActive();
        mProduct.addValueEventListener(valueEventListener);
    }
}
