package com.tanxe.supple_online.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.helper.Config;
import com.tanxe.supple_online.model.Products;
import com.tanxe.supple_online.screen.DetailProductActivity;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class SoldProductsAdapter extends RecyclerView.Adapter<SoldProductsAdapter.Holder> {

    private Context context;
    private List<Products> productList;

    public SoldProductsAdapter(Context context, List<Products> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sold_product_mall, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.products = productList.get(position);
        holder.tvTitleNew.setText(holder.products.getProductName());
//        holder.tvPriceProduct.setText(Config.decimalFormat.format((holder.products.getPrice())) + " Đ");
        Picasso.get().load(BASE_URL+"/public/"+productList.get(position).getImageProduct()).into(holder.imvImageNew);
        holder.ratingProductMall.setRating(Float.parseFloat(holder.products.getVote()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imvImageNew;
        private TextView tvTitleNew;
        private RatingBar ratingProductMall;
        private Products products;
        private TextView tvPriceProduct;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imvImageNew = (ImageView) itemView.findViewById(R.id.imvImageNew);
            tvTitleNew = (TextView) itemView.findViewById(R.id.tvTitleNew);
            ratingProductMall = (RatingBar) itemView.findViewById(R.id.ratingProductMall);
            tvPriceProduct = (TextView) itemView.findViewById(R.id.tvPriceProduct);
        }
    }
}

