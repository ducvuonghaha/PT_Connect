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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.constants.CometChatConstants;
import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.User;
import com.tanxe.supple_online.screen.DetailCoachActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import constant.StringContract;
import screen.messagelist.CometChatMessageListActivity;

import static com.tanxe.supple_online.screen.LoginActivity.BASE_URL;

public class CoachSuggestionsAdapter extends RecyclerView.Adapter<CoachSuggestionsAdapter.Holder> {

    private Context context;
    private List<User> coachesList;


    public CoachSuggestionsAdapter(Context context, List<User> coachesList) {
        this.context = context;
        this.coachesList = coachesList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coach_suggestions_home, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.coach = coachesList.get(position);
        holder.tvNameCoachesSuggestionHome.setText(holder.coach.getFullname());
        String specialized = "| ";
        int size = holder.coach.getSpecialized().size();
        for (int i = 0; i < size; i++) {
            specialized += holder.coach.getSpecialized().get(i) + " | ";
        }
        holder.tvSpecializedSuggest.setText(specialized);
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
        Picasso.get().load(BASE_URL+"/public/"+holder.coach.getImageProfile()).into(holder.imvCoachesSuggestionHome);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("nameCoach", holder.coach.getFullname());
                bundle.putString("imageCoach",BASE_URL+"/public/"+ holder.coach.getImageProfile());
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
        return coachesList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private User coach;
        private ImageView imvCoachesSuggestionHome;
        private TextView tvNameCoachesSuggestionHome,tvSpecializedSuggest;
        private Button btnSendCoachesBySuggestionHome;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imvCoachesSuggestionHome = (ImageView) itemView.findViewById(R.id.imvCoachesSuggestionHome);
            tvNameCoachesSuggestionHome = (TextView) itemView.findViewById(R.id.tvNameCoachesSuggestionHome);
            tvSpecializedSuggest = (TextView) itemView.findViewById(R.id.tvSpecializedSuggest);
            btnSendCoachesBySuggestionHome = (Button) itemView.findViewById(R.id.btnSendCoachesBySuggestionHome);
        }
    }
}
