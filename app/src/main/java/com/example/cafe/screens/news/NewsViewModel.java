package com.example.cafe.screens.news;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.cafe.database.firebase.AppRepository;
import com.example.cafe.models.News;
import com.google.android.gms.tasks.OnCompleteListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MutableLiveData<List<News>> allNews;

    public NewsViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new AppRepository();
        allNews = repository.getAllNews();
    }

    public MutableLiveData<List<News>> getAllNews() {
        return allNews;
    }

    public void deleteNews(News news, OnCompleteListener<Void> onCompleteListener){
        repository.deleteNews(news, onCompleteListener);
    }


}