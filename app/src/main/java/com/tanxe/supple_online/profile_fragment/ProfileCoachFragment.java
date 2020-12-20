package com.tanxe.supple_online.profile_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.DetailProfileCoachActivity;
import com.tanxe.supple_online.screen.LoginActivity;
import com.tanxe.supple_online.screen.MyOrderActivity;
import com.tanxe.supple_online.screen.ReportActivity;
import com.tanxe.supple_online.screen.UpdateCoachActivity;
import com.tanxe.supple_online.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

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

public class ProfileCoachFragment extends Fragment {
    Button btnUpdateCoach2;
    List<User> userList;
    View view;
    private TextView tvNameProfile;
    private TextView tvSpecialized, tvNotification,tvAddress;
    private NestedScrollView nesAll;
    LinearLayout llUpdate,llDetailProfile,llMyOrder,llLogOut,llChangePass,llReport;
    Switch swStatus;
    ImageView imgCoachProfile;
    String passPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile_coach, container, false);
        init(view);
        userList=new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String id = sharedPreferences.getString("id", "");
        getInformationUser(username);
        btnUpdateCoach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.size()==0){
                    return;
                }else {
                    Intent intent = new Intent(getContext(), UpdateCoachActivity.class);
                    intent.putExtra("fullname", userList.get(0).getFullname());
                    startActivity(intent);
                }

            }
        });
        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swStatus.isChecked() == true) {
                    switchCoach(id, "Updated");
                } else {
                    switchCoach(id, "OFF");
                }
            }
        });
        llDetailProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.size()==0){
                    return;
                }else {
                    String fullname = userList.get(0).getFullname();
                    String address = userList.get(0).getAddress();
                    String background = userList.get(0).getBackground();
                    String age = userList.get(0).getAge();
                    String imageProfile=userList.get(0).getImageProfile();
                    String place=userList.get(0).getWorkplace();
                    String specialize = "";
                    for(int i = 0;i<userList.get(0).getSpecialized().size();i++) {
                        specialize += userList.get(0).getSpecialized().get(i);
                    }

                    Intent intent=new Intent(getContext(), DetailProfileCoachActivity.class);
                    intent.putExtra("fullname",fullname);
                    intent.putExtra("address",address);
                    intent.putExtra("background",background);
                    intent.putExtra("age",age);
                    intent.putExtra("imageProfile",BASE_URL + "/public/" +imageProfile);
                    intent.putExtra("place",place);
                    intent.putExtra("specialize",specialize);
                    startActivity(intent);
                }


            }
        });
        llChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = View.inflate(getContext(), R.layout.change_password_item, null);
                mBuilder.setView(mView);
                mBuilder.setCancelable(true);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
                EditText edtPasswordChange;
                EditText edtRePasswordChange;
                Button btnSaveNewPassword;
                Button btnCancelForgetPass;
                edtPasswordChange = mView.findViewById(R.id.edtPasswordChange);
                edtRePasswordChange = mView.findViewById(R.id.edtRePasswordChange);
                btnSaveNewPassword = mView.findViewById(R.id.btnSaveNewPasswordChange);
                btnCancelForgetPass = mView.findViewById(R.id.btnCanceltPassChange);

                btnSaveNewPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String pass = edtPasswordChange.getText().toString();
                        String repass = edtRePasswordChange.getText().toString();
                        String id = userList.get(0).getId();
                        String username = userList.get(0).getUsername();
                        if (pass.isEmpty()) {
                            edtPasswordChange.setError("Bạn không được để trống");
                            edtPasswordChange.requestFocus();
                        } else if (repass.isEmpty()) {
                            edtRePasswordChange.setError("Bạn không được để trống");
                            edtRePasswordChange.requestFocus();
                        } else if (!pass.matches(passPattern)) {
                            edtPasswordChange.setError("Mật khẩu ít nhất phải 8 ký tự gồm tối thiểu 1 chữ hoa, 1 chữ thường, 1 số và 1 ký tự đặc biệt");
                            edtPasswordChange.requestFocus();
                        } else if (!(pass.equals(repass))) {
                            edtRePasswordChange.setError("Mật khẩu không trùng khớp");
                            edtRePasswordChange.requestFocus();
                        } else {
                            changePass(id, username, pass);
                            dialog.dismiss();
                        }
                    }
                });
                btnCancelForgetPass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.size()==0){
                    return;
                }else {
                    Intent intent=new Intent(getContext(), MyOrderActivity.class);
                    intent.putExtra("usernameOrder",userList.get(0).getUsername());
                    startActivity(intent);
                }

            }
        });

        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ReportActivity.class);
                startActivity(intent);
            }
        });
        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences myPrefs = getContext().getSharedPreferences("My Data",
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void init(View view) {
        tvNameProfile = (TextView) view.findViewById(R.id.tvNameProfile);
//        tvSpecialized = (TextView) view.findViewById(R.id.tvSpecialized);
        tvNotification = (TextView) view.findViewById(R.id.tvNotification);
//        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        nesAll = view.findViewById(R.id.nesAll);
        llUpdate = view.findViewById(R.id.llUpdate);
        llMyOrder = view.findViewById(R.id.llMyOrder);
        llLogOut = view.findViewById(R.id.llLogOut);
        llDetailProfile = view.findViewById(R.id.llDetailProfile);
        llChangePass = view.findViewById(R.id.llChangePass);
        llReport = view.findViewById(R.id.llReport);
        btnUpdateCoach2 = view.findViewById(R.id.btnUpdateCoach);
        swStatus = view.findViewById(R.id.swStatus);
        imgCoachProfile = view.findViewById(R.id.imgCoachProfile);

    }
    private void changePass(String id, String username, String password) {
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
        userService.changePass(id, username, password).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("Change Success")) {
                    Toast.makeText(getContext(), "Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Change Fail")) {
                    Toast.makeText(getContext(), "Thay đổi mật khẩu thất bại. Xin kiểm tra lại", Toast.LENGTH_SHORT).show();
                } else if (response.body().equals("Duplicate")) {
                    Toast.makeText(getContext(), "Mật khẩu đã trung với mật khẩu cũ. Mời kiểm tra lại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Fail Change Password", t.getLocalizedMessage());
            }
        });
    }
    private void getInformationUser(String userlogin) {
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
        userService.getInforUser(userlogin).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userList = new ArrayList<>();
                userList = response.body();
                if (userList.get(0).getType().equals("Coach") && userList.get(0).getStatus().equals("Updated")) {
                    llUpdate.setVisibility(View.GONE);
                    tvNameProfile.setText(userList.get(0).getFullname());
//                    tvAddress.setText(userList.get(0).getAddress());
//                    String specialized = "";
//                    for (int i = 0; i < userList.get(0).getSpecialized().size(); i++) {
//                        specialized += userList.get(0).getSpecialized().get(i) + "";
//                    }
//                    tvSpecialized.setText(specialized);
                    Picasso.get().load(BASE_URL + "/public/" + userList.get(0).getImageProfile()).into(imgCoachProfile);
                    swStatus.setChecked(true);
                } else if (userList.get(0).getType().equals("Coach") && userList.get(0).getStatus().equals("None")) {
                    tvNotification.setText("Hồ sơ của bạn chưa cập nhật xong");
                    nesAll.setVisibility(View.GONE);
                } else if (userList.get(0).getType().equals("Coach") && userList.get(0).getStatus().equals("OFF")) {
                    llUpdate.setVisibility(View.GONE);
                    tvNameProfile.setText(userList.get(0).getFullname());
//                    tvAddress.setText(userList.get(0).getAddress());
//                    String specialized = "";
//                    for (int i = 0; i < userList.get(0).getSpecialized().size(); i++) {
//                        specialized += userList.get(0).getSpecialized().get(i) + " , ";
//                    }
//                    tvSpecialized.setText(specialized);
                    Picasso.get().load(BASE_URL + "/public/" + userList.get(0).getImageProfile()).into(imgCoachProfile);
                    swStatus.setChecked(false);
                } else {
                    if (userList.get(0).getStatus().equals("None")) {
                        nesAll.setVisibility(View.GONE);
                        tvNotification.setText("Bạn chưa phải là Huấn luyện viên");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Thông báo");
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.it_notification, null, false);
                        builder.setView(view1);
                        Dialog dialog = builder.show();
                        Button btnUpdateCoach;
                        Button btnCancelUpdate;
                        btnUpdateCoach = (Button) view1.findViewById(R.id.btnConfirmCoach);
                        btnCancelUpdate = (Button) view1.findViewById(R.id.btnCancelUpdate);
                        btnUpdateCoach.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), UpdateCoachActivity.class);
                                intent.putExtra("fullname", userList.get(0).getFullname());
                                startActivity(intent);
                            }
                        });

                        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                            }
                        });
                    } else if (userList.get(0).getStatus().equals("Updating")) {
                        nesAll.setVisibility(View.GONE);
                        tvNotification.setText("Hồ sơ của bạn đang được kiểm duyệt. Vui lòng đợi!");
                        btnUpdateCoach2.setVisibility(View.GONE);
                    }


                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });

    }

    private void switchCoach(String id, String status) {
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
        userService.switchCoach(status, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });

    }
}