package com.example.cnpm_lt_da_ta.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;

import com.example.cnpm_lt_da_ta.DatabaseHelper;
import com.example.cnpm_lt_da_ta.R;
import com.example.cnpm_lt_da_ta.Adapter.NewsAdapter;
import com.example.cnpm_lt_da_ta.model.Course;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Course> courseList;  // Sử dụng đúng biến courseList thay vì newsList
    private DatabaseHelper dbHelper; // Khai báo dbHelper

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseList = new ArrayList<>();
        adapter = new NewsAdapter(courseList);
        recyclerView.setAdapter(adapter);

        dbHelper = new DatabaseHelper(getContext()); // Khởi tạo dbHelper
        loadNews(); // Load dữ liệu từ SQLite

        return view;
    }

    private void loadNews() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(DatabaseHelper.TABLE_NEWS,
                    new String[]{DatabaseHelper.COLUMN_NEWS_MESSAGE},
                    null, null, null, null,
                    DatabaseHelper.COLUMN_NEWS_TIMESTAMP + " DESC");

            courseList.clear(); // Đổi newsList thành courseList

            while (cursor.moveToNext()) {
                courseList.add(new Course(
                        0, // ID giả định vì bảng news có thể không có trường ID
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NEWS_MESSAGE)),
                        "", "", 0, 0
                ));
            }

            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    }
