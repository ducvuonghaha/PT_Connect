package com.tanxe.supple_online.screen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CartAdapter;
import com.tanxe.supple_online.dao.ProductInCartDAO;
import com.tanxe.supple_online.helper.BaseActivity;
import com.tanxe.supple_online.model.Cart;
import com.tanxe.supple_online.model.Carts;
import com.tanxe.supple_online.model.ProductsInCart;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.service.CartService;
import com.tanxe.supple_online.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class PaymentActivity extends BaseActivity {
    ProductInCartDAO productInCartDAO;
    List<ProductsInCart> productsInCartList;
    List<Cart> cartList;

    EditText edtFullNamePayment, edtAddressPayment, edtPhonePayment;
    private RecyclerView rvPayment;
    private LinearLayout linearLayout2;
    public TextView tvSumPrice;
    private Button btnPayment;
    RadioButton rdbConfirm;
    CartAdapter orderAdapter;
    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        userList=new ArrayList<>();
        SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        getInformationUser(username);
        rdbConfirm.setChecked(true);
        rdbConfirm.setClickable(false);
        productsInCartList = productInCartDAO.getAllProductCart(productInCartDAO.getUsername());
        orderAdapter = new CartAdapter(this, productsInCartList);
        rvPayment.setLayoutManager(new LinearLayoutManager(this));
        rvPayment.setAdapter(orderAdapter);
        NumberFormat formatter = new DecimalFormat("#,###");
        tvSumPrice.setText((formatter.format(productInCartDAO.getTongTien(productInCartDAO.getUsername()))));
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtFullNamePayment.getText().toString().trim();
                String address = edtAddressPayment.getText().toString().trim();
                String phone = edtPhonePayment.getText().toString().trim();
                if (name.isEmpty()) {
                    edtFullNamePayment.setError("Không được để trống");
                    edtFullNamePayment.requestFocus();
                } else if (address.isEmpty()) {
                    edtAddressPayment.setError("Không được để trống");
                    edtAddressPayment.requestFocus();
                } else if (phone.isEmpty()) {
                    edtPhonePayment.setError("Không được để trống");
                    edtPhonePayment.requestFocus();
                } else {
                    if (productsInCartList.size() == 0) {
                        Toast.makeText(PaymentActivity.this, "Bạn chưa có sản phẩm nào", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < productsInCartList.size(); i++) {
                            productsInCartList = productInCartDAO.getAllProductCart(productInCartDAO.getUsername());
                            Cart cart = new Cart(productsInCartList.get(i).getProductname(),
                                    (productsInCartList.get(i).getQuantity()),
                                    (productsInCartList.get(i).getPrice()));
                            cartList.add(cart);
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
                        String username = sharedPreferences.getString("username", "");
                        Log.e("username", username);
                        Date currentTime = Calendar.getInstance().getTime();
                        Carts carts = new Carts(username, cartList, edtPhonePayment.getText().toString(), edtFullNamePayment.getText().toString(),
                                edtAddressPayment.getText().toString(), (currentTime), productInCartDAO.getTongTien(productInCartDAO.getUsername()));
                        upCart(carts);
                    }
                }

            }
        });
    }

    private void getInformationUser(String username) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        UserService userService = retrofit.create(UserService.class);
        userService.getInforUser(username).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                   userList=response.body();
                   if (userList.size()==0){
                       return;
                   }else {
                       edtAddressPayment.setText(userList.get(0).getAddress());
                       edtFullNamePayment.setText(userList.get(0).getFullname());
                       edtPhonePayment.setText(userList.get(0).getPhone());
                   }




            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });
    }

    private void upCart(Carts carts) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CartService cartService = retrofit.create(CartService.class);
        cartService.upCart(carts).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Up Cart Success")) {
                    Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startNewActivity(SuccessOrderActivity.class);
                    productInCartDAO.deleteCart(productInCartDAO.getUsername());
                } else if (response.body().equals("Up Cart Failure")) {
                    showMessegeError("Đã xảy ra lỗi. Vui lòng thử lại sau");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Fail", t.getLocalizedMessage() + "");
            }
        });
    }

    private void initView() {
        cartList = new ArrayList<>();
        productInCartDAO = new ProductInCartDAO(PaymentActivity.this);
        edtAddressPayment = findViewById(R.id.edtAddressPayment);
        edtFullNamePayment = findViewById(R.id.edtFullNamePayment);
        edtPhonePayment = findViewById(R.id.edtPhonePayment);
        rvPayment = (RecyclerView) findViewById(R.id.rvPayment);
        linearLayout2 = (LinearLayout) findViewById(R.id.linearLayout2);
        tvSumPrice = (TextView) findViewById(R.id.tvSumPrice);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        rdbConfirm = findViewById(R.id.rdbConfirm);
    }
}