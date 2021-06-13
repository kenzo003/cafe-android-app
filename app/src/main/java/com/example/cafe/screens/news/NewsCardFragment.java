package com.example.cafe.screens.news;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cafe.databinding.FragmentNewsCardBinding;
import com.example.cafe.models.News;
import com.example.cafe.utilits.Utils;
import com.example.cafe.utilits.constants;


public class NewsCardFragment extends Fragment {
    private FragmentNewsCardBinding mBinding;
    private News news;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentNewsCardBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            news = (News) getArguments().getSerializable(constants.NODE_NEWS);
            if (news!=null){
                mBinding.niTxtVTitle.setText(news.news_title);
                mBinding.niTxtVPubDate.setText(news.news_date);
                mBinding.niTxtVDesc.setText(news.news_desc);
                mBinding.fncTxtVNewsDesc.setText(news.news_desc);

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.centerCrop();
                requestOptions.placeholder(Utils.getRandomDrawbleColor());
                requestOptions.error(Utils.getRandomDrawbleColor());
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(this)
                        .load(news.news_img_url)
                        .apply(requestOptions)
                        .addListener(
                                new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        mBinding.niPrgBar.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        mBinding.niPrgBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                }
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(mBinding.niImgVPlaceholder);
            }
        } catch (Exception exception) {
            Log.d(constants.TAG, exception.getMessage());
        }
    }
}