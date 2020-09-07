package com.lyl.test9.ui.home.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyl.test9.MainActivity;
import com.lyl.test9.R;
import com.lyl.test9.ui.home.FlowLayout.FlowLayout;
import com.lyl.test9.ui.home.FlowLayout.TagAdapter;
import com.lyl.test9.ui.home.FlowLayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;


public class EditActivity extends AppCompatActivity {
    boolean flag = false;
    List<String> mVals = new ArrayList<>();
    List<String> unVal = new ArrayList<>();
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
                ArrayList bundlelist = new ArrayList();
                Bundle bundle = new Bundle();
                bundlelist.add(mVals);
                //bundlelist.add(unVal);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                bundle.putParcelableArrayList("list", bundlelist);
                intent.putExtras(bundle);
                setResult(0,intent);
                finish();
            }
        });

        final LayoutInflater mInflater = LayoutInflater.from(this);
        mFlowLayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        unFlowLayout = (TagFlowLayout) findViewById(R.id.un_flowlayout);

        unFlowLayout.setAdapter(new TagAdapter<String>(unVal) {
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
                System.out.println(position);
                String tmp = unVal.get(position);
                System.out.println(tmp);
                unVal.remove(position);
                mVals.add(tmp);
                mFlowLayout.onChanged();
                unFlowLayout.onChanged();
                //view.setVisibility(View.GONE);
                return true;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                System.out.println(position);
                String tmp = mVals.get(position);
                System.out.println(tmp);
                mVals.remove(position);
                unVal.add(tmp);
                unFlowLayout.onChanged();
                mFlowLayout.onChanged();
                view.setVisibility(View.GONE);
                return true;
            }
        });
    }
    void initVal(){
        if (!flag){
            mVals.add("A");
            mVals.add("B");
            mVals.add("C");
            mVals.add("D");
            flag = true;
        }
    }
}
