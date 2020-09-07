package com.lyl.test9.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.lyl.test9.Utils.UrlIDGeter;
import com.lyl.test9.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> datas = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private PagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        initDatas();
        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);
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
        datas.add("test1");
        datas.add("test2");
        datas.add("test3");
        datas.add("test1");
        datas.add("test2");
        datas.add("test3");
    }
    private void initViews() throws InterruptedException {
        //boolean hasnet = NetWorkChangeReceiver.hasnetwork;
        UrlIDGeter geter = new UrlIDGeter("https://covid-dashboard.aminer.cn/api/events/list?type=paper&page=1");
        //if(hasnet) {
            geter.Gets();
       // }
        //循环注入标签
        for (String tab : datas) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            //if(hasnet) {
                String tem;
                while ((tem = geter.getData("title")) == null) {
                    Thread.sleep(100);
                }
                s.add(tem);
           // } else
            //    s.add("aaa");
        }

        List<String> id_list = geter.getidl();

        //设置TabLayout点击事件
        for (int i = 0; i < 9; i++) {
            HomeSubFragment h = new HomeSubFragment();
            h.getS(s);
            fragments.add(h);
        }
        //tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) this);
        adapter = new PagerAdapter(getChildFragmentManager(), datas, fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}