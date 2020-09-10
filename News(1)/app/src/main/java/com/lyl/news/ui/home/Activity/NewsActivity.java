package com.lyl.news.ui.home.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lyl.news.R;
import com.lyl.news.Utils.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;

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
        String s_label = intent.getStringExtra("label");

        helper = new DatabaseHelper(NewsActivity.this, "NewsDatabase.db", null, 1);
        SQLiteDatabase dbr = helper.getReadableDatabase();
        Cursor cursor = dbr.rawQuery("select * from "+s_label+" where title=?", new String[]{s_title});
        if(cursor.moveToFirst()) {
            String s_date = cursor.getString(cursor.getColumnIndex("date"));
            String s_content = cursor.getString(cursor.getColumnIndex("content"));
            String s_source;
            if(s_label.equals("news"))
                s_source = cursor.getString(cursor.getColumnIndex("source"));
            else
                s_source = cursor.getString(cursor.getColumnIndex("author"));
            title.setText(s_title);
            date.setText(s_date);
            source.setText(s_source);
            content.setText(s_content);
        }
        dbr.close();
    }
}
