package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.helper.Config;
import com.tanxe.supple_online.model.ProductsInCart;

import java.util.List;

import static com.tanxe.supple_online.helper.Config.decimalFormat;

public class CartInPaymentAdapter extends RecyclerView.Adapter<CartInPaymentAdapter.Holder> {

    private Context context;
    private List<ProductsInCart> productsInCartList;


    public CartInPaymentAdapter(Context context, List<ProductsInCart> productsInCartList) {
        this.context = context;
        this.productsInCartList = productsInCartList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_in_payment, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.productsInCart = productsInCartList.get(position);
        Picasso.get().load(holder.productsInCart.getImage()).into(holder.imgProductInCartPayment);
        holder.tvPriceProductInCartPayment.setText(decimalFormat.format(holder.productsInCart.getPrice()) + " VNĐ");
        holder.tvNameProductInCartPayment.setText(holder.productsInCart.getProductname());
    }

    @Override
    public int getItemCount() {
        return productsInCartList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imgProductInCartPayment;
        private TextView tvNameProductInCartPayment;
        private TextView tvPriceProductInCartPayment;
        private ProductsInCart productsInCart;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imgProductInCartPayment = (ImageView) itemView.findViewById(R.id.imgProductInCartPayment);
            tvNameProductInCartPayment = (TextView) itemView.findViewById(R.id.tvNameProductInCartPayment);
            tvPriceProductInCartPayment = (TextView) itemView.findViewById(R.id.tvPriceProductInCartPayment);
        }
    }
}
