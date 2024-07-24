package com.example.cnpm_lt_da_ta.DAO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cnpm_lt_da_ta.Course.Dictionary;
import com.example.cnpm_lt_da_ta.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DictionaryDAO {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DictionaryDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Dictionary> searchWords(String query) {
        List<Dictionary> words = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_DICTIONARY_WORD + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};
        Cursor cursor = db.query(DatabaseHelper.TABLE_DICTIONARY, null, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            Dictionary word = new Dictionary(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_WORD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_MEANING)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_PRONUNCIATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_TYPE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DICTIONARY_EXAMPLE))
            );
            words.add(word);
        }
        cursor.close();
        return words;
    }
}

