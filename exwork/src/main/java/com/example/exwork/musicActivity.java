package com.example.exwork;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exwork.Service.MusicService;

import java.text.SimpleDateFormat;

public class musicActivity extends AppCompatActivity {
    int type = R.raw.musicjj;//歌曲类型 默认这首
    MusicService.MusicControl control;//遥控器
    String musicType;
    SeekBar sb;
    Button btnMusic1,btnMusic2,btnMusic3;
    MusicBroadcastReceiver receiver;//广播
    TextView maxText,currentText,fanhui;
    ServiceConnection connection=new ServiceConnection() {//活动和服务的纽带
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            control= (MusicService.MusicControl) iBinder;//获取服务的遥控器
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music);
        //动态注册广播接收器
        receiver=new MusicBroadcastReceiver();
        //创建过滤器，并指定action，使之用于接收同action的广播
        IntentFilter filter=new IntentFilter("com.example.musicplay.MusicService");
        //注册广播接收器
        registerReceiver(receiver,filter);

        final Intent intent = new Intent(this,MusicService.class);
        bindService(intent,connection,BIND_AUTO_CREATE);//绑定服务
        sb=findViewById(R.id.seek_bar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                control.musicSeekTo(seekBar.getProgress());
            }
        });
        btnMusic1 = findViewById(R.id.music1);
        btnMusic2 = findViewById(R.id.music2);
        btnMusic3 = findViewById(R.id.music3);


        fanhui=findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        maxText=findViewById(R.id.max);
        currentText =findViewById(R.id.current);

    }
    public void OnType(View view){//切歌
        switch (view.getId()){
            case R.id.music1:
                control.musicStop();
                type = R.raw.musicjj;//那些你很冒险的梦
                sendMusicTypeBroadTask();

                Log.i("type",type+"那些你很冒险的梦");
                break;
            case R.id.music2:
                control.musicStop();
                type = R.raw.girl;//挥着翅膀的女孩
                sendMusicTypeBroadTask();
                Log.i("type",type+"挥着翅膀的女孩");
                break;
            case R.id.music3:
                control.musicStop();
                type = R.raw.oaoa;//OAOA
                sendMusicTypeBroadTask();
                Log.i("type",type+"OAOA");
                break;
        }
    }
    public void OnPlay(View view){//播放 暂停 停止
        switch (view.getId()){
            case R.id.play:
                Log.i("typeplay",String.valueOf(type));
                control.musicPlay(sb.getProgress(),type);//得到当前进度
                sendMusicTypeBroadTask();
                break;
            case R.id.pause:
                control.musicPause();
                sendMusicTypeBroadTask();
                break;
            case R.id.stop:
                control.musicStop();
                sendMusicTypeBroadTask();
                break;
        }

    }
    public void sendMusicTypeBroadTask(){
            Intent musicIntent = new Intent("com.example.musicplay.MusicTypeService");
            musicIntent.putExtra("type",type);
            sendBroadcast(musicIntent);//发送广播
            Log.i("发送广播musicActivity:",String.valueOf(type));
    }
    public class MusicBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int max = intent.getIntExtra("max",0);
            int current =intent.getIntExtra("current",0);
            sb.setMax(max);
            sb.setProgress(current);
            SimpleDateFormat sdf =new SimpleDateFormat("mm:ss");
            maxText.setText(sdf.format(max));
            currentText.setText(sdf.format(current));
        }
    }
    protected void onDestroy() {
        // TODO 自动生成的方法存根

        Log.i("ondestroy","musicActivity");
        super.onDestroy();
        unregisterReceiver(receiver);
        unbindService(connection);

    }


}
