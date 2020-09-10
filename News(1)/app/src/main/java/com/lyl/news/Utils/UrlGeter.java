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

public class UrlGeter {
    private String url;//向服务器请求的url.
    int index;
    JSONArray jsarray;

    public UrlGeter(String url){
        index = 0;
        this.url = url;
        jsarray = null;
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
                    jsarray = js.getJSONArray("data");

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

    public void setZero(){index = 0;}

    public String getData(String tag) {
        String ret = null;
        try {
            if(jsarray == null) return null;
            if(jsarray.getJSONObject(index) == null) return null;
            if(!tag.equals("authors"))
                ret = jsarray.getJSONObject(index).optString(tag);
            else{
                ret = "";
                JSONArray jsa = jsarray.getJSONObject(index).getJSONArray("authors");
                for(int i=0;i<jsa.length()-1;i++){
                    ret += jsa.getJSONObject(i).optString("name") + ",  ";
                }
                ret += jsa.getJSONObject(jsa.length()-1).optString("name");
            }
            index += 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }


}
