package com.tanxe.supple_online.screen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatRatingBar;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.dao.ProductInCartDAO;
import com.tanxe.supple_online.helper.BaseActivity;
import com.tanxe.supple_online.model.ProductsInCart;
import com.tanxe.supple_online.model.User;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static com.tanxe.supple_online.main_fragment.MallFragment.tvMallNumberInCart;

public class DetailProductActivity extends BaseActivity {

    private List<User> userList;
    private TextView tvTitleDetailProduct;
    private RelativeLayout rlBarProductDetail;
    private TextView tvSpecies;
    private ImageView imgProductDetail;
    private TextView tvNameProductDetail;
    private TextView tvPriceProductDetail;
    private TextView tvDescriptionDetail;
    private Button btnAddProduct;
    private AppCompatRatingBar ratingDetailProduct;
    private ProductInCartDAO productInCartDAO;
    private ImageView btnBackFromDetailProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String name = bundle.getString("nameSP");
        String classify = bundle.getString("type");
        String description = bundle.getString("motaSP");
        int price = bundle.getInt("giaSP");
        String image = bundle.getString("imageSP");

        SharedPreferences sharedPreferences=getSharedPreferences("My Data",MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        tvDescriptionDetail.setText(description);
        tvNameProductDetail.setText(name);
        NumberFormat formatter = new DecimalFormat("#,###");
        tvPriceProductDetail.setText(formatter.format(price));
        tvSpecies.setText(classify);
        tvTitleDetailProduct.setText(name);
        ratingDetailProduct.setRating(Float.parseFloat("5"));
        Picasso.get().load(image).into(imgProductDetail);
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCart(name,price,image,username);

            }
        });
        btnBackFromDetailProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        productInCartDAO=new ProductInCartDAO(this);
        tvTitleDetailProduct = (TextView) findViewById(R.id.tvTitleDetailProduct);
        rlBarProductDetail = (RelativeLayout) findViewById(R.id.rlBarProductDetail);
        tvSpecies = (TextView) findViewById(R.id.tvSpecies);
        imgProductDetail = (ImageView) findViewById(R.id.imgProductDetail);
        tvNameProductDetail = (TextView) findViewById(R.id.tvNameProductDetail);
        tvPriceProductDetail = (TextView) findViewById(R.id.tvPriceProductDetail);
        tvDescriptionDetail = (TextView) findViewById(R.id.tvDescriptionDetail);
        btnAddProduct = (Button) findViewById(R.id.btnAddProduct);
        ratingDetailProduct = (AppCompatRatingBar) findViewById(R.id.ratingDetailProduct);
        btnBackFromDetailProduct = (ImageView) findViewById(R.id.btnBackFromDetailProduct);
    }


    private void addToCart(String nameSP, int price, String image, String username) {
        Log.e("Name", nameSP);
        long result = productInCartDAO.insertProductCart(new ProductsInCart(image, price, nameSP, 1, username));
        if (result > 0) {
            showMessegeSuccess("Thêm vào giỏ hàng thành công");
            tvMallNumberInCart.setText(String.valueOf(productInCartDAO.getNumberInCart(productInCartDAO.getUsername())));
        } else {
            Toast.makeText(this, "Thêm vào giỏ hàng thất bại. Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
        }
    }
}



