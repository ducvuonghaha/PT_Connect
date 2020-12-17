package com.tanxe.supple_online.coach_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CoachesAdapter;
import com.tanxe.supple_online.main_fragment.HomeFragment;
import com.tanxe.supple_online.model.User;
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

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class GymFragment extends Fragment {
    List<User> coachList;
    CoachesAdapter coachesAdapter;
    private ImageView imvBackInMallProduct;
    private TextView tvTitleCoach;
    private TextView tvTitleCoach1;
    private TextView tvTitleCoach2;
    private RecyclerView rvCoach;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        init(view);

        tvTitleCoach.setText("GYM Coach");
        coachList=new ArrayList<>();
        coachesAdapter=new CoachesAdapter(getContext(),coachList);
        rvCoach.setAdapter(coachesAdapter);
        rvCoach.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllCoach("GYM");
        imvBackInMallProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, homeFragment, "backToHome").addToBackStack(null).commit();
            }
        });
        return view;
    }

    private void init(View view) {

        imvBackInMallProduct = (ImageView) view.findViewById(R.id.imvBackInMallProduct);
        tvTitleCoach = (TextView) view.findViewById(R.id.tvTitleCoach);
        rvCoach = (RecyclerView) view.findViewById(R.id.rvCoach);

    }
    private void getAllCoach(String specialized){
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
        List<String> specializeds=new ArrayList<>();
        specializeds.add(specialized);
        coachService.getCoachForType(specializeds).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                coachList.clear();
                List<User> coaches =new ArrayList<>();
                coaches = response.body();
                coachList.addAll(coaches);
                coachesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("Failure",t.getLocalizedMessage()+"");
            }
        });
    }
}
