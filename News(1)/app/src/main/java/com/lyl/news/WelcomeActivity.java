package com.lyl.news;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.lyl.news.Utils.DataGeter;
import com.lyl.news.Utils.DatabaseHelper;
import com.lyl.news.Utils.UrlGeter;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//TODO
public class WelcomeActivity extends AppCompatActivity {
    private ImageView welcomeImg = null;
    private DatabaseHelper helper;
    private final int listnum = 20;    //初次访问存入数据库的新闻数
    private final int shownum = 10;     //展示的新闻数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        welcomeImg = this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(0.3f, 1.0f);
        anima.setDuration(1000);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());
    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            welcomeImg.setBackgroundResource(R.drawable.welcome);
            new Thread(){
                @Override
                public void run(){
                    try {
                        inithome();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//
//                }
//            },1000);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            start();
            redirectTo(); // 动画结束后跳转到别的页面
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    private void start(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void redirectTo(){
        finish();
    }

    //TODO
    private void inithome() throws InterruptedException {
        UrlGeter geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type=news&page=2");
        geter.Gets();
        List<String> n_show_list;
        List<String> n_title_list = new ArrayList<>();
        List<String> n_date_list = new ArrayList<>();
        List<String> n_content_list = new ArrayList<>();
        List<String> n_source_list = new ArrayList<>();
        //获取新闻标题
        for (int i = 0; i < listnum; i++){
            String tem;
            while ((tem = geter.getData("title")) == null) {
                Thread.sleep(100);
            }
            n_title_list.add(tem);
        }
        //获取新闻的时间
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_date_list.add(geter.getData("date"));
        }
        //获取新闻的内容
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_content_list.add(geter.getData("content"));
        }
        //获取新闻的来源
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            n_source_list.add(geter.getData("source"));
        }
        //将list信息存入数据库
        helper = new DatabaseHelper(this, "NewsDatabase.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from news");
        db = helper.getWritableDatabase();
        for(int i=listnum-1;i>=0;i--){
            //组装数据
            ContentValues values = new ContentValues();
            values.put("title", n_title_list.get(i));
            values.put("date", n_date_list.get(i));
            values.put("content",n_content_list.get(i));
            values.put("source",n_source_list.get(i));
            //存入数据库
            db.insert("news",null,values);
        }
        db.close();
        n_show_list = n_title_list.subList(0,shownum);

        //获取paper信息
        geter = new UrlGeter("https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=2");
        geter.Gets();
        List<String> p_source_list = new ArrayList<>();
        List<String> p_content_list = new ArrayList<>();
        List<String> p_date_list = new ArrayList<>();
        List<String> p_title_list = new ArrayList<>();
        List<String> p_author_list = new ArrayList<>();
        //获取论文标题
        for (int i = 0; i < listnum; i++){
            String tem;
            while ((tem = geter.getData("title")) == null) {
                Thread.sleep(100);
            }
            p_title_list.add(tem);
        }
        //获取论文的时间
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_date_list.add(geter.getData("date"));
        }
        //获取论文的内容
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_content_list.add(geter.getData("content"));
        }
        //获取论文的来源
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            p_source_list.add(geter.getData("source"));
        }
        //获取论文的作者
        geter.setZero();
        for(int i = 0;i < listnum;i++){
            String tem = geter.getData("authors");
            p_author_list.add(tem);
        }
        //将list信息存入数据库
        helper = new DatabaseHelper(this, "NewsDatabase.db", null, 1);
        db = helper.getWritableDatabase();
        db.execSQL("delete from paper");
        db = helper.getWritableDatabase();
        for(int i=listnum-1;i>=0;i--){
            //组装数据
            ContentValues values = new ContentValues();
            values.put("title", p_title_list.get(i));
            values.put("date", p_date_list.get(i));
            values.put("content",p_content_list.get(i));
            values.put("source",p_source_list.get(i));
            values.put("author",p_author_list.get(i));
            //存入数据库
            db.insert("paper",null,values);
        }
        db.close();

        for(String i: n_title_list){
            System.out.println("title: "+i);
        }

        //获取疫情数据
        System.out.println("get Data!!!!!!!!!");
        DataGeter d_geter = new DataGeter();
        d_geter.Gets();
        while(d_geter.getRegions().size() == 0){
            Thread.sleep(1000);
        }
        List<String> d_regions = new ArrayList<>(d_geter.getRegions());
        List<String> d_begins = new ArrayList<>(d_geter.getBegins());
        List<String> d_datas = new ArrayList<>(d_geter.getDatas());
    }
}

