package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.Category;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class CategoryLiveData extends MutableLiveData<List<Category>> {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mInstance;
    private LinkedList<Category> category;
    private final Query mCategory;     //Получаем все id новостей для текущего пользователя;


    public CategoryLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mInstance = FirebaseDatabase.getInstance();
        mCategory = mReference.child(constants.NODE_CATEGORY);

        category = new LinkedList<>();
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            category.clear();
            try {

                for (DataSnapshot item : snapshot.getChildren()) {
                    if (item.exists() && item.getValue() != null) {
                        category.add(item.getValue(Category.class));
                    }
                }
                postValue(category);
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
        mCategory.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mCategory.removeEventListener(valueEventListener);
    }
}
