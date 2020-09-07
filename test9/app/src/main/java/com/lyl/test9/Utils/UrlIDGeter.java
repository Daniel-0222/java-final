package com.lyl.test9.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UrlIDGeter {
    private String url;//向服务器请求的url.
    String data;
    int start;
    List<String> id_list;
    List<String> date_list;

    public UrlIDGeter(String url){
        start = 0;
        this.url = url;
        id_list = new ArrayList<>();
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

                    final StringBuffer sb = new StringBuffer();//把获取的数据不断存放到StringBuffer中；
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));//使用reader向输入流中读取数据，并不断存放到StringBuffer中；
                    String line;
                    while ((line = reader.readLine()) != null) {//只要还没有读取完，就不断读取；
                        sb.append(line);//在StringBuffer中添加；
                    }
                    data = sb.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void setZero(){ start = 0; }

    public String getData(String tag){
        if(data != null) {
            int ind = data.indexOf(tag,start);
            if(ind == -1) return null;
            int leftindex = ind + tag.length() + 2;
            int rightindex = data.indexOf('\"', leftindex);
            start = rightindex;
            return data.substring(leftindex, rightindex);
        }else {
            return null;
        }
    }

    public List<String> getidl(){
        return id_list;
    }
}
