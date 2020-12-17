package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.dao.ProductInCartDAO;
import com.tanxe.supple_online.model.ProductsInCart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static com.tanxe.supple_online.main_fragment.MallFragment.tvMallNumberInCart;
import static com.tanxe.supple_online.screen.CartActivity.tvSumPrice;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHoler> {
    private ProductInCartDAO productInCartDAO;
    int quantity;
    private Context context;
    private List<ProductsInCart> list;

    public CartAdapter(Context context, List<ProductsInCart> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CartHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        CartHoler orderHoler = new CartHoler(view);
        return orderHoler;
    }


    @Override
    public void onBindViewHolder(@NonNull CartHoler holder, int position) {
        productInCartDAO = new ProductInCartDAO(context);
        holder.tvNameProductOrder.setText(list.get(position).getProductname());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvPriceOrder.setText(formatter.format((list.get(position).getPrice())) + " VNĐ");
        holder.tvQuantityOrder.setText(String.valueOf(list.get(position).getQuantity()));
        Picasso.get().load(list.get(position).getImage()).into(holder.imgProductOrder);
        holder.imgAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity==5){
                    Toast.makeText(context, "Chỉ có thể chọn tối đa 5 sản phẩm", Toast.LENGTH_SHORT).show();
                }else {
                    quantity = Integer.parseInt(holder.tvQuantityOrder.getText().toString());
                    quantity++;
                    long result = productInCartDAO.updateProductCartAmount(new ProductsInCart(quantity), list.get(position).getProductname());
                    if (result == 1) {
                        holder.tvQuantityOrder.setText(String.valueOf(quantity));

                        tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));
                    } else {
                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.imgDelProductOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productInCartDAO = new ProductInCartDAO(context);
                productInCartDAO.deleteProductCart(list.get(position).getProductname());
                list.remove(position);
                notifyDataSetChanged();
                tvMallNumberInCart.setText(String.valueOf(productInCartDAO.getNumberInCart(productInCartDAO.getUsername())));
                tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));
            }
        });
        holder.imgSubQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity==1){
                    Toast.makeText(context, "Phải có it nhất 1 sản phẩm", Toast.LENGTH_SHORT).show();
                }else {
                    quantity = Integer.parseInt(holder.tvQuantityOrder.getText().toString());
                    quantity--;
                    long result = productInCartDAO.updateProductCartAmount(new ProductsInCart(quantity), list.get(position).getProductname());
                    if (result == 1) {
                        holder.tvQuantityOrder.setText(String.valueOf(quantity));
                        tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));
                    } else {
                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartHoler extends RecyclerView.ViewHolder {
        ImageView imgProductOrder, imgDelProductOrder;
        TextView tvNameProductOrder;
        TextView tvPriceOrder;
        TextView tvQuantityOrder;
        ImageView imgAddQuantity,imgSubQuantity;

        public CartHoler(@NonNull View itemView) {
            super(itemView);
            imgProductOrder = itemView.findViewById(R.id.imgProductOrder);
            imgDelProductOrder = itemView.findViewById(R.id.imgDelProductOrder);
            tvNameProductOrder = itemView.findViewById(R.id.tvNameProductOrder);
            tvPriceOrder = itemView.findViewById(R.id.tvPriceOrder);
            tvQuantityOrder = itemView.findViewById(R.id.tvQuantityOrder);
            imgAddQuantity = itemView.findViewById(R.id.imgAddQuantity);
            imgSubQuantity = itemView.findViewById(R.id.imgSubQuantity);

        }
    }
}
