package com.tanxe.supple_online.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tanxe.supple_online.R;

public class DetailNewsActivity extends AppCompatActivity {

    private TextView tvTitleNews;
    private WebView wvNews;
    private ImageView btnBackFromDetailNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        initView();
        Intent intent = getIntent();
        tvTitleNews.setText(intent.getStringExtra("titleNews"));
        tvTitleNews.setSelected(true);
        wvNews.loadUrl(intent.getStringExtra("contentNews"));
        btnBackFromDetailNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        tvTitleNews = (TextView) findViewById(R.id.tvTitleNews);
        wvNews = (WebView) findViewById(R.id.wvNews);
        btnBackFromDetailNew = (ImageView) findViewById(R.id.btnBackFromDetailNew);
    }
}