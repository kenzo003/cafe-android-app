package com.example.cafe.screens.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.cafe.R;
import com.example.cafe.models.Product;
import com.example.cafe.utilits.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FavoriteAdapter  extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>{

    private List<Product> products;
    private OnItemClickListener OnItemClickListener;
    private OnItemClickListener OnItemBasketClickListener;
    private OnItemClickListener OnItemFavoriteClickListener;
    private final Context context;


    public FavoriteAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setFavorite(List<Product> products) {
        if (products != null)
            this.products = products;
        notifyDataSetChanged();
    }

    public Product getProduct(int position) {
        if (position >= 0 && position < products.size())
            return products.get(position);
        else
            return null;
    }

    @NonNull
    @NotNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull @NotNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Product model = products.get(position);
        holder.bind(model);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(model.product_logo)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.logo);

    }

    @Override
    public int getItemCount() {
        if (products != null)
            return products.size();
        else
            return 0;
    }

    public void setOnItemClickListener(OnItemClickListener onCatalogClickListener) {
        this.OnItemClickListener = onCatalogClickListener;
    }

    public void setOnBasketClickListener(OnItemClickListener onBasketClickListener) {
        this.OnItemBasketClickListener = onBasketClickListener;
    }

    public void setOnFavoriteClickListener(OnItemClickListener onFavoriteClickListener) {
        this.OnItemFavoriteClickListener = onFavoriteClickListener;
    }



    final class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView logo;
        private final TextView name;
        private final TextView price;
        private final Button addBasket;
        private final Button addFavorite;


        //TODO: Здесь добавить событе клика по корзине
        public FavoriteViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.fi_img_v_product_logo);
            name = itemView.findViewById(R.id.fi_txt_v_product_name);
            price = itemView.findViewById(R.id.fi_txt_v_product_price);
            addFavorite = itemView.findViewById(R.id.fi_btn_product_favorite_add);
            addBasket = itemView.findViewById(R.id.fi_btn_product_delete);

            itemView.setOnClickListener(this);
            addFavorite.setOnClickListener(this::onFavoriteClick);
            addBasket.setOnClickListener(this::onBasketClick);
        }

        @SuppressLint("SetTextI18n")
        private void bind(Product product) {
            name.setText(product.product_name);
            price.setText(product.product_price + " Р");
        }

        private void onBasketClick(View view) {
            OnItemBasketClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }

        private void onFavoriteClick(View view) {
            OnItemFavoriteClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }

        @Override
        public void onClick(View v) {
            OnItemClickListener.onItemClick(v, getAbsoluteAdapterPosition());
        }
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
