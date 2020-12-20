package com.tanxe.supple_online.main_fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CoachSuggestionsAdapter;
import com.tanxe.supple_online.adapter.LessonsAdapter;
import com.tanxe.supple_online.adapter.NewsAdapter;
import com.tanxe.supple_online.adapter.SliderHomeAdapter;
import com.tanxe.supple_online.coach_fragment.BoxingFragment;
import com.tanxe.supple_online.coach_fragment.GymFragment;
import com.tanxe.supple_online.coach_fragment.SwimmingFragment;
import com.tanxe.supple_online.coach_fragment.YogaFragment;
import com.tanxe.supple_online.model.ImageSlider;
import com.tanxe.supple_online.model.Lesson;
import com.tanxe.supple_online.model.New;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.MatchProfileActivity;
import com.tanxe.supple_online.screen.ProfileActivity;
import com.tanxe.supple_online.screen.SearchActivity;
import com.tanxe.supple_online.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import screen.unified.CometChatUnified;

import static android.content.Context.MODE_PRIVATE;
import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class HomeFragment extends Fragment {
    private ImageButton btnProfile;
    private ViewFlipper viewFlipper;
    private Button btnSearch;
    private ImageButton btnInbox;
    private TextView tvNumberInCart;
    private RecyclerView rcvListNewsHome, rcvListLessonsHome, rcvListSuggestionCoachHome;
    private GridLayoutManager gridLayoutManager;
    private NestedScrollView scrollView_home;
    private LinearLayout llHeader,llYogaCoach,llSwimmingCoach,llGymCoach,llBoxingCoach,llMatchPT;
    private ShimmerFrameLayout shimmer_view_container_suggested_coaches;
    String id;
    final Handler handler = new Handler();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
        id = sharedPreferences.getString("id", "");
        getNewsHome();
        getLessonsHome();
        getImageSliderHome();
        getCoachSuggestionHome();
        int images[] = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
        for (int image : images) {
            flipperImages(image);
        }


        btnInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), CometChatUnified.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        llBoxingCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxingFragment boxingFragment = new BoxingFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, boxingFragment, "toBoxingFragment")
                        .addToBackStack(null).commit();
            }
        });

        llGymCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GymFragment gymFragment = new GymFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, gymFragment, "toBoxingFragment")
                        .addToBackStack(null).commit();
            }
        });
        llSwimmingCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwimmingFragment swimmingFragment = new SwimmingFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, swimmingFragment, "toBoxingFragment")
                        .addToBackStack(null).commit();
            }
        });
        llYogaCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YogaFragment yogaFragment = new YogaFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, yogaFragment, "toBoxingFragment")
                        .addToBackStack(null).commit();
            }
        });
        llMatchPT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MatchProfileActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void flipperImages(int image) {
        try {
            ImageView imageView = new ImageView(getContext());
            imageView.setBackgroundResource(image);
            viewFlipper.addView(imageView);
            viewFlipper.setFlipInterval(2500);
            viewFlipper.setAutoStart(true);
            viewFlipper.setInAnimation(getContext(), android.R.anim.fade_in);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void initView(View view) {
        btnProfile = (ImageButton) view.findViewById(R.id.btnProfile);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnInbox = (ImageButton) view.findViewById(R.id.btnInbox);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.vpSlider);
        rcvListNewsHome = (RecyclerView) view.findViewById(R.id.rcvListNewsHome);
        rcvListLessonsHome = (RecyclerView) view.findViewById(R.id.rcvListLessonsHome);
        scrollView_home = (NestedScrollView) view.findViewById(R.id.scrollView_home);
        llHeader = (LinearLayout) view.findViewById(R.id.llHeader);
        sliderView = (SliderView) view.findViewById(R.id.imageSlider);
        rcvListSuggestionCoachHome = (RecyclerView) view.findViewById(R.id.rcvListSuggestionCoachHome);
        llYogaCoach=view.findViewById(R.id.llYogaCoach);
        llSwimmingCoach=view.findViewById(R.id.llSwimmingCoach);
        llGymCoach=view.findViewById(R.id.llGymCoach);
        llBoxingCoach=view.findViewById(R.id.llBoxingCoach);
        llMatchPT=view.findViewById(R.id.llMatchPT);
        shimmer_view_container_suggested_coaches = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container_suggested_coaches);
    }


    private NewsAdapter newsAdapter;
    private List<New> newsList;

    private void getNewsHome() {
        newsList = new ArrayList<>();
        newsList.clear();
        New news = new New(R.drawable.newest_news_gymroom, "Top 10 phòng tập tốt nhất khu vực Hải Phòng","https://camnanghaiphong.vn/top-10-phong-gym-xin-so-o-hai-phong");
        New news2 = new New(R.drawable.newest_news_gymevent, "Sự kiện 'Vì một gia đình khỏe mạnh' được tổ chức tại Hà Nội", "http://www.hanoimoi.com.vn/tin-tuc/Doi-song/968972/nhieu-hoat-dong-tai-chuoi-su-kien-ngay-hoi-gia-dinh");
        New news3 = new New(R.drawable.newest_news_stomachache, "Nguyên nhân dẫn đến cơn đau bụng mỗi đêm", "https://phuongdongdaitrang.vn/bi-dau-bung-ve-dem-va-gan-sang-co-nguy-hiem.html");
        New news4 = new New(R.drawable.newest_news_tired, "Những cách giúp bạn giảm bớt căng thẳng sau giờ làm việc", "https://www.vinmec.com/vi/tin-tuc/thong-tin-suc-khoe/suc-khoe-tong-quat/16-cach-don-gian-de-giam-cang-thang-va-lo-au/");
        newsList.add(news);
        newsList.add(news2);
        newsList.add(news3);
        newsList.add(news4);
        newsAdapter = new NewsAdapter(getContext(), newsList);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        rcvListNewsHome.setAdapter(newsAdapter);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListNewsHome.setLayoutManager(gridLayoutManager);
        rcvListNewsHome.setHasFixedSize(true);
        rcvListNewsHome.setNestedScrollingEnabled(false);
        rcvListNewsHome.scheduleLayoutAnimation();
        newsAdapter.notifyDataSetChanged();
    }

    private LessonsAdapter lessonsAdapter;
    private List<Lesson> lessonsList;

    private void getLessonsHome() {
        lessonsList = new ArrayList<>();
        lessonsList.clear();
        Lesson lessons = new Lesson("XAxycuZPxQE", "BÀI TẬP MÔNG QUẢ ĐÀO | KHẮC PHỤC MÔNG HÓP | 30 phút HIP DIP", R.drawable.lesson_1_home);
        Lesson lessons2 = new Lesson("8zwL-hcU9-Y", "Bốn LÝ DO khiến bạn TẬP NGỰC mãi không lên & GIẢI PHÁP", R.drawable.lesson_2_home);
        Lesson lessons3 = new Lesson("ehpFsiDIbuc", "15 sai lầm phổ biến trong việc tập luyện bạn cần từ bỏ", R.drawable.lesson_4_home);
        Lesson lessons4 = new Lesson("mra27LEwpS8", "Cách để Đốt nhiều Mỡ Hơn Khi Ngủ", R.drawable.lesson_3_home);
        lessonsList.add(lessons);
        lessonsList.add(lessons2);
        lessonsList.add(lessons3);
        lessonsList.add(lessons4);
        lessonsAdapter = new LessonsAdapter(getContext(), lessonsList);
        rcvListLessonsHome.setAdapter(lessonsAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListLessonsHome.setLayoutManager(gridLayoutManager);
        rcvListLessonsHome.setHasFixedSize(true);
        rcvListLessonsHome.setNestedScrollingEnabled(false);
        rcvListLessonsHome.scheduleLayoutAnimation();
        lessonsAdapter.notifyDataSetChanged();
    }

    private SliderView sliderView;
    List<ImageSlider> imageSliderList;
    private void getImageSliderHome() {
        ImageSlider imageSlider = new ImageSlider(R.drawable.banner_slide_1);
        ImageSlider imageSlider2 = new ImageSlider(R.drawable.banner_slide_2);
        ImageSlider imageSlider3 = new ImageSlider(R.drawable.banner_slide_3);
        imageSliderList = new ArrayList<>();
        imageSliderList.clear();
        imageSliderList.add(imageSlider2);
        imageSliderList.add(imageSlider);
        imageSliderList.add(imageSlider3);
        sliderView.setSliderAdapter(new SliderHomeAdapter(getContext(), imageSliderList));
        sliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private List<User> coachList;
    private CoachSuggestionsAdapter coachesAdapter;
    private void getCoachSuggestionHome() {
        coachList = new ArrayList<>();
        coachList.clear();
        coachesAdapter = new CoachSuggestionsAdapter(getContext(), coachList);
        rcvListSuggestionCoachHome.setAdapter(coachesAdapter);
        gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rcvListSuggestionCoachHome.setLayoutManager(gridLayoutManager);
        rcvListSuggestionCoachHome.setHasFixedSize(true);
        rcvListSuggestionCoachHome.setNestedScrollingEnabled(false);
        rcvListSuggestionCoachHome.scheduleLayoutAnimation();
        coachesAdapter.notifyDataSetChanged();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getHotCoach();
            }
        }, 2000);

    }

    private void getHotCoach(){
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
        coachService.getHotCoach().enqueue(new Callback<List<User>>() {
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
                shimmer_view_container_suggested_coaches.stopShimmer();
                shimmer_view_container_suggested_coaches.setVisibility(View.GONE);
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
        shimmer_view_container_suggested_coaches.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmer_view_container_suggested_coaches.stopShimmer();

    }
}

