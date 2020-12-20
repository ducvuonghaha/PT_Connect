package com.tanxe.supple_online.screen;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanxe.supple_online.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;


public class UpdateCoachActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 100;
    private Spinner spnAddress;
    private Spinner spnSpecialized;
    private Button btnSelectImage, btnUpdateCoach;
    private EditText edtAddressCoach, edtBackground, edtAge;
    ImageView imgProfile;
    Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_coach);
        initView();
        getSpinnerAddress();
        getSpinnerSpecialized();

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                try {
                    startActivityForResult(intent, REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }


            }
        });
        btnUpdateCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = getSharedPreferences("My Token", MODE_PRIVATE);
                String token = sharedPreferences1.getString("token", "");
                List<String> specializeds = new ArrayList<>();
                String specialized = spnSpecialized.getSelectedItem().toString();
                specializeds.add(specialized);
                Intent intent = getIntent();
                String fullname = intent.getStringExtra("fullname");

                String place = spnAddress.getSelectedItem().toString();
                SharedPreferences sharedPreferences = getSharedPreferences("My Data", MODE_PRIVATE);
                String id = sharedPreferences.getString("id", "");
                String username = sharedPreferences.getString("username", "");
                String address = edtAddressCoach.getText().toString();
                String background = edtBackground.getText().toString();
                String age = edtAge.getText().toString();
                if (age.isEmpty()) {
                    edtAge.setError("Không được để trống");
                    edtAge.requestFocus();
                } else if (address.isEmpty()) {
                    edtAddressCoach.setError("Không được để trống");
                    edtAddressCoach.requestFocus();
                } else if (background.isEmpty()) {
                    edtBackground.setError("Không được để trống");
                    edtBackground.requestFocus();
                } else if (selectedImage == null) {
                    Toast.makeText(UpdateCoachActivity.this, "Mời bạn chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                } else {
                    upload( id, fullname, place, age, specialized, background, "Updating", token, username, address);
                }

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode==RESULT_OK)
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    InputStream inputStream1 = getContentResolver().openInputStream(data.getData());
                    selectedImage = BitmapFactory.decodeStream(inputStream1);
                    imgProfile.setImageBitmap(selectedImage);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


        }

    }

    private void upload( String id, String fullname,
                         String workplace, String age, String specialized,
                         String background, String status, String token, String username, String address) {
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
        File filesDir = getApplicationContext().getFilesDir();
        File file = new File(filesDir, "image" + ".jpeg");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("exImage", file.getName(), requestFile);
        RequestBody idPart = RequestBody.create(MultipartBody.FORM, id);
        RequestBody fullnamePart = RequestBody.create(MultipartBody.FORM, fullname);
        RequestBody workplacePart = RequestBody.create(MultipartBody.FORM, workplace);
        RequestBody agePart = RequestBody.create(MultipartBody.FORM, age);
        RequestBody specializedPart = RequestBody.create(MultipartBody.FORM, specialized);
        RequestBody backgroundPart = RequestBody.create(MultipartBody.FORM, background);
        RequestBody statusPart = RequestBody.create(MultipartBody.FORM, status);
        RequestBody tokenPart = RequestBody.create(MultipartBody.FORM, token);
        RequestBody usernamenPart = RequestBody.create(MultipartBody.FORM, username);
        RequestBody addressPart = RequestBody.create(MultipartBody.FORM, address);
        UserService uploadService = retrofit.create(UserService.class);
        uploadService.updateCoach(body, idPart, fullnamePart, workplacePart, agePart, specializedPart, backgroundPart, statusPart, tokenPart, usernamenPart, addressPart).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body().equals("kích thước file lớn hơn 2mb")){
                    Toast.makeText(UpdateCoachActivity.this, "File không được quá 2MB", Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateCoachActivity.this);
                    View view = LayoutInflater.from(UpdateCoachActivity.this).inflate(R.layout.it_noti, null, false);
                    builder.setTitle("Thông báo");
                    builder.setView(view);
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.show();
                    TextView tvNoti;
                    Button btnOk;
                    tvNoti = (TextView) view.findViewById(R.id.tvNoti);
                    btnOk = (Button) view.findViewById(R.id.btnOk);
                    tvNoti.setText("Hồ sơ của bạn sẽ được phê duyệt trong 24h");
                    btnOk.setText("Đồng ý");
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(UpdateCoachActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UpdateCoachActivity.this, "Không kết nối được sever", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void getSpinnerAddress() {
        String arr[] = {"Hà Nội", "Hồ Chí Minh", "Cần Thơ", "Đà Nẵng", "Hải Phòng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnAddress.setAdapter(adapter);

    }

    private void getSpinnerSpecialized() {
        String arr[] = {"GYM", "Kick Boxing", "Bơi lội", "Yoga"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnSpecialized.setAdapter(adapter);
    }

    private void initView() {
        spnAddress = (Spinner) findViewById(R.id.spnAddress);
        spnSpecialized = (Spinner) findViewById(R.id.spnSpecialized);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnUpdateCoach = findViewById(R.id.btnUpdateCoach);
        edtAddressCoach = findViewById(R.id.edtAddressCoach);
        edtBackground = findViewById(R.id.edtBackground);
        edtAge = findViewById(R.id.edtAge);
        imgProfile = findViewById(R.id.imgProfile);

    }
}