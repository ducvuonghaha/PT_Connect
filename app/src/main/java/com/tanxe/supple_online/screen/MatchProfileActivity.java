package com.tanxe.supple_online.screen;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.adapter.CoachSuggestionsAdapter;
import com.tanxe.supple_online.helper.BaseActivity;
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

public class MatchProfileActivity extends BaseActivity {

    private Button btnEditPlace;
    private Button btnEditSpecialize;
    private Button btnEditTarget;
    private Button btnEditAge;
    private Button btnEditSex;
    private Button btnMatch;
    private TextView tvPlace;
    private TextView tvSpecialized;
    private TextView tvTarget;
    private TextView tvAge;
    private TextView tvSex;
    String ageFrom, ageTo;
    List<User> userList;
    RecyclerView rvCoachMatch;
    CoachSuggestionsAdapter coachSuggestionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_profile);
        initView();

        userList = new ArrayList<>();
        coachSuggestionsAdapter = new CoachSuggestionsAdapter(this, userList);
        rvCoachMatch.setAdapter(coachSuggestionsAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        rvCoachMatch.setLayoutManager(gridLayoutManager);
        btnEditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileActivity.this);
                builder.setTitle("Địa điểm");
                View view = LayoutInflater.from(MatchProfileActivity.this).inflate(R.layout.it_edit, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                dialog.show();
                RadioGroup rdgPlace;
                Button btnOk;

                rdgPlace = (RadioGroup) view.findViewById(R.id.rdgPlace);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rdgPlace.getCheckedRadioButtonId();
                        if (id == R.id.rdbHN) {
                            tvPlace.setText("Hà Nội");
                        } else if (id == R.id.rdbCT) {
                            tvPlace.setText("Cần Thơ");
                        } else if (id == R.id.rdbDN) {
                            tvPlace.setText("Đà Nẵng");
                        } else if (id == R.id.rdbHCM) {
                            tvPlace.setText("Hồ Chí Minh");
                        } else if (id == R.id.rdbHP) {
                            tvPlace.setText("Hải Phòng");
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        btnEditAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileActivity.this);
                builder.setTitle("Tuổi");
                View view = LayoutInflater.from(MatchProfileActivity.this).inflate(R.layout.it_edit_age, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                dialog.show();
                RadioGroup rdgPlace;
                Button btnOk;

                rdgPlace = (RadioGroup) view.findViewById(R.id.rdgPlace);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rdgPlace.getCheckedRadioButtonId();
                        if (id == R.id.rdb21) {
                            tvAge.setText("Từ 21 dến 30 tuổi");
                        } else if (id == R.id.rdb30) {
                            tvAge.setText("Từ 30 đến 40 tuổi");
                        } else if (id == R.id.rdb40) {
                            tvAge.setText("Trên 40 tuổi");
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        btnEditSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileActivity.this);
                builder.setTitle("Giới tính");
                View view = LayoutInflater.from(MatchProfileActivity.this).inflate(R.layout.it_edit_sex, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                dialog.show();
                RadioGroup rdgPlace;
                Button btnOk;

                rdgPlace = (RadioGroup) view.findViewById(R.id.rdgPlace);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rdgPlace.getCheckedRadioButtonId();
                        if (id == R.id.rdbNam) {
                            tvSex.setText("Nam");
                        } else if (id == R.id.rdbNu) {
                            tvSex.setText("Nữ");
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        btnEditSpecialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileActivity.this);
                builder.setTitle("Bộ môn");
                View view = LayoutInflater.from(MatchProfileActivity.this).inflate(R.layout.it_edit_specialize, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                dialog.show();
                RadioGroup rdgPlace;
                Button btnOk;

                rdgPlace = (RadioGroup) view.findViewById(R.id.rdgPlace);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rdgPlace.getCheckedRadioButtonId();
                        if (id == R.id.rdbYoga) {
                            tvSpecialized.setText("Yoga");
                        } else if (id == R.id.rdbGYM) {
                            tvSpecialized.setText("GYM");
                        } else if (id == R.id.rdbBoxing) {
                            tvSpecialized.setText("Kick Boxing");
                        } else if (id == R.id.rdbSwimming) {
                            tvSpecialized.setText("Bơi lội");
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        btnEditTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchProfileActivity.this);
                builder.setTitle("Mục tiêu");
                View view = LayoutInflater.from(MatchProfileActivity.this).inflate(R.layout.it_edit_target, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                dialog.show();
                RadioGroup rdgPlace;
                Button btnOk;

                rdgPlace = (RadioGroup) view.findViewById(R.id.rdgPlace);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = rdgPlace.getCheckedRadioButtonId();
                        if (id == R.id.rdbTangCan) {
                            tvTarget.setText("Tăng Cân");
                        } else if (id == R.id.rdbTangCao) {
                            tvTarget.setText("Tăng chiều cao");
                        } else if (id == R.id.rdbTangCo) {
                            tvTarget.setText("Tăng cơ");
                        } else if (id == R.id.rdbGiamMo) {
                            tvTarget.setText("Giảm mỡ");
                        } else if (id == R.id.rdbDeoDai) {
                            tvTarget.setText("Dẻo dai");
                        }
                        dialog.dismiss();
                    }
                });

            }
        });
        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = tvPlace.getText().toString();
                String specialize = tvSpecialized.getText().toString();
                String sex = tvSex.getText().toString();
                String age = tvAge.getText().toString();
                if (age.equals("Từ 21 dến 30 tuổi")) {
                    ageFrom = "21";
                    ageTo = "30";
                } else if (age.equals("Từ 30 đến 40 tuổi")) {
                    ageFrom = "30";
                    ageTo = "40";
                } else {
                    ageFrom = "40";
                    ageTo = "100";
                }
                matchCoach(place, specialize, sex, ageFrom, ageTo);
            }
        });
    }

    private void matchCoach(String place, String specialized, String sex, String ageFrom, String ageTo) {
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
        userService.matchCoach(place, specialized, sex, ageFrom, ageTo).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userList.clear();
                List<User> users = new ArrayList<>();
                users = response.body();
                Log.e("Size",users.size()+"");
                if (users.size()==0){
                    userList.addAll(users);
                    coachSuggestionsAdapter.notifyDataSetChanged();
                    showMessegeWarning("Không có Huấn luyện viên phù hợp với bạn");
                }else {
                    rvCoachMatch.setVisibility(View.VISIBLE);
                    userList.addAll(users);
                    coachSuggestionsAdapter.notifyDataSetChanged();
                }



            }


            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("XXXXXX", t.getCause() + "");
            }
        });
    }

    private void initView() {
        tvPlace = (TextView) findViewById(R.id.tvPlace);
        tvSpecialized = (TextView) findViewById(R.id.tvSpecialized);
        tvTarget = (TextView) findViewById(R.id.tvTarget);
        tvAge = (TextView) findViewById(R.id.tvAge);
        tvSex = (TextView) findViewById(R.id.tvSex);
        btnMatch = (Button) findViewById(R.id.btnMatch);

        btnEditPlace = (Button) findViewById(R.id.btnEditPlace);
        btnEditSpecialize = (Button) findViewById(R.id.btnEditSpecialize);
        btnEditTarget = (Button) findViewById(R.id.btnEditTarget);
        btnEditAge = (Button) findViewById(R.id.btnEditAge);
        btnEditSex = (Button) findViewById(R.id.btnEditSex);
        btnMatch = (Button) findViewById(R.id.btnMatch);
        rvCoachMatch = findViewById(R.id.rvCoachMatch);

    }
}