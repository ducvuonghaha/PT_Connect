package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.Carts;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHoder> {
    private Context context;
    private List<Carts> cartsList;

    public OrderAdapter(Context context, List<Carts> cartsList) {
        this.context = context;
        this.cartsList = cartsList;

    }

    @NonNull
    @Override
    public OrderHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        OrderHoder orderHoler = new OrderHoder(view);
        return orderHoler;

    }

    @Override
    public void onBindViewHolder(@NonNull OrderHoder holder, int position) {
        String nameProduct="";
        for (int i = 0; i < cartsList.get(position).getCart().size(); i++) {
            nameProduct+=cartsList.get(position).getCart().get(i).getName()+" x "+cartsList.get(position).getCart().get(i).getQuantity()+""+"\n";
        }
        Date date=cartsList.get(position).getDateCart();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(date.getTime());
        holder.tvDateOrder.setText(mydate);
        holder.tvNameProduct.setText(nameProduct);
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvTotal.setText(formatter.format(cartsList.get(position).getTotalPrice()) + "VND");
    }

    @Override
    public int getItemCount() {
        return cartsList.size();
    }

    public class OrderHoder extends RecyclerView.ViewHolder {
        public TextView tvTotal;
        public TextView tvNameProduct,tvDateOrder;

        public OrderHoder(@NonNull View itemView) {
            super(itemView);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
            tvDateOrder = (TextView) itemView.findViewById(R.id.tvDateOrder);
            tvNameProduct = (TextView) itemView.findViewById(R.id.tvNameProduct);

        }
    }
}
