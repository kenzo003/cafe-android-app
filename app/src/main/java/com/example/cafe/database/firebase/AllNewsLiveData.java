package com.example.cafe.database.firebase;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

import java.util.LinkedList;
import java.util.List;

public class AllNewsLiveData extends MutableLiveData<List<News>> {
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mInstance;
    private Query mNewsUser;


    public AllNewsLiveData() {
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
        mNewsUser = mReference.child(constants.NODE_NEWS_USERS)
                .orderByChild(constants.USER_ID).equalTo(mAuth.getCurrentUser().getUid());
        mInstance = FirebaseDatabase.getInstance();
    }


    ChildEventListener childEventListener = new ChildEventListener() {
        LinkedList<News> news = new LinkedList<>();
        int finalCount_news = 0;


        @Override
        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            try {
                if (snapshot.exists()) {
                    String newsId = snapshot.getValue(NewsUser.class).news_id;
                    finalCount_news += 1;

                    mReference.child(constants.NODE_NEWS).child(newsId).addValueEventListener(
                            new ValueEventListener() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if (snapshot != null && snapshot.getValue(News.class) != null) {
                                        if (news.size() == finalCount_news)
                                            for (int i = 0; i < news.size(); i++) {
                                                if (news.get(i).news_id.equals(snapshot.getKey())) {
                                                    news.set(i, snapshot.getValue(News.class));
                                                    break;
                                                }
                                            }
                                        else {
                                            news.add(snapshot.getValue(News.class));
                                        }
                                        postValue(news);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                    Log.d(constants.TAG, error.getMessage());
                                }
                            }
                    );
                }
            } catch (Exception exception) {
                Log.d(constants.TAG, exception.getMessage());
            }
        }

        @Override
        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
        }

        @Override
        public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
        }

        @Override
        public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull @NotNull DatabaseError error) {
            Log.d(constants.TAG, error.getMessage());
        }

    };


    @Override
    protected void onActive() {
        super.onActive();
        mNewsUser.addChildEventListener(childEventListener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mNewsUser.removeEventListener(childEventListener);
    }
}
