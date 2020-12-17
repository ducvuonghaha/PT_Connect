package com.tanxe.supple_online.screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cometchat.pro.constants.CometChatConstants;
import com.tanxe.supple_online.R;
import com.squareup.picasso.Picasso;

import constant.StringContract;
import de.hdodenhof.circleimageview.CircleImageView;
import screen.messagelist.CometChatMessageListActivity;

public class DetailCoachActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    private RelativeLayout barDetailCoach;
    private AppCompatRatingBar ratingCoach;
    private TextView tvDetailNameCoach, tvAddressCoach, tvWorkPlace, tvEmailCoach;
    private TextView tvDetailBGCoach, tvAgeCoach;
    private Button btnChat;
    private ImageButton btnCall;
    private CircleImageView imgCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_coach);
        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("dataCoach");
        String nameCoach = bundle.getString("nameCoach");
        String imageCoach = bundle.getString("imageCoach");
        String addressCoach = bundle.getString("addressCoach");
        String workplace = bundle.getString("workplace");
        String ageCoach = bundle.getString("ageCoach");
        String emailCoach = bundle.getString("emailCoach");
        String idCoach = bundle.getString("idCoach");
        String phoneCoach = bundle.getString("phoneCoach");
        String rateCoach = bundle.getString("rateCoach");
        String bgCoach = bundle.getString("bgCoach");
        Picasso.get().load(imageCoach).into(imgCoach);
        tvDetailBGCoach.setText(bgCoach);
        tvWorkPlace.setText(workplace);
        tvDetailNameCoach.setText(nameCoach);
        tvAddressCoach.setText(addressCoach);
        tvAgeCoach.setText(ageCoach);
        tvEmailCoach.setText(emailCoach);
        ratingCoach.setRating(Float.parseFloat(rateCoach));

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailCoachActivity.this, CometChatMessageListActivity.class);
                intent.putExtra(StringContract.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                intent.putExtra(StringContract.IntentStrings.UID, idCoach);
                intent.putExtra(StringContract.IntentStrings.NAME, nameCoach);
                intent.putExtra(StringContract.IntentStrings.STATUS, CometChatConstants.USER_STATUS_OFFLINE);
                startActivity(intent);
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall(phoneCoach);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void makePhoneCall(String phoneNo) {
        if (ContextCompat.checkSelfPermission(DetailCoachActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailCoachActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial ="tel:"+ phoneNo;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
        }
    }

    private void initView() {
        barDetailCoach = (RelativeLayout) findViewById(R.id.barDetailCoach);
        ratingCoach = (AppCompatRatingBar) findViewById(R.id.ratingCoach);
        tvDetailNameCoach = (TextView) findViewById(R.id.tvDetailNameCoach);
        tvDetailBGCoach = (TextView) findViewById(R.id.tvDetailBGCoach);
        tvAddressCoach = (TextView) findViewById(R.id.tvAddressCoach);
        tvWorkPlace = (TextView) findViewById(R.id.tvWorkPlace);
        tvAgeCoach = (TextView) findViewById(R.id.tvAgeCoach);
        tvEmailCoach = (TextView) findViewById(R.id.tvEmailCoach);
        btnChat = (Button) findViewById(R.id.btnChat);
        btnCall = (ImageButton) findViewById(R.id.btnCall);
        imgCoach = findViewById(R.id.imgCoach);


    }
}