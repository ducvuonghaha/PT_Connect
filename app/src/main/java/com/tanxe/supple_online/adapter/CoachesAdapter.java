package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.DetailCoachActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import constant.StringContract;
import screen.messagelist.CometChatMessageListActivity;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class CoachesAdapter extends RecyclerView.Adapter<CoachesAdapter.Holder> {

    private Context context;
    private List<User> coachList;

    public CoachesAdapter(Context context, List<User> coachList) {
        this.context = context;
        this.coachList = coachList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coach, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.coach = coachList.get(position);
        holder.tvNameCoach.setText(holder.coach.getFullname());
        int size = holder.coach.getSpecialized().size();
        String specialized = "";
        for (int i = 0; i < size; i++) {
            specialized += holder.coach.getSpecialized().get(i) + " \n";
        }

        holder.tvDescriptionCoach.setText(specialized);
        holder.ratingCoach.setRating(Float.parseFloat(holder.coach.getRating()));
        holder.btnSendCoachesBySuggestionHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CometChatMessageListActivity.class);
                intent.putExtra(StringContract.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER);
                intent.putExtra(StringContract.IntentStrings.UID,holder.coach.getId());
                intent.putExtra(StringContract.IntentStrings.NAME,holder.coach.getFullname());
                intent.putExtra(StringContract.IntentStrings.STATUS,CometChatConstants.USER_STATUS_OFFLINE);
                context.startActivity(intent);
            }
        });
        Picasso.get().load(BASE_URL+"/public/"+holder.coach.getImageProfile()).into(holder.imvAvatarCoach);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nameCoach", holder.coach.getFullname());
                bundle.putString("imageCoach", BASE_URL+"/public/"+holder.coach.getImageProfile());
                bundle.putString("addressCoach", holder.coach.getAddress());
                bundle.putString("workplace", holder.coach.getWorkplace());
                bundle.putString("ageCoach", holder.coach.getAge());
                bundle.putString("emailCoach", holder.coach.getEmail());
                bundle.putString("idCoach", holder.coach.getId());
                bundle.putString("phoneCoach", holder.coach.getPhone());
                bundle.putString("rateCoach", holder.coach.getRating());
                bundle.putString("bgCoach", holder.coach.getBackground());
                Intent intent = new Intent(context, DetailCoachActivity.class);
                intent.putExtra("dataCoach", bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView imvAvatarCoach;
        private Button btnSendCoachesBySuggestionHome;
        private TextView tvNameCoach;
        private TextView tvDescriptionCoach;
        private AppCompatRatingBar ratingCoach;
        private User coach;

        public Holder(@NonNull View itemView) {
            super(itemView);
            imvAvatarCoach = (ImageView) itemView.findViewById(R.id.imvAvatarCoach);
            btnSendCoachesBySuggestionHome = (Button) itemView.findViewById(R.id.btnSendCoachesBySuggestionHome);
            tvNameCoach = (TextView) itemView.findViewById(R.id.tvNameCoach);
            tvDescriptionCoach = (TextView) itemView.findViewById(R.id.tvDescriptionCoach);
            ratingCoach = (AppCompatRatingBar) itemView.findViewById(R.id.ratingCoach);
        }
    }

    public void updateList(List<User> list) {
        coachList=new ArrayList<>();
        coachList.addAll(list);
        notifyDataSetChanged();

    }
}
