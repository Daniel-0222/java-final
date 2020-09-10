package com.lyl.news.ui.Charts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.lyl.news.R;

import java.util.ArrayList;
import java.util.List;

public class ChartsFragment extends Fragment {
    private TableView mTableView;
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_charts, container, false);
        mTableView = root.findViewById(R.id.tableView);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        float [] f = {0f, 5f, 10f, 15f, 20f, 25f, 30f};
        float [] f2 = {0f, 10f, 30f, 54f, 30f, 100f, 10f};
        data_list = new ArrayList<String>();
        data_list.add("北京");
        data_list.add("上海");
        data_list.add("广州");
        data_list.add("深圳");
        //适配器
        arr_adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner.setAdapter(arr_adapter);
        List<Float> f_list = new ArrayList<>();
        for (int i =0; i < 7; i++){
            f_list.add(f2[i]);
        }
        mTableView.setupCoordinator("日", "人", f);
        // 添加曲线, 确保纵坐标的数值位数相等
        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_orange_light), true, f_list);
//        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_green_light), false,
//                0f, 30f, 20f, 20f, 46f, 25f, 5f);
//        mTableView.addWave(ContextCompat.getColor(getContext(), android.R.color.holo_purple), false,
//                0f, 30f, 20f, 50f);
//        mTableView.addWave(Color.parseColor("#8596dee9"), true,
//                0f, 15f, 10f, 10f, 40f, 20f, 5000f);R
        return root;
    }
}