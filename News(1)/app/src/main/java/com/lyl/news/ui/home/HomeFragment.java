package com.lyl.news.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyl.news.R;
import com.lyl.news.Utils.DataGeter;
import com.lyl.news.Utils.DatabaseHelper;
import com.lyl.news.Utils.UrlGeter;
import com.lyl.news.ui.home.Activity.EditActivity;
import com.lyl.news.ui.home.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private Button button;
    private ViewPager viewPager;
    private static List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;
    private TextView search_tv;
    public static boolean flag = false;
    private DatabaseHelper helper;
    private final int listnum = 20;    //初次访问存入数据库的新闻数
    private final int shownum = 10;     //展示的新闻数

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initDatas();
        flag = false;
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
        button = root.findViewById(R.id.add_dle_btn);
        search_tv = root.findViewById(R.id.search_text);
        search_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
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
        if (!flag){
            datas.clear();
            SharedPreferences my_tags = getActivity().getSharedPreferences("my_tags",0);
            String tmp = my_tags.getString("tags","新闻-论文");
            if (!tmp.equals("")){
                String[] tmpList = tmp.split("-");
                for (int i = 0; i < tmpList.length; i++){
                    datas.add(tmpList[i]);
                }
            }
        }
    }
    private void  initViews() throws InterruptedException {
        //TODO
        //从数据库获取新闻title信息
        List<String> n_show_list = new ArrayList<>();
        helper = new DatabaseHelper(getActivity(), "NewsDatabase.db", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from news",null);
        int count = 0;
        if(cursor.moveToLast()){
            do{
                n_show_list.add(cursor.getString(cursor.getColumnIndex("title")));
            } while (cursor.moveToPrevious() && count++ < 10);
        }

        //从数据库获取论文title信息
        List<String> p_show_list = new ArrayList<>();
        cursor = db.rawQuery("select * from paper",null);
        count = 0;
        if(cursor.moveToLast()){
            do{
                p_show_list.add(cursor.getString(cursor.getColumnIndex("title")));
            } while (cursor.moveToPrevious() && count++ < 10);
        }
        //TODO over


        if (datas.size() > 4){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
        if (datas.size() == 0) {
            Toast.makeText(getContext(), "草泥马的，手尼玛被驴踩过非要删空？？？？？", Toast.LENGTH_SHORT).show();
        }

        //循环注入标签
        fragments.clear();
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
            HomeSubFragment h = new HomeSubFragment();
            if(tab.equals("新闻"))
                h.setS(n_show_list,"news");
            else
                h.setS(p_show_list,"paper");
            //h.initString(tab);
            fragments.add(h);
        }

        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}