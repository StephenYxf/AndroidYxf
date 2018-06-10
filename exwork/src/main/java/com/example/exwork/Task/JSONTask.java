package com.example.exwork.Task;

/**
 * Created by Administrator on 2018/4/26.
 * 用于下载JSON数据，返回字符串
 */

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JSONTask extends AsyncTask<String,Void,String> {
    CallBack callBack;

    public JSONTask(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(String... params) {
        String result=null;
        //实例化
        OkHttpClient client=new OkHttpClient();
        //创建一个request对象用于发送http请求
        Request request=new Request.Builder()
                .url(params[0]).build();
        try {
            //发送请求 获取服务器返回数据
            Response response=client.newCall(request).execute();
            result=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(callBack!=null){
            callBack.getData(result);
        }
    }

    public interface CallBack{
        void getData(String result);
    }
}
