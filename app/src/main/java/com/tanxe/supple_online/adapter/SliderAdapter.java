package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.screen.LoginActivity;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    private int[] slider_images = {
            R.drawable.welcome_slide_1,
            R.drawable.welcome_slide_2,
            R.drawable.welcome_slide_3,
    };

    private String[] slider_headings = {
            "CHẤT LƯỢNG",
            "TIỆN LỢI",
            "CỘNG ĐỒNG"
    };

    private String[] slider_des = {
            "Chúng tôi luôn cố gắng để đảm bảo việc cung cấp những dịch vụ có chất lượng cao nhất cho khách hàng và đối tác.",
            "Không gì quan trọng bằng thời gian và sức khỏe của bạn. Chúng tôi cam kết cung cấp những giải pháp thể hình tốt nhất giúp bạn sống khỏe mạnh và năng động mọi lúc mọi nơi.",
            "Chúng tôi luôn kết nối cộng đồng những người yêu thể thao để giáo dục mọi người về tầm quan trọng của lối sống năng động và khỏe mạnh, qua đó tạo dựng nên lối sống khỏe mạnh và năng động đến tất cả mọi người."
    };

    @Override
    public int getCount() {
        return slider_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_slider, container, false);

        ImageView sliderImageView = view.findViewById(R.id.imgSlider);
        TextView tvHeading = view.findViewById(R.id.tvHeading);
        TextView tvDes = view.findViewById(R.id.tvDescription);
        Button btnDiscover = view.findViewById(R.id.btnSlider);

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });

        sliderImageView.setImageResource(slider_images[position]);
        tvHeading.setText(slider_headings[position]);
        tvDes.setText(slider_des[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
