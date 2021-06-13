package com.example.cafe.screens.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private RecyclerView mRecyclerView;
    private NewsAdapter adapter;
    private Observer<List<News>> observerNews;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            ((MainActivity) getActivity()).navController.navigate(R.id.auth_nav);
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
        mRecyclerView = mBinding.nfRecView;
        mRecyclerView.setAdapter(adapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        adapter.setOnNewsClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onNewsClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(constants.NODE_NEWS, adapter.getNews(position));
                ((MainActivity)getActivity()).navController.navigate(R.id.action_newsFragment2_to_newsCardFragment, bundle);
                Toast.makeText(getContext(), adapter.getNews(position).news_title, Toast.LENGTH_LONG).show();
            }
        });

        adapter.setOnNewsDeleteClickListener(
                new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onNewsClick(View view, int position) {
                        mViewModel.deleteNews(adapter.getNews(position));
                        adapter.delNews(position);
                    }
                }
        );


        observerNews = new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> news) {
                adapter.setNews(news);
            }
        };

        mViewModel.getAllNews().observe(this, observerNews);

    }

    @Override
    public void onPause() {
        super.onPause();
        observerNews = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinding = null;
        observerNews = null;
    }

}