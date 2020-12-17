package com.tanxe.supple_online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanxe.supple_online.R;
import com.tanxe.supple_online.model.Lesson;
import com.tanxe.supple_online.screen.YoutubeLessonVideoActivity;

import java.util.List;

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.Holder> {

    private Context context;
    private List<Lesson> lessonsList;

    public LessonsAdapter(Context context, List<Lesson> lessonsList) {
        this.context = context;
        this.lessonsList = lessonsList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lessons_home, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        holder.lessons = lessonsList.get(position);
        holder.imvImageLesson.setImageResource(holder.lessons.getThumbnail());
        holder.tvTitleLesson.setText(holder.lessons.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YoutubeLessonVideoActivity.class);
                intent.putExtra("videoID",holder.lessons.getIDVideo());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }



    public class Holder extends RecyclerView.ViewHolder {
        private ImageView imvImageLesson;
        private TextView tvTitleLesson;
        private Lesson lessons;
        public Holder(@NonNull View itemView) {
            super(itemView);
            imvImageLesson = (ImageView) itemView.findViewById(R.id.imvImageLesson);
            tvTitleLesson = (TextView) itemView.findViewById(R.id.tvTitleLesson);
        }
    }
}
