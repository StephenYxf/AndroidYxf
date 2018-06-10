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
//            params[0] = params[0].replaceAll(" ", "%20");
//            if(!params[0].equals("")){
//                params[0]="http://wimg.spriteapp.cn/profile/large/2017/10/10/59dca3c409d5d_mini.jpg";
////                String httpstr = params[0].substring(0,7);
////                if(!httpstr.equals("http://")){
////                    params[0]="http://"+params[0];
////                }
//            }

            Request request=new Request.Builder().url(params[0]).build();
            Log.i("ImageTask0:",request.toString());
            Bitmap pic=null;
            try {
                Log.i("ImageTask1:",request.toString());
                Response response=client.newCall(request).execute();
                Log.i("ImageTask2:",request.toString());
                byte[] temp=response.body().bytes();
                Log.i("ImageTask3:",request.toString());
                pic= BitmapFactory.decodeByteArray(temp,0,temp.length);
                Log.i("ImageTask4:",request.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        Log.i("pic:",pic.toString());
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
