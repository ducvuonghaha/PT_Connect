package com.tanxe.supple_online.screen;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.service.ReportService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class ReportActivity extends AppCompatActivity {
    private EditText edtContent;
    private EditText edtDetail;
    private Button btnSendReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
        btnSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = getSharedPreferences("My Token", MODE_PRIVATE);
                String token = sharedPreferences1.getString("token", "");
                SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String content=edtContent.getText().toString();
                String detail=edtDetail.getText().toString();
                Log.e("ABC",content);
                Log.e("ABC",detail);
                sendReportFromUser(username,content,detail,token);
            }
        });
    }

    private void init() {

        edtContent = (EditText) findViewById(R.id.edtContent);
        edtDetail = (EditText) findViewById(R.id.edtDetail);
        btnSendReport = (Button) findViewById(R.id.btnSendReport);

    }

    private void sendReportFromUser(String username, String content, String detail, String token) {
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
        ReportService reportService = retrofit.create(ReportService.class);
        reportService.sendReportFromUser(username, content, detail, token).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportActivity.this);
                builder.setTitle("Thông báo");
                View view = LayoutInflater.from(ReportActivity.this).inflate(R.layout.it_noti, null, false);
                builder.setView(view);
                AlertDialog dialog = builder.show();
                TextView tvNoti;
                Button btnOk;

                tvNoti = (TextView) view.findViewById(R.id.tvNoti);
                btnOk = (Button) view.findViewById(R.id.btnOk);
                tvNoti.setText("Phản hồi của bạn đã gửi thành công. Vui lòng đợi trong 24h để chúng tôi phản hồi");
                btnOk.setText("Đồng ý");
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        edtContent.setText("");
                        edtDetail.setText("");
                    }
                });

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("XXXXXX", t.getLocalizedMessage());
            }
        });
    }
}