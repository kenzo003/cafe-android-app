package com.example.cafe.screens.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.R;
import com.example.cafe.models.News;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.NewsViewHolder> {
    private List<News> news;

    public Adapter(List<News> news){
        this.news = news;
    }
    @NonNull
    @NotNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull Adapter.NewsViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    public void add(News news){
        this.news.add(news);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return news.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private TextView header;
        private TextView description;


        public NewsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header_title);
            description = (TextView) itemView.findViewById(R.id.tag_state_description);
        }

        private void bind(@NonNull News news) {
            header.setText(news.header_text);
            description.setText(news.description);
        }
    }
}
