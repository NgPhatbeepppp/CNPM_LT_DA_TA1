package com.example.cnpm_lt_da_ta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LessonAdapter extends ArrayAdapter<Lesson> {

    public LessonAdapter(@NonNull Context context, @NonNull List<Lesson> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.lesson_list_item, parent, false); // Thay bằng layout item của bạn
        }

        Lesson currentLesson = getItem(position);

        TextView lessonTitleTextView = listItemView.findViewById(R.id.lesson_title);
        lessonTitleTextView.setText(currentLesson.getTitle());

        TextView lessonTypeTextView = listItemView.findViewById(R.id.lesson_type);
        lessonTypeTextView.setText(currentLesson.getType());

        return listItemView;
    }
}