package com.example.exwork;



import com.example.exwork.bean.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/26.
 * 解析json格式的数据
 */

public class JSONParser {
    public static ArrayList<News> MyJsonParer(String data){
        ArrayList<News> result = new ArrayList<News>();
        try {
            JSONObject object1 = new JSONObject(data);

            JSONArray jsonArray1 = object1.getJSONArray("data");
            for(int i = 0 ; i <jsonArray1.length();i++){
                JSONObject object3 = jsonArray1.getJSONObject(i);
                String title = object3.getString("text");
                String content = object3.getString("name");//作者
                String url = object3.getString("profile_image");//image0    profile_image
                String date = object3.getString("created_at");//创建时间
                String themetype = object3.getString("theme_name");//标签
                News news = new News();
                news.setTitle(title);
                news.setContent(content);
                news.setImgUrl(url);
                news.setDate(date);
                news.setThemetype(themetype);
                result.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
