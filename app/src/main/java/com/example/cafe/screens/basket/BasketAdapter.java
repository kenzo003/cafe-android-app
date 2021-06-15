package com.example.cafe.screens.basket;

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
import com.example.cafe.models.BasketProduct;
import com.example.cafe.utilits.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder>{
    private List<BasketProduct> basketProducts;
    private OnItemClickListener OnItemClickListener;
    private OnItemClickListener OnItemBasketClickListener;
    private OnItemClickListener OnItemFavoriteClickListener;
    private OnItemClickListener OnItemMinusClickListener;
    private OnItemClickListener OnItemPlusClickListener;
    private final Context context;
    private String priceBasket;

    public BasketAdapter(List<BasketProduct> basketProducts, Context context) {
        this.priceBasket = "0";
        this.basketProducts = basketProducts;
        this.context = context;
    }

    public void setBasket(List<BasketProduct> basketProducts) {
        if (basketProducts != null)
            this.basketProducts = basketProducts;
        notifyDataSetChanged();
    }

    public BasketProduct getProduct(int position) {
        if (position >= 0 && position < basketProducts.size())
            return basketProducts.get(position);
        else
            return null;
    }

    public void deleteProduct(int position) {
        if (position >= 0 && position < basketProducts.size()){
            basketProducts.remove(position);
            notifyDataSetChanged();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false); //
        return new BasketAdapter.BasketViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull @NotNull BasketAdapter.BasketViewHolder holder, int position) {
        BasketProduct model = basketProducts.get(position);
        holder.bind(model);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(model.getProduct().product_logo)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.logo);

    }

    @Override
    public int getItemCount() {
        if (basketProducts != null)
            return basketProducts.size();
        else
            return 0;
    }

    public String getPrice(){
        int price = 0;
        for (BasketProduct item: basketProducts) {
            price+=Integer.parseInt(item.getPriceBasket());
        }
        return String.valueOf(price);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.OnItemClickListener = onItemClickListener;
    }

    public void setOnBasketClickListener(OnItemClickListener onBasketClickListener) {
        this.OnItemBasketClickListener = onBasketClickListener;
    }

    public void setOnFavoriteClickListener(OnItemClickListener onFavoriteClickListener) {
        this.OnItemFavoriteClickListener = onFavoriteClickListener;
    }


    public void setOnItemMinusClickListener(OnItemClickListener onItemMinusClickListener) {
        this.OnItemMinusClickListener = onItemMinusClickListener;
    }


    public void setOnItemPlusClickListener(OnItemClickListener onItemPlusClickListener) {
        this.OnItemPlusClickListener = onItemPlusClickListener;
    }

    final class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView logo;
        private final TextView name;
        private final TextView price;
        private final TextView count;
        private final Button delBasket;
        private final Button addFavorite;
        private final Button btnMinus;
        private final Button btnPlus;


        //TODO: Здесь добавить событе клика по корзине
        public BasketViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.fp_img_v_product_logo);
            name = itemView.findViewById(R.id.fp_txt_v_product_name);
            count = itemView.findViewById(R.id.fp_txt_v_product_count);
            price = itemView.findViewById(R.id.fp_txt_v_product_price);
            addFavorite = itemView.findViewById(R.id.fp_btn_product_favorite_add);
            delBasket = itemView.findViewById(R.id.fp_btn_product_delete);
            btnMinus = itemView.findViewById(R.id.fp_btn_minus);
            btnPlus = itemView.findViewById(R.id.fp_btn_plus);

            itemView.setOnClickListener(this);
            addFavorite.setOnClickListener(this::onFavoriteClick);
            delBasket.setOnClickListener(this::onBasketClick);
            btnMinus.setOnClickListener(this::onMinusClick);
            btnPlus.setOnClickListener(this::onPlusClick);
        }

        @SuppressLint("SetTextI18n")
        private void bind(BasketProduct basketProduct) {
            name.setText(basketProduct.getProduct().product_name);
            price.setText(basketProduct.getProduct().product_price + " Р");
            count.setText(basketProduct.getCountProduct());
        }

        private void onMinusClick(View view) {
            OnItemMinusClickListener.onItemClick(view, getAbsoluteAdapterPosition());
        }

        private void onPlusClick(View view) {
            OnItemPlusClickListener.onItemClick(view, getAbsoluteAdapterPosition());
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
