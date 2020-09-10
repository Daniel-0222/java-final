package com.lyl.news.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataGeter {
    private String url;//向服务器请求的url.
    int index;
    List<String> regions;
    List<String> begins;
    List<String> datas;

    public DataGeter(){
        index = 0;
        url = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
        regions = new ArrayList<>();
        begins = new ArrayList<>();
        datas = new ArrayList<>();
    }

    public void Gets() {
        new Thread(){
            @Override
            public void run(){
                try {
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();//与服务器建立连接；
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");//设置请求方式为GET

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line = reader.readLine();
                    JSONObject js = new JSONObject(line);
                    Iterator<String> keys = js.keys();
                    while(keys.hasNext()){
                        String key = keys.next();
                        regions.add(key);
                        JSONObject js1 = js.getJSONObject(key);
                        begins.add(js1.getString("begin"));
                        JSONArray jsarray = js1.getJSONArray("data");
                        JSONArray dataarray = jsarray.getJSONArray(jsarray.length()-1);
                        String data = "";
                        data += dataarray.optString(0) + ",";
                        data += dataarray.optString(2) + ",";
                        data += dataarray.optString(3);
                        datas.add(data);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public List<String> getRegions(){ return regions; }
    public List<String> getBegins(){ return begins; }
    public List<String> getDatas(){ return datas; }
}
