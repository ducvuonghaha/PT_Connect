package com.tanxe.supple_online.main_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CoachesAdapter;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.MatchProfileActivity;
import com.tanxe.supple_online.service.UserService;
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

import static android.content.Context.MODE_PRIVATE;
import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class FindFragment extends Fragment {

    private EditText edtNameHLV;
    private RecyclerView rcvListCoachInFind;
    private GridLayoutManager gridLayoutManager;
    private ImageView btnMatchProfile;
    private ShimmerFrameLayout shimmer_view_container_coaches;
    final Handler handler = new Handler();
    String id;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
         id = sharedPreferences.getString("id", "");
        getCoachList();

        btnMatchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MatchProfileActivity.class);
                startActivity(intent);
            }
        });
        edtNameHLV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().toLowerCase().trim();
                List<User> newlist = new ArrayList<>();
                for (User user : coachList) {
                    if (user.getFullname().toLowerCase().contains(text)) {
                        newlist.add(user);
                    }
                }
                coachesAdapter.updateList(newlist);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void initView(View view) {
        edtNameHLV = (EditText) view.findViewById(R.id.edtNameHLV);
        rcvListCoachInFind = (RecyclerView) view.findViewById(R.id.rcvListCoachInFind);
        btnMatchProfile = (ImageView) view.findViewById(R.id.btnMatchProfile);
        shimmer_view_container_coaches = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_coaches);
    }

    private List<User> coachList;
    private CoachesAdapter coachesAdapter;

    private void getCoachList() {
        coachList = new ArrayList<>();
        coachList.clear();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAllCoach();
            }
        }, 4000);
        coachesAdapter = new CoachesAdapter(getContext(), coachList);
        rcvListCoachInFind.setAdapter(coachesAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListCoachInFind.setLayoutManager(gridLayoutManager);
        rcvListCoachInFind.setHasFixedSize(true);
        rcvListCoachInFind.setNestedScrollingEnabled(false);
        rcvListCoachInFind.scheduleLayoutAnimation();
        coachesAdapter.notifyDataSetChanged();
    }
    private void getAllCoach(){
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
        UserService coachService = retrofit.create(UserService.class);
        coachService.getAllCoach().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                coachList.clear();

                List<User> coaches =response.body();
                for (int i = 0; i < coaches.size(); i++) {
                    if (coaches.get(i).getId().equals(id)){
                        coaches.remove(i);
                        break;

                    }
                }
                Log.e("Size",coaches.size()+"");
                shimmer_view_container_coaches.stopShimmer();
                shimmer_view_container_coaches.setVisibility(View.GONE);
                coachList.addAll(coaches);
                coachesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Failure",t.getLocalizedMessage()+"");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shimmer_view_container_coaches.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmer_view_container_coaches.stopShimmer();

    }
}
