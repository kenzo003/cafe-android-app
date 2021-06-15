package com.example.cafe.screens.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.R;
import com.example.cafe.activities.MainActivity;
import com.example.cafe.databinding.NewsFragmentBinding;
import com.example.cafe.models.News;
import com.example.cafe.utilits.constants;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

//TODO Может быть стоит использовать Общий шаблон для создания Fragment (5)
public class NewsFragment extends Fragment {

    private NewsViewModel mViewModel;
    private NewsFragmentBinding mBinding;
    private NewsAdapter adapter;
    private Observer<List<News>> observerNews; //Реагирует когда данные в БД изменились
    private MainActivity activity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        activity = (MainActivity) getActivity();
        //Проверяем авторизован ли пользователь
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //Если нет то перемещаем на экран авторизации TODO:Сомнения по поводу backstack, возможно перейдет в главное меню, если нажать назад
            activity.navController.navigate(R.id.auth_nav);
        }

        mBinding = NewsFragmentBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        init();
    }


    private void init() {
        mViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        adapter = new NewsAdapter(new LinkedList<>(), getContext());
        RecyclerView mRecyclerView = mBinding.nfRecView;
        mRecyclerView.setAdapter(adapter);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        //Событие клика по карточке Новости
        adapter.setOnNewsClickListener((view, position) -> {
            try {
                News news = adapter.getNews(position);
                if (news != null) {
                    //Передаем модель и переходим к карточке новости
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(constants.NODE_NEWS, news);

                    activity.navController.navigate(R.id.action_newsFragment2_to_newsCardFragment, bundle);
                }
            } catch (Exception e) {
                Log.d(constants.TAG, e.getMessage()); //Если вдруг что-то пошло не так, но должно работать как часы
            }
        });

        //Удаляем новость из БД
        adapter.setOnNewsDeleteClickListener(
                (view, position) -> {
                    News news = adapter.getNews(position);
                    if (news != null) {
                        mViewModel.deleteNews(adapter.getNews(position), null);
                    }
                }
        );
        observerNews = news -> adapter.setNews(news);
        mViewModel.getAllNews().observe(this, observerNews);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        observerNews = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}