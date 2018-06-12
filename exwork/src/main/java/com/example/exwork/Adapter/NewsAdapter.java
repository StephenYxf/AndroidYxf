package com.example.exwork.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.exwork.R;
import com.example.exwork.Task.ImageTask;
import com.example.exwork.bean.News;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/4.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private int resourceID;//列表项布局的ID
    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        resourceID=resource;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        ViewHolder holder=new ViewHolder();
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            holder.newsTitle= view.findViewById(R.id.news_title);
            holder.newsContent = view.findViewById(R.id.news_content);
            holder.newsImage = view.findViewById(R.id.news_image);
            holder.newsData = view.findViewById(R.id.news_data);
            view.setTag(holder);
        }else{
            view = convertView;
            holder=(ViewHolder) view.getTag();
        }
        final Map<String,Bitmap> map=new HashMap<String,Bitmap>();
        final News news = getItem(position);
        holder.newsTitle.setText(news.getTitle());
        holder.newsContent.setText(news.getContent());
        holder.newsData.setText(news.getDate());
        final String imgUrl = news.getImgUrl();
        //http://litchiapi.jstv.com/
        if(map.get(imgUrl)!=null) {
            holder.newsImage.setImageBitmap(map.get(imgUrl));
        }else{
            final ViewHolder finalHolder = holder;
            //Log.d("imgUrl",imgUrl);
            new ImageTask(new ImageTask.CallBack() {
                @Override
                public void getData(Bitmap pic) {
                    finalHolder.newsImage.setImageBitmap(pic);
                    map.put(imgUrl,pic);
                }
            }).execute(imgUrl);
        }
        return view;
    }

    class ViewHolder{
        //        根据列表项中的控件来定义属性
        TextView newsTitle;
        TextView newsContent;
        ImageView newsImage;
        TextView newsData;
    }
}