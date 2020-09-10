package com.lyl.test9.ui.home.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import com.lyl.test9.R;
import com.lyl.test9.Utils.DatabaseHelper;
import com.lyl.test9.Utils.UrlGeter;

import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {
    private DatabaseHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news);
        TextView title = findViewById(R.id.title);
        TextView date = findViewById(R.id.date);
        TextView source = findViewById(R.id.source);
        TextView content = findViewById(R.id.content);
        Intent intent = getIntent();
        String s_title = intent.getStringExtra("title");

        helper = new DatabaseHelper(NewsActivity.this, "NewsDatabase.db", null, 1);
        SQLiteDatabase dbr = helper.getReadableDatabase();
        Cursor cursor = dbr.rawQuery("select * from News where news_title=?", new String[]{s_title});
        if(cursor.moveToFirst()) {
            String s_date = cursor.getString(cursor.getColumnIndex("news_date"));
            String s_content = cursor.getString(cursor.getColumnIndex("news_content"));
            String s_source = cursor.getString(cursor.getColumnIndex("news_source"));
            title.setText(s_title);
            date.setText(s_date);
            source.setText(s_source);
            content.setText(s_content);
        }
        dbr.close();


    }
}
