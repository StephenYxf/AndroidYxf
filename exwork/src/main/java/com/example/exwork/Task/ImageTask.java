package com.example.exwork.Task;

/**
 * Created by Administrator on 2018/4/26.
 * 下载网络图片，返回Bitmap
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ImageTask extends AsyncTask<String,Void,Bitmap> {
    CallBack callBack;

    public ImageTask(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
            OkHttpClient client=new OkHttpClient();

        Request request=null;
            try {
                request=new Request.Builder().url(params[0]).build();
            }catch (Exception e){
                Log.d("chucuole","======================");

            }

            //Log.i("ImageTask0:",request.toString());
            Bitmap pic=null;
            try {
                Response response=client.newCall(request).execute();
                byte[] temp=response.body().bytes();
                pic= BitmapFactory.decodeByteArray(temp,0,temp.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pic;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(callBack!=null){
            callBack.getData(bitmap);
        }
    }

    public interface CallBack{
        void getData(Bitmap pic);
    }
}
