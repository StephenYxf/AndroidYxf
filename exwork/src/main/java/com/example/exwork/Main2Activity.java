package com.example.exwork;



import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.example.exwork.Adapter.NewsAdapter;

import com.example.exwork.Service.MusicService;
import com.example.exwork.Service.MyService;
import com.example.exwork.Task.ImageTask;
import com.example.exwork.Task.JSONTask;
import com.example.exwork.bean.News;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import pl.droidsonroids.gif.GifImageView;

public class Main2Activity extends AppCompatActivity {
    ProgressBar pb;
    Intent intentj;
    MyService.MusicControl control;//遥控器
    SeekBar sb;
    ListView newsList,listView;
    String struname1;
    View footView,fudongView,zdView,mView;
    transient NewsAdapter adapter;//不序列化
    ArrayList<News> data;
    TextView loadText,fdtitle,fdcontent,fudongThemetype,fudongCreateTime,maxText,currentText,uname,exit;
    ImageView fdimage;
    boolean isDown,isLoad=true;
    int curpage=1;
    Button exitbtn;
    GifImageView giv;
    WindowManager windowManager;
    WindowManager.LayoutParams wmParams;
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            control = (MyService.MusicControl) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);


        if (Build.VERSION.SDK_INT >= 23) {//浮动窗口权限要申请一下  第一次 同意之后就好了
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }

        newsList = findViewById(R.id.news_list);//列表
        footView = LayoutInflater.from(this).inflate(R.layout.footerview,null);//底部视图

        uname = findViewById(R.id.uname);
        Intent intent=getIntent();
        struname1 = intent.getStringExtra("user");
        uname.setText("欢迎:"+struname1.toString());
        exit = findViewById(R.id.exit);
        loadText = footView.findViewById(R.id.textView);//加载数据的那个TextView
        data = new ArrayList<>();
        adapter = new NewsAdapter(this,R.layout.news_item,data);
        newsList.addFooterView(footView);
        newsList.setAdapter(adapter);
        pb = footView.findViewById(R.id.progressBar);
        loadData();
        exit.setOnClickListener(new View.OnClickListener() {//退出按钮
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(Main2Activity.this,MainActivity.class);
//                startActivity(intent1);
                finish();
            }
        });




        listView=findViewById(R.id.news_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//单击列表项 显示对应浮动视图
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fudongView =LayoutInflater.from(Main2Activity.this).inflate(R.layout.activity_fudong,null);//浮动视图
                News news = data.get(position);
                fdtitle=fudongView.findViewById(R.id.fudongtitle);
                fdimage=fudongView.findViewById(R.id.fudongimgContent);
                fdcontent=fudongView.findViewById(R.id.fudongcontent);
                fudongThemetype = fudongView.findViewById(R.id.fudongThemetype);
                //fudongCreateTime =fudongView.findViewById(R.id.fudongCreateTime);
                fdtitle.setText(news.getTitle());
                fdcontent.setText(news.getContent());
                fudongThemetype.setText(news.getThemetype());//新闻类型
                //fudongCreateTime.setText(news.getDate());
                final String imgUrl = news.getImgUrl();
                final Map<String,Bitmap> map=new HashMap<String,Bitmap>();
                //http://litchiapi.jstv.com/
                new ImageTask(new ImageTask.CallBack() {
                    @Override
                    public void getData(Bitmap pic) {
                        fdimage.setImageBitmap(pic);
                        map.put(imgUrl,pic);
                    }
                }).execute(imgUrl);


                //获取WindowManager对象
                windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
                //创建WindowManager的布局参数对象
                wmParams = new WindowManager.LayoutParams();
                //设置添加View的类型
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                //设置添加View的标识
                wmParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                wmParams.gravity = Gravity.CENTER;
                //设置添加View的默认坐标值
//                wmParams.x = 150;
//                wmParams.y = 550;
                // 透明度
                //wmParams.alpha=0.9f;
                //设置添加View的宽高
                wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
                wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                wmParams.format = 1;


                //最终调用WindowManger.addView方法，将View添加到窗口上
                windowManager.addView(fudongView, wmParams);


                exitbtn = fudongView.findViewById(R.id.exitfudong);
                exitbtn.setOnClickListener(new View.OnClickListener() {//浮动窗口关闭按钮
                    @Override
                    public void onClick(View v) {
                        windowManager.removeView(fudongView);
                        //解绑音乐
                        //unbindService(connection);
                        //unregisterReceiver(receiver);
                        stopService(intentj);
                    }
                });

                //绑定service 播放音乐===========================================================================
                intentj = new Intent(Main2Activity.this,MyService.class);
                startService(intentj);
//                Log.i("typre",String.valueOf(type));
//                control.musicStop();
//                control.musicPlay(type);
                Log.i("123","绑定成功");

            }
        });


        FloatingActionButton mFloatBtn;//返回顶部按钮
        mFloatBtn =findViewById(R.id.floating_btn_main);
        mFloatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //listView返回到顶部
                listView.smoothScrollToPosition(0);
            }
        });


        giv = findViewById(R.id.imageView3);//进入音乐选择界面
        giv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentmusic = new Intent(Main2Activity.this,musicActivity.class);
                startActivity(intentmusic);

            }
        });



        newsList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(isDown==true && scrollState==SCROLL_STATE_IDLE){//到底部   并且   静止状态
                        loadData();
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(visibleItemCount+firstVisibleItem==totalItemCount){
                    isDown=true;
                }else {
                    isDown=false;
                }
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(Main2Activity.this, MusicService.class);
        stopService(intent);
//        if(connection!=null){unbindService(connection);}
//        Log.i("con1111111111111111111",connection.toString());
//        if(receiver!=null){unregisterReceiver(receiver);}
//        Log.i("rec1111111111111111111",receiver.toString());
    }

    //下载一页数据
    public void loadData(){
        if(isLoad){
            Log.d("加载","===================================");
            pb.setVisibility(View.VISIBLE);
            loadText.setText("正在加载数据，请稍后");
            isLoad=false;
            new JSONTask(new JSONTask.CallBack() {
                @Override
                public void getData(String result) {
                    ArrayList<News> temp = JSONParser.MyJsonParer(result); //存放解析好的json数据
                    data.addAll(temp);
                    adapter.notifyDataSetChanged();//刷新数据 不然不会刷新

                    pb.setVisibility(View.VISIBLE);
                    loadText.setText("加载数据");
                    isLoad=true;
                    curpage++;
                    Log.i("curpage:",String.valueOf(curpage));
                }
            }).execute("https://www.apiopen.top/satinApi?type=1&page="+curpage);
        }

    }

}
