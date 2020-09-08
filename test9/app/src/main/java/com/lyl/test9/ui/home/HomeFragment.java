package com.lyl.test9.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyl.test9.R;
import com.lyl.test9.Utils.DatabaseHelper;
import com.lyl.test9.Utils.UrlGeter;
import com.lyl.test9.ui.home.Activity.EditActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.database.sqlite.SQLiteDatabase.deleteDatabase;


public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private Button button;
    private ViewPager viewPager;
    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private DatabaseHelper helper;
    private final int listnum = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initDatas();
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        button = root.findViewById(R.id.add_dle_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivityForResult(intent, 0);
                Toast.makeText(getActivity(),"good", Toast.LENGTH_SHORT).show();

            }
        });
        try {
            initViews();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return root;
    }

    private void initDatas() {
        datas.add("test1");
        datas.add("test2");
        datas.add("test3");
        datas.add("test4");
        datas.add("test5");
        datas.add("test6");
        datas.add("test7");
        datas.add("test8");
        datas.add("test9");
    }
    private void initViews() throws InterruptedException {
        UrlGeter geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type=news&page=5");
        geter.Gets();
        //循环注入标签
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }

        List<String> title_list = new ArrayList<>();
        List<String> id_list = new ArrayList<>();
        List<String> date_list = new ArrayList<>();
        List<String> content_list = new ArrayList<>();
        List<String> urls_list = new ArrayList<>();
        //获取新闻标题
        for (int i = 0; i < listnum; i++){
            String tem;
            while ((tem = geter.getData("title")) == null) {
                Thread.sleep(100);
            }
            title_list.add(tem);
        }
        //获取新闻的id
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            id_list.add(geter.getData("_id"));
        }
        //获取新闻的时间
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            date_list.add(geter.getData("date"));
        }
        //获取新闻的内容
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            content_list.add(geter.getData("content"));
        }
        //获取新闻的来源
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            urls_list.add(geter.getData("urls"));
        }

        //将list信息存入数据库
        helper = new DatabaseHelper(getActivity(), "Newsdb.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        for(int i=listnum-1;i>=0;i--){
            //组装数据
            ContentValues values = new ContentValues();
            values.put("news_title", title_list.get(i));
            values.put("news_id",id_list.get(i));
            values.put("news_date", date_list.get(i));
            values.put("news_content",content_list.get(i));
            values.put("news_sourse",urls_list.get(i));
            //存入数据库
            db.insert("News",null,values);
        }
        db.close();

        SQLiteDatabase dbr = helper.getReadableDatabase();
        Cursor cursor = dbr.rawQuery("select * from News", null);
        if(cursor.moveToFirst()){
            do {
                String title = cursor.getString(cursor.getColumnIndex("news_title"));
                String id = cursor.getString(cursor.getColumnIndex("news_id"));
                String date = cursor.getString(cursor.getColumnIndex("news_date"));
                String content = cursor.getString(cursor.getColumnIndex("news_content"));
                String sourse = cursor.getString(cursor.getColumnIndex("news_sourse"));
                System.out.print("title is: "+title);
                System.out.print("       id is: "+id);
                System.out.print("       date is: "+date);
                System.out.print("       content is: "+content);
                System.out.println("       sourse is: "+sourse);
            }while (cursor.moveToNext());
        }
        dbr.close();

        db = helper.getWritableDatabase();
        db.execSQL("delete from News");
        //设置TabLayout点击事件
        for (int i = 0; i < 9; i++) {
            HomeSubFragment h = new HomeSubFragment();
            h.getS(title_list);
            fragments.add(h);
        }
        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}