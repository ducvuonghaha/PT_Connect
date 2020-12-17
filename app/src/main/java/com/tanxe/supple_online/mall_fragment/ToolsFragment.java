package com.tanxe.supple_online.mall_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.ProductsAdapter;
import com.tanxe.supple_online.main_fragment.MallFragment;
import com.tanxe.supple_online.model.Products;
import com.tanxe.supple_online.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class ToolsFragment extends Fragment {
    List<Products> productsList;
    ProductsAdapter productsAdapter;
    private String type="equipment";
    private Spinner spnSortTools;
    private RecyclerView rvToolsDeals;
    private ImageView imvBackInMallProduct;
    private TextView tvTitleMall;
    private List<String> spList;
    private ArrayAdapter<String> spAdater;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall_product, container, false);
        initView(view);
        tvTitleMall.setText("Dụng cụ tập luyện");
        imvBackInMallProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MallFragment mallFragment = new MallFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mallFragment, "backToMallFromTools")
                        .addToBackStack(null).commit();
            }
        });
        productsList=new ArrayList<>();
        productsAdapter=new ProductsAdapter(getContext(),productsList);
        rvToolsDeals.setAdapter(productsAdapter);
        rvToolsDeals.setLayoutManager(new GridLayoutManager(getContext(),2));
        getProductForType();
        spList=new ArrayList<>();
        spList.add("Sắp xếp sản phẩm");
        spList.add("Giá : Thấp đến Cao");
        spList.add("Giá : Cao đến Thấp");
        spAdater=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spList);
        spAdater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSortTools.setAdapter(spAdater);
        spnSortTools.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ABC",productsList.size()+"");
                String name=spList.get(position);
                if (name.equals("Sắp xếp sản phẩm")){
                    return;
                }else if (name.equals("Giá : Thấp đến Cao")){
//                    productsList.clear();
//                    productsList=new ArrayList<>();
                    Collections.sort(productsList, new Comparator<Products>() {
                        @Override
                        public int compare(Products o1, Products o2) {
                            return Double.valueOf(o1.getPrice()).compareTo(Double.valueOf(o2.getPrice()));
                        }
                    });
                    productsAdapter=new ProductsAdapter(getContext(),productsList);
                    rvToolsDeals.setAdapter(productsAdapter);
                    rvToolsDeals.setLayoutManager(new GridLayoutManager(getContext(),2));
                    productsAdapter.notifyDataSetChanged();
                }else if (name.equals("Giá : Cao đến Thấp")){
                    Collections.sort(productsList, new Comparator<Products>() {
                        @Override
                        public int compare(Products o1, Products o2) {
                            return Double.valueOf(o2.getPrice()).compareTo(Double.valueOf(o1.getPrice()));
                        }
                    });
                    productsAdapter=new ProductsAdapter(getContext(),productsList);
                    rvToolsDeals.setAdapter(productsAdapter);
                    rvToolsDeals.setLayoutManager(new GridLayoutManager(getContext(),2));
                    productsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void initView(View view) {
        spnSortTools = (Spinner) view.findViewById(R.id.spnSort);
        tvTitleMall =  view.findViewById(R.id.tvTitleMall);
        rvToolsDeals = (RecyclerView) view.findViewById(R.id.rvDeals);
        imvBackInMallProduct = (ImageView) view.findViewById(R.id.imvBackInMallProduct);
    }
    private void getProductForType() {
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
        productService.getWheyProducts(type).enqueue(new Callback<List<Products>>() {
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
