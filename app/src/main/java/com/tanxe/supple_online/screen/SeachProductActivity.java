package com.tanxe.supple_online.screen;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.ProductsAdapter;
import com.tanxe.supple_online.model.Products;
import com.tanxe.supple_online.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class SeachProductActivity extends AppCompatActivity {
    private EditText edtNameProduct;
    private RecyclerView rvSearchProduct;
    private List<Products> productsList;
    private ProductsAdapter productsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seach_product);
        init();
        productsList=new ArrayList<>();
        productsAdapter=new ProductsAdapter(this,productsList);
        rvSearchProduct.setAdapter(productsAdapter);
        rvSearchProduct.setLayoutManager(new GridLayoutManager(this,2));
        getAllProduct();
        edtNameProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase().trim();
                List<Products> newlist = new ArrayList<>();
                for (Products products : productsList) {
                    if (products.getProductName().toLowerCase().contains(text)) {
                        newlist.add(products);
                    }
                }
                productsAdapter.updateListProduct(newlist);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init() {
        edtNameProduct = (EditText) findViewById(R.id.edtNameProduct);
        rvSearchProduct = (RecyclerView) findViewById(R.id.rvSearchProduct);

    }

    private void getAllProduct(){
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
        ProductService productService = retrofit.create(ProductService.class);
        productService.getAllProducts().enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                productsList.clear();
                List<Products> products = response.body();
                productsList.addAll(products);
                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.e("Failure", t.getLocalizedMessage() + "");
            }
        });
    }
}