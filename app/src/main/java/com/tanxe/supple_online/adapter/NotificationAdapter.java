package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.Notification;

import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {

    private Context context;
    private List<Notification> notificationList;


    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.notification = notificationList.get(position);
        holder.tvContentNoti.setText(holder.notification.getDescription());
        Date date=holder.notification.getDateRecieve();
        String mydate = java.text.DateFormat.getDateTimeInstance().format(date.getTime());
        holder.tvExpirationDateNoti.setText(mydate);
        holder.tvTitleNoti.setText(holder.notification.getTitle());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tvTitleNoti;
        private TextView tvContentNoti;
        private TextView tvExpirationDateNoti;
        private Notification notification;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvTitleNoti = (TextView) itemView.findViewById(R.id.tvTitleNoti);
            tvContentNoti = (TextView) itemView.findViewById(R.id.tvContentNoti);
            tvExpirationDateNoti = (TextView) itemView.findViewById(R.id.tvExpirationDateNoti);
        }
    }
}
