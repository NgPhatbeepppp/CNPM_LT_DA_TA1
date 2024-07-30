package com.example.cnpm_lt_da_ta.ManagerAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cnpm_lt_da_ta.DAO.CourseDAO;
import com.example.cnpm_lt_da_ta.Manager.CourseEditActivity;
import com.example.cnpm_lt_da_ta.Course.Course;
import com.example.cnpm_lt_da_ta.R;

import java.util.ArrayList;
import java.util.List;

public class CourseManagementAdapter extends RecyclerView.Adapter<CourseManagementAdapter.ViewHolder> implements Filterable {

    private final CourseDAO courseDAO;
    private List<Course> courseList;
    private List<Course> courseListFiltered;
    private Context context;
    private AlertDialog.Builder alertDialogBuilder;

    public CourseManagementAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.courseListFiltered = courseList;
        this.context = context;
        this.courseDAO = new CourseDAO(context);
        this.courseDAO.open();
        alertDialogBuilder = new AlertDialog.Builder(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseListFiltered.get(position);
        holder.tvCourseName.setText(course.getName());
        holder.tvCourseDescription.setText(course.getDescription());

        holder.btnEditCourse.setOnClickListener(v -> {
            Intent intent = new Intent(context, CourseEditActivity.class);
            intent.putExtra("courseId", course.getId());
            context.startActivity(intent);
        });

        holder.btnDeleteCourse.setOnClickListener(v -> {
            alertDialogBuilder
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa khóa học này?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        courseDAO.deleteCourse(course.getId());
                        courseListFiltered.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return courseListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence
                    constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    courseListFiltered = courseList;
                } else {
                    List<Course> filteredList = new ArrayList<>();
                    for (Course course : courseList) {
                        if (course.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(course);
                        }
                    }
                    courseListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = courseListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                courseListFiltered = (ArrayList<Course>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCourseName, tvCourseDescription;
        ImageView btnEditCourse, btnDeleteCourse;

        ViewHolder(View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvCourseDescription = itemView.findViewById(R.id.tvCourseDescription);
            btnEditCourse = itemView.findViewById(R.id.btnEditCourse);
            btnDeleteCourse = itemView.findViewById(R.id.btnDeleteCourse);
        }
    }
}
