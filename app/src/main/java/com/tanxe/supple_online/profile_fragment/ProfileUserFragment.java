package com.tanxe.supple_online.profile_fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.LoginActivity;
import com.tanxe.supple_online.screen.OrderActivity;
import com.tanxe.supple_online.screen.ProfileDetailActivity;
import com.tanxe.supple_online.screen.ReportActivity;
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

public class ProfileUserFragment extends Fragment {
    private List<User> userList;
    private TextView tvNameProfile;
    private LinearLayout llChangePass, llDetailProfile, llMyOrder, llLogOut, llReport;
    String passPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        initView(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("My Data", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.e("aa", username);
        userList=new ArrayList<>();
        getInformationUser(username);
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
        llDetailProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.size()==0){
                    return;
                }else {
                    String username = userList.get(0).getUsername();
                    String password = userList.get(0).getPassword();
                    String email = userList.get(0).getEmail();
                    String fullname = userList.get(0).getFullname();
                    String phone = userList.get(0).getPhone();
                    String sex = userList.get(0).getSex();
                    Intent intent = new Intent(getContext(), ProfileDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    bundle.putString("password", password);
                    bundle.putString("email", email);
                    bundle.putString("fullname", fullname);
                    bundle.putString("phone", phone);
                    bundle.putString("sex", sex);
                    intent.putExtra("dataUser", bundle);
                    startActivity(intent);
                }

            }
        });
        llMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( userList.size()==0){
                    return;
                }else {
                    Intent intent = new Intent(getContext(), OrderActivity.class);
                    intent.putExtra("usernameOrder", userList.get(0).getUsername());
                    startActivity(intent);
                }

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
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        llReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReportActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initView(View view) {
        tvNameProfile = (TextView) view.findViewById(R.id.tvNameProfile);
        llChangePass = view.findViewById(R.id.llChangePass);
        llDetailProfile = view.findViewById(R.id.llDetailProfile);
        llMyOrder = view.findViewById(R.id.llMyOrder);
        llLogOut = view.findViewById(R.id.llLogOut);
        llReport = view.findViewById(R.id.llReport);
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
                userList = response.body();
                tvNameProfile.setText(userList.get(0).getFullname());


            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });
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
}