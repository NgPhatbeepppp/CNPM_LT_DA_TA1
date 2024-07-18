package com.example.cnpm_lt_da_ta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.Course.CourseDetailActivity;
import com.example.cnpm_lt_da_ta.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<Course> courses;
    private Context context;

    public CourseAdapter(List<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseDescription.setText(course.getDescription());
        try {
            Glide.with(context)
                    .load(context.getResources().getIdentifier(course.getImage(), "drawable", context.getPackageName()))
                    .into(holder.ivCourseImage);
        } catch (NullPointerException e) {
            // Xử lý khi course.getImage() là null
            Glide.with(context)
                    .load(R.drawable.default_image)
                    .into(holder.ivCourseImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int courseId = course.getId();
                Intent intent = new Intent(context, CourseDetailActivity.class);
                intent.putExtra("courseId", courseId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCourseImage;
        TextView tvCourseName, tvCourseDescription, tvCourseLevel;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCourseImage = itemView.findViewById(R.id.ivCourseImage);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
        }
    }
}
