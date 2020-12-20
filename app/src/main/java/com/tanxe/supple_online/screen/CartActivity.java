package com.tanxe.supple_online.screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CartAdapter;
import com.tanxe.supple_online.dao.ProductInCartDAO;
import com.tanxe.supple_online.helper.BaseActivity;
import com.tanxe.supple_online.model.Cart;
import com.tanxe.supple_online.model.ProductsInCart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.tanxe.supple_online.main_fragment.MallFragment.tvMallNumberInCart;

public class CartActivity extends BaseActivity {

    ProductInCartDAO productInCartDAO;
    CartAdapter orderAdapter;
    List<ProductsInCart> productsInCartList;
    List<Cart> cartList;
    private RecyclerView rvProductCart;
    public static TextView tvSumPrice;
    private Button btnPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        initView();

        productsInCartList = productInCartDAO.getAllProductCart(productInCartDAO.getUsername());
        orderAdapter = new CartAdapter(this, productsInCartList);
        rvProductCart.setLayoutManager(new LinearLayoutManager(this));
        rvProductCart.setAdapter(orderAdapter);
        NumberFormat formatter = new DecimalFormat("#,###");
        tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productsInCartList.size() == 0) {
                   showMessegeError("Bạn chưa có sản phẩm nào");
                } else {
                    startNewActivity(PaymentActivity.class);
                }
            }

        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(CartActivity.this, "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Toast.makeText(CartActivity.this, "on Swiped ", Toast.LENGTH_SHORT).show();
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                productInCartDAO = new ProductInCartDAO(CartActivity.this);
                productInCartDAO.deleteProductCart(productsInCartList.get(position).getProductname());
                productsInCartList.remove(position);
                orderAdapter.notifyDataSetChanged();
                tvMallNumberInCart.setText(String.valueOf(productInCartDAO.getNumberInCart(productInCartDAO.getUsername())));
                tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvProductCart);
    }


    private void initView() {
        cartList = new ArrayList<>();
        productInCartDAO = new ProductInCartDAO(CartActivity.this);
        rvProductCart = (RecyclerView) findViewById(R.id.rvProductCart);
        tvSumPrice = (TextView) findViewById(R.id.tvSumPrice);
        btnPayment = (Button) findViewById(R.id.btnPayment);
    }
}
