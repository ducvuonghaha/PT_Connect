package com.tanxe.supple_online.main_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.ProductsAdapter;
import com.tanxe.supple_online.adapter.SoldProductsAdapter;
import com.tanxe.supple_online.dao.ProductInCartDAO;
import com.tanxe.supple_online.mall_fragment.ClothesFragment;
import com.tanxe.supple_online.mall_fragment.SuppleFragment;
import com.tanxe.supple_online.mall_fragment.ToolsFragment;
import com.tanxe.supple_online.model.Products;
import com.tanxe.supple_online.screen.CartActivity;
import com.tanxe.supple_online.screen.SeachProductActivity;
import com.tanxe.supple_online.service.ProductService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanxe.supple_online.service.SoldOutProductService;

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

public class MallFragment extends Fragment {
    ProductsAdapter productsAdapter;
    SoldProductsAdapter soldProductsAdapter;
    List<Products> productsList;
    private Button btnMallSearch;
    private ImageButton btnMallCart;
    public static TextView tvMallNumberInCart;
    private ViewFlipper vpFlipperImageMall;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rcvListHotProduct;
    private RecyclerView rcvListSoldProduct;
    private LinearLayout llSuppleFragment;
    private LinearLayout llClothesFragment;
    private LinearLayout llToolsFragment;
    private ShimmerFrameLayout shimmerFrameLayout;
    private ShimmerFrameLayout shimmerFrameLayoutSold;
    final Handler handler = new Handler();
    private ProductInCartDAO productInCartDAO;
    private List<Products> soldProductList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall, container, false);
        initView(view);
        tvMallNumberInCart.setText(String.valueOf(productInCartDAO.getNumberInCart(productInCartDAO.getUsername())));
        btnMallCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProduct();
                getSoldProduct();
            }
        }, 4000);
        int images[] = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
        for (int image : images) {
            flipperImages(image);
        }

        llSuppleFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SuppleFragment suppleFragment = new SuppleFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, suppleFragment, "toSuppleFragment")
                        .addToBackStack(null).commit();
            }
        });

        llClothesFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothesFragment clothesFragment = new ClothesFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, clothesFragment, "toClothesFragment")
                        .addToBackStack(null).commit();
            }
        });

        llToolsFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToolsFragment toolsFragment = new ToolsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, toolsFragment, "toToolsFragment")
                        .addToBackStack(null).commit();
            }
        });

        btnMallSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SeachProductActivity.class));
            }
        });

        rcvListSoldProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        vpFlipperImageMall.addView(imageView);
        vpFlipperImageMall.setFlipInterval(2500);
        vpFlipperImageMall.setAutoStart(true);
        vpFlipperImageMall.setInAnimation(getContext(), android.R.anim.fade_in);
    }

    private void initView(View view) {
        llSuppleFragment = (LinearLayout) view.findViewById(R.id.llSuppleFragment);
        llClothesFragment = (LinearLayout) view.findViewById(R.id.llClothesFragment);
        llToolsFragment = (LinearLayout) view.findViewById(R.id.llToolsFragment);

        productInCartDAO = new ProductInCartDAO(getContext());
        btnMallSearch = (Button) view.findViewById(R.id.btnMallSearch);
        btnMallCart = (ImageButton) view.findViewById(R.id.btnMallCart);
        tvMallNumberInCart = (TextView) view.findViewById(R.id.tvMallNumberInCart);
        vpFlipperImageMall = (ViewFlipper) view.findViewById(R.id.vpFlipperImageMall);
        rcvListHotProduct = (RecyclerView) view.findViewById(R.id.rcvListHotProduct);
        rcvListSoldProduct = (RecyclerView) view.findViewById(R.id.rcvListSoldProduct);
        shimmerFrameLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayoutSold = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_sold);
    }


    private void getProduct() {
        productsList = new ArrayList<>();
        productsList.clear();
        getHotProduct();
        productsAdapter = new ProductsAdapter(getContext(), productsList);
        rcvListHotProduct.setAdapter(productsAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListHotProduct.setLayoutManager(gridLayoutManager);
        rcvListHotProduct.setHasFixedSize(true);
        rcvListHotProduct.setNestedScrollingEnabled(false);
        rcvListHotProduct.scheduleLayoutAnimation();
        productsAdapter.notifyDataSetChanged();
    }

    private void getSoldProduct() {
        soldProductList = new ArrayList<>();
        soldProductList.clear();
        getSoldProductFromServer();
        soldProductsAdapter = new SoldProductsAdapter(getActivity().getBaseContext(), soldProductList);
        rcvListSoldProduct.setAdapter(soldProductsAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListSoldProduct.setLayoutManager(gridLayoutManager);
        rcvListSoldProduct.setHasFixedSize(true);
        rcvListSoldProduct.setNestedScrollingEnabled(false);
        rcvListSoldProduct.scheduleLayoutAnimation();
        soldProductsAdapter.notifyDataSetChanged();
    }

    private void getHotProduct() {
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
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                productsList.addAll(products);
                productsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.e("Failure", t.getLocalizedMessage() + "");
            }
        });
    }


    private void getSoldProductFromServer() {
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
        SoldOutProductService soldOutProductService = retrofit.create(SoldOutProductService.class);
        soldOutProductService.getAllSoldProducts().enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                soldProductList.clear();
                List<Products> products = response.body();
                shimmerFrameLayoutSold.stopShimmer();
                shimmerFrameLayoutSold.setVisibility(View.GONE);
                soldProductList.addAll(products);
                soldProductsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.e("Failure", t.getLocalizedMessage() + "");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayoutSold.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayoutSold.stopShimmer();

    }
}
