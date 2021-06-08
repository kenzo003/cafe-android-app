package com.example.cafe.database.firebase;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.News;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AllNewsLiveData extends MutableLiveData<List<News>> {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mInstance;

    public AllNewsLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mInstance = FirebaseDatabase.getInstance();
    }

    final Object listener = new ValueEventListener() {
        LinkedList news = new LinkedList();

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            news.clear();
            String UID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

            for (Map.Entry<String, String> item : ((HashMap<String, String>) snapshot.child(constants.NODE_NEWS_USERS).getValue(Object.class)).entrySet()) {
                if (((HashMap) ((Object) item.getValue())).get(constants.USER_ID).equals(UID)) {
                    String id = ((HashMap) ((Object) item.getValue())).get(constants.NEWS_ID).toString();

                    for (Map.Entry<String, String> item_news : ((HashMap<String, String>) snapshot.child(constants.NODE_NEWS).getValue(Object.class)).entrySet()) {
                        HashMap<String, String> data = ((HashMap) ((Object) item_news.getValue()));
                        if (data != null || !data.isEmpty())
                            news.add(new News(data.get(constants.NEWS_HEADER).toString(), data.get(constants.NEWS_DATA).toString(), data.get(constants.NEWS_MEDIA).toString(), data.get(constants.NEWS_DESC).toString()));
                    }
                }
            }
            if (!news.isEmpty())
                postValue(news);
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {

        }

    };

    @Override
    protected void onActive() {
        super.onActive();
        mReference.addValueEventListener((ValueEventListener) listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mReference.removeEventListener((ValueEventListener) listener);
    }
}
