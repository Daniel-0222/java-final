package com.lyl.test9.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    public static final String CREATE_NEWS = "create table News ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "news_title text,"
            + "news_date text,"
            + "news_content text,"
            + "news_id text,"
            + "news_sourse text)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_NEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}