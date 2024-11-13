package com.tdtu.studentmanagement.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_USER_IMAGES = "CREATE TABLE UserImages (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "email TEXT NOT NULL, " +
            "image BLOB" +
            ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserImages");
        onCreate(db);
    }

    public byte[] getImageByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("UserImages", new String[]{"image"}, "email = ?", new String[]{email}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
            cursor.close();
            return image;
        }
        if (cursor != null) cursor.close();
        return null;
    }

}
