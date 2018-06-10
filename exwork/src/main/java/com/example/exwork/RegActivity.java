package com.example.exwork;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegActivity extends AppCompatActivity {
    EditText uname,pwd,conf;
    Button reg;
    MyDatabaseOpenHelper helper;
    SQLiteDatabase db;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        uname = findViewById(R.id.username1);
        pwd = findViewById(R.id.pwd1);
        conf =findViewById(R.id.pwd2);
        reg = findViewById(R.id.reg);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unameString = uname.getText().toString();
                String pwdString =pwd.getText().toString();
                String confString =conf.getText().toString();
                helper=new MyDatabaseOpenHelper(RegActivity.this,"myUser.db",null,1);
                db = helper.getWritableDatabase();

                //查询是否存在用户
                cursor=db.rawQuery("select * from user where username=?",new String[]{unameString});

                if(TextUtils.isEmpty(unameString) || TextUtils.isEmpty(pwdString) || TextUtils.isEmpty(confString)){
                    Toast.makeText(RegActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                }else if(!pwdString.equals(confString)){
                        Toast.makeText(RegActivity.this,"两次密码不同",Toast.LENGTH_SHORT).show();

                }else if(cursor.moveToFirst()){
                    Toast.makeText(RegActivity.this,"创建的用户已经存在",Toast.LENGTH_SHORT).show();
                }else{
                    db.execSQL("insert into user(username,password) values(?,?)",new String[]{unameString,pwdString});
                }
                db.close();//关闭数据库
                finish();//结束当前活动 回到前面活动  注册完毕

            }
        });
    }
}
