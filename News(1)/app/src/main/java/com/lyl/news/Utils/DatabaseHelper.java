package com.lyl.news.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    public static final String CREATE_NEWS = "create table news ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "title text,"
            + "date text,"
            + "content text,"
            + "source text)";
    public static final String CREATE_PAPERS = "create table paper ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "title text,"
            + "date text,"
            + "content text,"
            + "author text,"
            + "source text)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NEWS);
        sqLiteDatabase.execSQL(CREATE_PAPERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}