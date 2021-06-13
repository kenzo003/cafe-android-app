package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.News;
import com.example.cafe.models.NewsUser;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class AllNewsLiveData extends MutableLiveData<List<News>> {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mInstance;
    private Query mNewsUser;
    private LinkedList<News> news;
    private HashMap<DatabaseReference, ChildEventListener> listenerHashMap;


    public AllNewsLiveData() {
        news = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mNewsUser = mReference.child(constants.NODE_NEWS_USERS)
                .orderByChild(constants.USER_ID)
                .equalTo(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        mInstance = FirebaseDatabase.getInstance();
    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            news.clear();
            try {
                if (snapshot.exists()) {
                    for (DataSnapshot news_user : snapshot.getChildren()) {
                        if (news_user.exists() && news_user.getValue() != null) {
                            String newsId = Objects.requireNonNullElse(Objects.requireNonNull(news_user.getValue(NewsUser.class)).news_id, "");
                            mReference.child(constants.NODE_NEWS).orderByChild(constants.NEWS_ID).equalTo(newsId).addChildEventListener(
                                    childEventListener
                            );
                        }
                    }
                }
            } catch (Exception exception) {
                Log.d(constants.TAG, exception.getMessage());
            }
        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.d(constants.TAG, error.getMessage());
        }
    };

    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            if (snapshot.exists() && snapshot.getValue() != null) {
                news.add(snapshot.getValue(News.class));
                postValue(news);
            }
        }

        @Override
        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            if (snapshot.exists() && snapshot.getValue() != null) {
                int index = indexOf(news, snapshot.getKey());
                news.set(index, snapshot.getValue(News.class));
                postValue(news);
            }
        }

        @Override
        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
            if (snapshot.exists() && snapshot.getValue() != null) {
                news.remove(snapshot.getValue(News.class));
                postValue(news);
            }
        }

        @Override
        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.d(constants.TAG, error.getMessage());
        }
    };

    //получение индекса элемента по news_id
    private int indexOf(List<News> list, String id) {
        int index = -1;
        for (News value : list) {
            index++;
            if (value.news_id.equals(id))
                break;
        }
        return index;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mNewsUser.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onInactive() {
        news.clear();
        super.onInactive();
        mReference.child(constants.NODE_NEWS).removeEventListener(childEventListener);
        mNewsUser.removeEventListener(valueEventListener);
    }

}
