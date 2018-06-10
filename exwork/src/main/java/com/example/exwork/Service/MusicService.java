package com.example.exwork.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.exwork.R;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    MediaPlayer player;
    Timer timer;//定时器
    TimerTask timerTask;//计划任务

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MusicService--------","onCreate");
        //player = MediaPlayer.create(this,R.raw.dream);//上下文   播放音乐的uri
    }

    public MusicService() {
    }

    public class MusicControl extends Binder {//定义 遥控器 类  IBinder
        int max;
        int current;
        public void musicPlay(int position){
            if(player==null){//初次播放音乐
                player = MediaPlayer.create(MusicService.this,R.raw.musicjj);//上下文   播放音乐的uri
                player.setLooping(true);//循环
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//播放结束监听
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        stopProgress();
                    }
                });
            }else if(!player.isPlaying()){//没有正在播放就是暂停  暂停了播放  或者拖动进度条播放
                player.seekTo(position);//定位到哪里播放
            }

            player.start();
            //player.setLooping(true);//循环
            updateProgress();
        }
        public void musicSeekTo(int position){
            if(player!=null){
                player.seekTo(position);
            }
        }
        public void musicStop(){
            if(player!=null){
                player.stop();
                stopProgress();
                player.release();//不播放 释放资源
                player=null;
            }
            stopProgress();

        }
        public void musicPause(){
            if(player!=null && player.isPlaying()){
                player.pause();
                pauseProgress();
            }

        }

        public void updateProgress() {
            timer = new Timer();
            max = player.getDuration();//获取总时长
            timerTask = new TimerTask() {//自定义计划任务
                @Override
                public void run() {
                    current=player.getCurrentPosition();
                    sendMusicBroadTask();
                }
            };
            timer.schedule(timerTask,0,1000);//延迟0   间隔1000ms
        }
        public void pauseProgress() {
            if(timerTask!=null){
                timerTask.cancel();
            }
            if(timer!=null){
                timer.cancel();
            }
        }
        public void stopProgress() {
            if(timerTask!=null){
                timerTask.cancel();
            }
            if(timer!=null){
                timer.cancel();
            }
            current=0;
            sendMusicBroadTask();
        }
        public void sendMusicBroadTask(){
            Intent musicIntent = new Intent("com.example.musicplay.MusicService");
            musicIntent.putExtra("max",max);//总时长
            musicIntent.putExtra("current",current);//当前时间
            sendBroadcast(musicIntent);//发送广播
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("MusicService--------","onBind");
        return new MusicControl();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //player.start();
        Log.i("MusicService--------","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("MusicService--------","onDestroy");
        if(player!=null){
            player.stop();
            player.release();
            player=null;
        }


    }
}
