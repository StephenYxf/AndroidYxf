package com.example.exwork.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.exwork.R;
import com.example.exwork.musicActivity;

public class MyService extends Service {
    MediaPlayer player=null;
//    MusictBroadcastReceiver receiver;//广播
    static int type=R.raw.girl;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();

        //动态注册广播接收器
        //receiver=new MusictBroadcastReceiver();
        //创建过滤器，并指定action，使之用于接收同action的广播
        //IntentFilter filter=new IntentFilter("com.example.musicplay.MusicTypeService");
        //注册广播接收器
        //registerReceiver(receiver,filter);
        player = MediaPlayer.create(MyService.this,type);//上下文   播放音乐的uri
        player.setLooping(true);
        player.start();
        Log.i("运行service",String.valueOf(type));

    }



    public class MusicControl extends Binder {//定义 遥控器 类  IBinder
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(receiver);
        player.stop();
    }
    public static class MusictBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            type = intent.getIntExtra("type",0);
            Log.i("传过来的type",type+"11111111111111111");
        }
    }
}
