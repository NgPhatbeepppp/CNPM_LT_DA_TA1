package com.example.cnpm_lt_da_ta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Thông tin cơ sở dữ liệu
    private static final String DATABASE_NAME = "language_learning.db";
    private static final int DATABASE_VERSION = 3;

    // Tên các bảng và cột
    public static final String TABLE_COURSE = "course";
    public static final String COLUMN_COURSE_ID = "id";
    public static final String COLUMN_COURSE_NAME = "name";
    public static final String COLUMN_COURSE_DESCRIPTION = "description";
    public static final String COLUMN_COURSE_IMAGE = "image";
    public static final String COLUMN_COURSE_POPULARITY = "popularity";
    public static final String COLUMN_COURSE_IS_NEW = "isNew";

    public static final String TABLE_FLASHCARDSET = "flashcard_set";
    public static final String COLUMN_FLASHCARDSET_ID = "id";
    public static final String COLUMN_FLASHCARDSET_NAME = "name";
    public static final String COLUMN_FLASHCARDSET_COURSE_ID = "courseId";

    public static final String TABLE_FLASHCARD = "flashcard";
    public static final String COLUMN_FLASHCARD_ID = "id";
    public static final String COLUMN_FLASHCARD_WORD = "word";
    public static final String COLUMN_FLASHCARD_MEANING = "meaning";
    public static final String COLUMN_FLASHCARD_PRONUNCIATION = "pronunciation";
    public static final String COLUMN_FLASHCARD_IMAGE = "image";
    public static final String COLUMN_FLASHCARD_SET_ID = "flashcardSetId";


    // Câu lệnh SQL tạo bảng
    private static final String CREATE_TABLE_COURSE =
            "CREATE TABLE " + TABLE_COURSE + "("
                    + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COURSE_NAME + " TEXT,"
                    + COLUMN_COURSE_DESCRIPTION + " TEXT,"
                    + COLUMN_COURSE_IMAGE + " TEXT,"
                    + COLUMN_COURSE_POPULARITY + " INTEGER,"
                    + COLUMN_COURSE_IS_NEW + " INTEGER"
                    + ")";

    private static final String CREATE_TABLE_FLASHCARDSET =
            "CREATE TABLE " + TABLE_FLASHCARDSET + "("
                    + COLUMN_FLASHCARDSET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FLASHCARDSET_NAME + " TEXT,"
                    + COLUMN_FLASHCARDSET_COURSE_ID + " INTEGER,"
                    + "FOREIGN KEY (" + COLUMN_FLASHCARDSET_COURSE_ID + ") REFERENCES " + TABLE_COURSE + "(" + COLUMN_COURSE_ID + ")"
                    + ")";

    private static final String CREATE_TABLE_FLASHCARD =
            "CREATE TABLE " + TABLE_FLASHCARD + "("
                    + COLUMN_FLASHCARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FLASHCARD_WORD + " TEXT,"
                    + COLUMN_FLASHCARD_MEANING + " TEXT,"
                    + COLUMN_FLASHCARD_PRONUNCIATION + " TEXT,"
                    + COLUMN_FLASHCARD_IMAGE + " TEXT,"
                    + COLUMN_FLASHCARD_SET_ID + " INTEGER,"
                    + "FOREIGN KEY (" + COLUMN_FLASHCARD_SET_ID + ") REFERENCES " + TABLE_FLASHCARDSET + "(" + COLUMN_FLASHCARDSET_ID + ")"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_FLASHCARDSET);
        db.execSQL(CREATE_TABLE_FLASHCARD);
        // Dữ liệu mẫu cho bảng Course
        String[] courseNames = {"Tiếng Anh Giao Tiếp", "Tiếng Anh Thương Mại", "Tiếng Anh Du Lịch", "Tiếng Anh Thi IELTS", "Tiếng Anh Thi TOEIC"};
        String[] courseDescriptions = {
                "Nâng cao kỹ năng giao tiếp tiếng Anh",
                "Tiếng Anh chuyên ngành kinh doanh",
                "Tiếng Anh giao tiếp khi đi du lịch",
                "Luyện thi IELTS hiệu quả",
                "Luyện thi TOEIC đạt điểm cao"
        };
        String[] courseImages = {"lesson", "lesson", "lesson", "lesson4", "lesson5"};

        for (int i = 0; i < courseNames.length; i++) {
            ContentValues courseValues = new ContentValues();
            courseValues.put(COLUMN_COURSE_NAME, courseNames[i]);
            courseValues.put(COLUMN_COURSE_DESCRIPTION, courseDescriptions[i]);
            courseValues.put(COLUMN_COURSE_IMAGE, courseImages[i]);
            courseValues.put(COLUMN_COURSE_POPULARITY, 80 + i * 5); // Giả sử độ phổ biến tăng dần
            courseValues.put(COLUMN_COURSE_IS_NEW, i % 2); // Các khóa học xen kẽ mới/cũ
            db.insert(TABLE_COURSE, null, courseValues);
        }

        // Dữ liệu mẫu cho bảng FlashcardSet
        String[] flashcardSetNames = {
                "Từ vựng cơ bản", "Ngữ pháp cơ bản", "Hội thoại giao tiếp",
                "Từ vựng nâng cao", "Thành ngữ tiếng Anh", "Từ vựng chuyên ngành kinh doanh",
                "Email và thư tín thương mại", "Từ vựng du lịch", "Hỏi đường và chỉ đường",
                "Từ vựng IELTS", "Bài mẫu IELTS Writing", "Từ vựng TOEIC", "Đọc hiểu TOEIC"
        };

        for (int i = 0; i < flashcardSetNames.length; i++) {
            ContentValues flashcardSetValues = new ContentValues();
            flashcardSetValues.put(COLUMN_FLASHCARDSET_NAME, flashcardSetNames[i]);
            flashcardSetValues.put(COLUMN_FLASHCARDSET_COURSE_ID, (i / 3) + 1); // Mỗi khóa học có 3 bộ flashcard
            db.insert(TABLE_FLASHCARDSET, null, flashcardSetValues);
        }

        // Dữ liệu mẫu cho bảng Flashcard (bạn có thể thêm nhiều hơn)
        String[] words = {"hello", "goodbye", "thank you", "please", "yes", "no"};
        String[] meanings = {"xin chào", "tạm biệt", "cảm ơn", "làm ơn", "có", "không"};
        String[] pronunciations = {"həˈloʊ", "ˌɡʊdˈbaɪ", "θæŋk juː", "pliːz", "jes", "noʊ"};
        String[] images = {"hello", "goodbye", null, null, null, null}; // Hoặc đường dẫn đến ảnh trong drawable nếu có

        for (int i = 0; i < words.length; i++) {
            ContentValues flashcardValues = new ContentValues();
            flashcardValues.put(COLUMN_FLASHCARD_WORD, words[i]);
            flashcardValues.put(COLUMN_FLASHCARD_MEANING, meanings[i]);
            flashcardValues.put(COLUMN_FLASHCARD_PRONUNCIATION, pronunciations[i]);
            flashcardValues.put(COLUMN_FLASHCARD_IMAGE, images[i]);
            flashcardValues.put(COLUMN_FLASHCARD_SET_ID, 1); // Thuộc bộ flashcard "Từ vựng cơ bản"
            db.insert(TABLE_FLASHCARD, null, flashcardValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 3) {
            // Nâng cấp từ phiên bản 2 lên 3: Thêm các cột popularity và isNew vào bảng Course
            db.execSQL("ALTER TABLE " + TABLE_COURSE + " ADD COLUMN " + COLUMN_COURSE_POPULARITY + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_COURSE + " ADD COLUMN " + COLUMN_COURSE_IS_NEW + " INTEGER DEFAULT 0");
        }

    }

}
