package com.example.exwork;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.exwork.Service.MusicService;

import java.text.SimpleDateFormat;

public class musicActivity extends AppCompatActivity {
    MusicService.MusicControl control;//遥控器
    SeekBar sb;
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

        fanhui=findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(musicActivity.this,Main2Activity.class);
                String sname = getIntent().getStringExtra("uname");
                intent1.putExtra("user",sname);
                startActivity(intent1);
            }
        });


        maxText=findViewById(R.id.max);
        currentText =findViewById(R.id.current);
        //动态注册广播接收器
        MusicBroadcastReceiver receiver=new MusicBroadcastReceiver();
        //创建过滤器，并指定action，使之用于接收同action的广播
        IntentFilter filter=new IntentFilter("com.example.musicplay.MusicService");
        //注册广播接收器
        registerReceiver(receiver,filter);
    }
    public void OnPlay(View view){

        switch (view.getId()){
            case R.id.play:
                control.musicPlay(sb.getProgress());//得到当前进度
                break;
            case R.id.pause:
                control.musicPause();
                break;
            case R.id.stop:
                control.musicStop();
                break;
        }

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

}
