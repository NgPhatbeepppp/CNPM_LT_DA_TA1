package com.example.cnpm_lt_da_ta;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
public class DatabaseHelper extends SQLiteOpenHelper {

    // Thông tin cơ sở dữ liệu
    private static final String DATABASE_NAME = "language_learning.db";
    private static final int DATABASE_VERSION = 5;
    private Context context;
    public static final String TABLE_FLASHCARDSET_FLASHCARD = "flashcard_set_flashcard";
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
    public static final String TABLE_DICTIONARY = "dictionary";
    public static final String COLUMN_DICTIONARY_ID = "id";
    public static final String COLUMN_DICTIONARY_WORD = "word";
    public static final String COLUMN_DICTIONARY_MEANING = "meaning";
    public static final String COLUMN_DICTIONARY_PRONUNCIATION = "pronunciation";
    public static final String COLUMN_DICTIONARY_TYPE = "type";
    public static final String COLUMN_DICTIONARY_EXAMPLE = "example";


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
    private static final String CREATE_TABLE_DICTIONARY =
            "CREATE TABLE " + TABLE_DICTIONARY + "(" +
                    COLUMN_DICTIONARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DICTIONARY_WORD + " TEXT, " +
                    COLUMN_DICTIONARY_MEANING + " TEXT, " +
                    COLUMN_DICTIONARY_PRONUNCIATION + " TEXT, " +
                    COLUMN_DICTIONARY_TYPE + " TEXT, " +
                    COLUMN_DICTIONARY_EXAMPLE + " TEXT" +
                    ")";
    private static final String CREATE_TABLE_FLASHCARDSET_FLASHCARD =
            "CREATE TABLE " + TABLE_FLASHCARDSET_FLASHCARD + "("
                    + COLUMN_FLASHCARDSET_ID + " INTEGER,"
                    + COLUMN_FLASHCARD_ID + " INTEGER,"
                    + "PRIMARY KEY (" + COLUMN_FLASHCARDSET_ID + ", " + COLUMN_FLASHCARD_ID + "),"
                    + "FOREIGN KEY (" + COLUMN_FLASHCARDSET_ID + ") REFERENCES " + TABLE_FLASHCARDSET + "(" + COLUMN_FLASHCARDSET_ID + "),"
                    + "FOREIGN KEY (" + COLUMN_FLASHCARD_ID + ") REFERENCES " + TABLE_FLASHCARD + "(" + COLUMN_FLASHCARD_ID + ")"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_FLASHCARDSET);
        db.execSQL(CREATE_TABLE_FLASHCARD);
        db.execSQL(CREATE_TABLE_DICTIONARY); // Tạo bảng dictionary
        db.execSQL(CREATE_TABLE_FLASHCARDSET_FLASHCARD);
        try {
            insertDictionaryDataFromAssets(db);
        } catch (IOException e) {
            e.printStackTrace();
        }
        insertSampleCourseData(db);
        insertSampleFlashcardSetData(db);
        insertSampleFlashcardData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) { // Kiểm tra xem có cần cập nhật dữ liệu không
            // Xóa dữ liệu cũ (nếu cần)
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARDSET);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTIONARY );
            db.execSQL(CREATE_TABLE_FLASHCARDSET_FLASHCARD);
            onCreate(db);
        }
    }

    private void insertDictionaryDataFromAssets(SQLiteDatabase db) throws IOException {
        InputStream is = context.getAssets().open("anhviet109K.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        String currentWord = ""; // Biến lưu trữ từ vựng hiện tại
        String currentPronunciation = ""; // Biến lưu trữ phiên âm hiện tại
        StringBuilder currentMeaning = new StringBuilder(); // Biến lưu trữ nghĩa hiện tại
        String currentType = "";
        String currentExample = "";
        while ((line = reader.readLine()) != null) {
            if (line.startsWith("@")) {
                // Bắt đầu một từ mới, lưu từ và phiên âm (nếu có)
                String[] parts = line.substring(1).split("/"); // Loại bỏ "@" và tách từ/phiên âm
                currentWord = parts[0].trim();
                currentPronunciation = (parts.length > 1) ? parts[1].trim() : null;
                currentMeaning.setLength(0); // Reset StringBuilder cho nghĩa mới
            } else if (line.startsWith("-")) {
                // Đây là dòng nghĩa tiếng Việt
                currentMeaning.append(line.substring(1).trim()).append("\n"); // Thêm nghĩa vào StringBuilder
            } else if (line.startsWith("*")) {
                // Đây là dòng loại từ
                currentType = line.substring(1).trim();
            } else if (line.startsWith("=")) {
                // Đây là dòng ví dụ, bạn có thể xử lý nếu cần
                currentExample = line.substring(1).trim();
            } else if (line.isEmpty()) {
                // Dòng trống, kết thúc một từ
                // Chèn vào cơ sở dữ liệu
                ContentValues values = new ContentValues();
                values.put(COLUMN_DICTIONARY_WORD, currentWord);
                values.put(COLUMN_DICTIONARY_MEANING, currentMeaning.toString());
                values.put(COLUMN_DICTIONARY_PRONUNCIATION, currentPronunciation);
                values.put(COLUMN_DICTIONARY_TYPE, currentType);
                values.put(COLUMN_DICTIONARY_EXAMPLE, currentExample);
                db.insert(TABLE_DICTIONARY, null, values);
            }
        }
        reader.close();
    }
    private void insertSampleCourseData(SQLiteDatabase db) {
        String[] courseNames = {"Tiếng Anh Giao Tiếp", "Tiếng Anh Thương Mại", "Tiếng Anh Du Lịch",
                "Tiếng Anh Thi IELTS", "Tiếng Anh Thi TOEIC"};
        String[] courseDescriptions = {
                "Nâng cao kỹ năng giao tiếp tiếng Anh hàng ngày.",
                "Tiếng Anh chuyên ngành kinh doanh và môi trường làm việc.",
                "Tiếng Anh giao tiếp khi đi du lịch và khám phá thế giới.",
                "Luyện thi IELTS hiệu quả với các bài học và mẹo làm bài.",
                "Luyện thi TOEIC đạt điểm cao với các bài tập và chiến lược làm bài."
        };
        String[] courseImages = {"lesson", "lesson2", "lesson3", "lesson4", "lesson5"};

        for (int i = 0; i < courseNames.length; i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COURSE_NAME, courseNames[i]);
            values.put(COLUMN_COURSE_DESCRIPTION, courseDescriptions[i]);
            values.put(COLUMN_COURSE_IMAGE, courseImages[i]);
            values.put(COLUMN_COURSE_POPULARITY, 80 + i * 5);
            values.put(COLUMN_COURSE_IS_NEW, i < 3 ? 1 : 0); // Đánh dấu 3 khóa đầu là mới
            db.insert(TABLE_COURSE, null, values);
        }
    }

    private void insertSampleFlashcardSetData(SQLiteDatabase db) {
        String[][] flashcardSetNames = {
                {"Từ vựng cơ bản", "Ngữ pháp cơ bản", "Hội thoại giao tiếp"},
                {"Từ vựng kinh doanh", "Email và thư tín", "Thuyết trình"},
                {"Từ vựng du lịch", "Hỏi đường và chỉ đường", "Đặt phòng và dịch vụ"},
                {"Từ vựng IELTS", "Ngữ pháp IELTS", "Bài mẫu IELTS Writing"},
                {"Từ vựng TOEIC", "Ngữ pháp TOEIC", "Đọc hiểu TOEIC"}
        };

        int courseId = 1;
        for (String[] setNames : flashcardSetNames) {
            for (String setName : setNames) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_FLASHCARDSET_NAME, setName);
                values.put(COLUMN_FLASHCARDSET_COURSE_ID, courseId);
                db.insert(TABLE_FLASHCARDSET, null, values);
            }
            courseId++;
        }
    }

    private void insertSampleFlashcardData(SQLiteDatabase db) {
        String[][][] flashcardData = {
                { // Tiếng Anh Giao Tiếp
                        {"hello", "xin chào", "həˈloʊ"},
                        {"goodbye", "tạm biệt", "ˌɡʊdˈbaɪ"},
                        {"thank you", "cảm ơn", "θæŋk juː"},
                        {"please", "làm ơn", "pliːz"}
                },
                { // Tiếng Anh Thương Mại
                        {"meeting", "cuộc họp", "ˈmiːtɪŋ"},
                        {"contract", "hợp đồng", "ˈkɑːntrækt"},
                        {"negotiation", "đàm phán", "nɪˌɡoʊʃiˈeɪʃn"},
                        {"client", "khách hàng", "ˈklaɪənt"}
                },
                { // Tiếng Anh Du Lịch
                        {"hotel", "khách sạn", "hoʊˈtel"},
                        {"flight", "chuyến bay", "flaɪt"},
                        {"passport", "hộ chiếu", "ˈpæspɔːrt"},
                        {"luggage", "hành lý", "ˈlʌɡɪdʒ"}
                },
                { // Tiếng Anh Thi IELTS
                        {"essay", "bài luận", "ˈeseɪ"},
                        {"vocabulary", "từ vựng", "voʊˈkæbjəleri"},
                        {"grammar", "ngữ pháp", "ˈɡræmər"},
                        {"pronunciation", "phát âm", "prəˌnʌnsiˈeɪʃn"}
                },
                { // Tiếng Anh Thi TOEIC
                        {"listening", "nghe", "ˈlɪsənɪŋ"},
                        {"reading", "đọc", "ˈriːdɪŋ"},
                        {"part", "phần", "pɑːrt"},
                        {"score", "điểm số", "skɔːr"}
                }
        };

        int flashcardSetId = 1;
        for (String[][] courseFlashcards : flashcardData) {
            for (String[] flashcard : courseFlashcards) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_FLASHCARD_WORD, flashcard[0]);
                values.put(COLUMN_FLASHCARD_MEANING, flashcard[1]);
                values.put(COLUMN_FLASHCARD_PRONUNCIATION, flashcard[2]);
                values.put(COLUMN_FLASHCARD_SET_ID, flashcardSetId);
                db.insert(TABLE_FLASHCARD, null, values);
            }
            flashcardSetId++;
        }
    }
}
