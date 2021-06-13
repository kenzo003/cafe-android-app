package com.example.cafe.screens.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cafe.R;
import com.example.cafe.models.News;
import com.example.cafe.utilits.Utils;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> news;
    private OnItemClickListener OnNewsClickListener;
    private OnItemClickListener OnNewsDeleteClickListener;
    private Context context;

    public void setNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    public NewsAdapter(List<News> news, Context context) {
        this.news = news;
        this.context = context;

    }

    public News getNews(int position) {
        return this.news.get(position);
    }

    public void delNews(int position){
        this.news.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsAdapter.NewsViewHolder holder, int position) {
        holder.bind(news.get(position));
        News model = news.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(model.news_img_url)
                .apply(requestOptions)
                .addListener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.progress_bar.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progress_bar.setVisibility(View.GONE);
                                return false;
                            }
                        }
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.media);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public void setOnNewsClickListener(OnItemClickListener onNewsClickListener) {
        this.OnNewsClickListener = onNewsClickListener;
    }

    public void setOnNewsDeleteClickListener(OnItemClickListener OnNewsDeleteClickListener) {
        this.OnNewsDeleteClickListener = OnNewsDeleteClickListener;
    }

    final class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final Button delete;
        private final TextView header;
        private final TextView description;
        private final ImageView media;
        private final TextView data;
        private final ProgressBar progress_bar;

        public NewsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            delete = (Button)itemView.findViewById(R.id.ni_btn_news_del);
            header = (TextView) itemView.findViewById(R.id.ni_txt_V_title);
            description = (TextView) itemView.findViewById((R.id.ni_txt_v_desc));
            media = (ImageView) itemView.findViewById(R.id.ni_img_v_placeholder);
            data = (TextView) itemView.findViewById(R.id.ni_txt_v_pubDate);
            progress_bar = (ProgressBar) itemView.findViewById(R.id.ni_prg_bar);


            delete.setOnClickListener(this::onDelClick);
            itemView.setOnClickListener(this);
        }

        public void bind(News news) {
            header.setText(news.news_title);
            description.setText(news.news_desc);
            data.setText(news.news_date);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        }

        private void onDelClick(View view){
            OnNewsDeleteClickListener.onNewsClick(view, getAbsoluteAdapterPosition());
        }

        @Override
        public void onClick(View v) {
            OnNewsClickListener.onNewsClick(v, getAbsoluteAdapterPosition());
        }
    }

    interface OnItemClickListener {
        void onNewsClick(View view, int position);
    }
}

