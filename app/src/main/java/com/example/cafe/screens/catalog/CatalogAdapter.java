package com.example.cafe.screens.catalog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {


    private List<Product> products;
    private OnItemClickListener OnItemClickListener;
    private Context context;


    public CatalogAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    public void setCatalog(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    public Product getProduct(int position){
        return products.get(position);
    }

    @NonNull
    @NotNull
    @Override
    public CatalogAdapter.CatalogViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
        return new CatalogAdapter.CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CatalogAdapter.CatalogViewHolder holder, int position) {

        holder.bind(products.get(position));
        Product model = products.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(model.product_name)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.logo);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    public void setOnCatalogClickListener(CatalogAdapter.OnItemClickListener onCatalogClickListener) {
        this.OnItemClickListener = onCatalogClickListener;
    }

    final class CatalogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //        private OnItemClickListener onItemClickListener;
        private final ImageView logo;
        private final TextView name;


        public CatalogViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.ci_img_v_product_logo);
            name = (TextView) itemView.findViewById(R.id.ci_txt_v_product_name);
            itemView.setOnClickListener(this);
        }

        private void bind(Product product){
            name.setText(product.product_name);
        }

        @Override
        public void onClick(View v) {
            OnItemClickListener.onNewsClick(v, getAbsoluteAdapterPosition());
        }
    }


    interface OnItemClickListener {
        void onNewsClick(View view, int position);
    }

}
