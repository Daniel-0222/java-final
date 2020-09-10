package com.lyl.news.ui.home.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyl.news.MainActivity;
import com.lyl.news.R;
import com.lyl.news.ui.home.FlowLayout.FlowLayout;
import com.lyl.news.ui.home.FlowLayout.TagAdapter;
import com.lyl.news.ui.home.FlowLayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;


public class EditActivity extends AppCompatActivity {
    boolean flag = false;
    List<String> mVals = new ArrayList<>();
    List<String> unVals = new ArrayList<>();
    private TagFlowLayout mFlowLayout,unFlowLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVal();
        Toast.makeText(this,"good", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_edit);
        Button button = findViewById(R.id.tab_edit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ArrayList bundlelist = new ArrayList();
//                Bundle bundle = new Bundle();
//                bundlelist.add(mVals);
                //bundlelist.add(unVal);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                StringBuilder tmp = new StringBuilder();
                StringBuilder untmp = new StringBuilder();

                int t = mVals.size();
                for (int i = 0; i < t; i++){
                    if (i == t-1){
                        tmp.append(mVals.get(i));
                    }else {
                        tmp.append(mVals.get(i));
                        tmp.append("-");
                    }
                }

                SharedPreferences my_tags = getSharedPreferences("my_tags",0);
                SharedPreferences.Editor editor = my_tags.edit();
                editor.putString("tags", tmp.toString());

                int unt =unVals.size();
                for (int i = 0; i < unt; i++){
                    if (i == unt-1){
                        untmp.append(unVals.get(i));
                    }else {
                        untmp.append(unVals.get(i));
                        untmp.append("-");
                    }
                }
                editor.putString("untags", untmp.toString());
                editor.apply();
                setResult(0,intent);
                finish();
            }
        });

        final LayoutInflater mInflater = LayoutInflater.from(this);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        unFlowLayout = (TagFlowLayout) findViewById(R.id.un_flowlayout);
        unFlowLayout.setAdapter(new TagAdapter<String>(unVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.single_textview,
                        unFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });

        mFlowLayout.setAdapter(new TagAdapter<String>(mVals) {

            @Override
            public View getView(FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.single_textview,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
            }


        });

        unFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                System.out.println("in");
                System.out.println(position);
                String tmp = unVals.get(position);
                System.out.println(tmp);
                unVals.remove(position);
                mVals.add(tmp);
                mFlowLayout.onChanged();
                unFlowLayout.onChanged();
                //view.setVisibility(View.GONE);
                return true;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                System.out.println("inm");
                System.out.println(position);
                String tmp = mVals.get(position);
                System.out.println(tmp);
                mVals.remove(position);
                unVals.add(tmp);
                unFlowLayout.onChanged();
                mFlowLayout.onChanged();
                view.setVisibility(View.GONE);
                return true;
            }
        });
    }
    void initVal(){
        if (!flag){
            SharedPreferences my_tags = getSharedPreferences("my_tags",0);
            String tmp = my_tags.getString("tags","a");
            String untmp = my_tags.getString("untags","");

            if (tmp != null && !tmp.equals("")){
                String[] tmpList = tmp.split("-");
                for (int i = 0; i < tmpList.length; i++){
                    mVals.add(tmpList[i]);
                }
            }

            if (untmp!=null&&!untmp.equals("")){
                String[] untmpList = untmp.split("-");
                for (int i = 0; i < untmpList.length; i++){
                    unVals.add(untmpList[i]);
                }
            }
            flag = true;


        }
    }
}
