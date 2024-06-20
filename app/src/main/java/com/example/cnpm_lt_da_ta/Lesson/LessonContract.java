package com.example.cnpm_lt_da_ta.Lesson;



import android.provider.BaseColumns;

public final class LessonContract {
    private LessonContract() {} // Để tránh khởi tạo đối tượng

    public static final class LessonEntry implements BaseColumns {
        public static final String TABLE_NAME = "lessons";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_CONTENT = "content";
    }
}