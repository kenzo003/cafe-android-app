package com.example.cafe.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.models.News;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.Objects;


//TODO: Нужно проверить обнуление списка перед изменением элементов
public class NewsLiveData extends MutableLiveData<List<News>> {
    private final LinkedList<News> news; //Список, в который пушим данные с сервака
    private final FirebaseUser mAuth; //Текущий пользователь
    private final DatabaseReference mReference; //Ссылка на корень БД
    private final Query mNewsUser;     //Получаем все id новостей для текущего пользователя;
    private final String UID; //UID текущего пользователя

    public NewsLiveData() {
        news = new LinkedList<>();
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

        UID = (mAuth != null) ? mAuth.getUid() : "";
        mNewsUser = mReference.child(constants.NODE_NEWS_USERS)
                .orderByChild(constants.USER_ID)
                .equalTo(UID);
    }

    // callback для узла НовостиПользователи
    private final ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
            news.clear();
            try {
                //Если данные не пусты и не равны null
                if (snapshot.exists()) {
                    //Перебираем все записи таблицы и получаем только для текущего пользователя
                    for (DataSnapshot news_user : snapshot.getChildren()) {
                        //Если запись не пуста и значение для модели News существует
                        if (news_user.exists() && news_user.getValue() != null) {
                            String newsId = Objects.requireNonNullElse(Objects.requireNonNull(news_user.getValue(News.class)).news_id, "");
                            //Устанавливаем callback для этой новости
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

    // callback для узла Новости
    private final ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            //Если данные не пусты и данные не равны null
            if (snapshot.exists() && snapshot.getValue() != null) {
                news.add(snapshot.getValue(News.class));
                postValue(news);
            }
        }

        @Override
        public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            if (snapshot.exists() && snapshot.getValue() != null) {
                int index = indexOf(news, snapshot.getKey());
                //Контролируем выход за пределы границ списка Новостей
                if (index >= 0 && news.size() > index)
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

    @Override
    protected void onActive() {
        super.onActive();
        mNewsUser.addValueEventListener(valueEventListener); //Установка callback для узла НовостиПользователи
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mReference.child(constants.NODE_NEWS).removeEventListener(childEventListener); //Удаление callback для узла Новости
        mNewsUser.removeEventListener(valueEventListener); //Удаление callback для узла НовостиПользователи
    }


    //Utilities

    //получение индекса элемента по news_id
    private static int indexOf(List<News> list, String id) {
        int index = -1;
        for (News value : list) {
            index++;
            if (value.news_id.equals(id))
                break;
        }
        return index;
    }

}
