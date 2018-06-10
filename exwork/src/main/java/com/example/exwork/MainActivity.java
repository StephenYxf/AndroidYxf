package com.example.exwork;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    CheckBox AutoLogin;
    SQLiteDatabase db;
    MyDatabaseOpenHelper helper;
    SharedPreferences spf;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spf= PreferenceManager.getDefaultSharedPreferences(this);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        AutoLogin = findViewById(R.id.isRemeber);//记住密码

        boolean auto = spf.getBoolean("auto",false);
        if(auto){
            String stringUser=spf.getString("usernameString","");
            String stringPwd=spf.getString("passwordString","");
            username.setText(stringUser);
            password.setText(stringPwd);
            AutoLogin.setChecked(auto);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameEdit = username.getText().toString();
                String passwordEdit = password.getText().toString();
                if(TextUtils.isEmpty(usernameEdit) || TextUtils.isEmpty(passwordEdit)){
                    Toast.makeText(MainActivity.this,"账号密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                helper= new MyDatabaseOpenHelper(MainActivity.this,"myUser.db",null,1);
                db=helper.getWritableDatabase();//SQLiteDatabase db返回
                Cursor cursor = db.rawQuery("select * from user where username=? and password=?",new String[]{usernameEdit,passwordEdit});
                if(cursor.moveToFirst()){
                    Toast.makeText(MainActivity.this,"登录成功！",Toast.LENGTH_SHORT).show();
                    editor=spf.edit();
                    boolean isAutoLogin=AutoLogin.isChecked();
                    if(isAutoLogin){
                        editor.putString("usernameString",usernameEdit);
                        editor.putString("passwordString",passwordEdit);
                        editor.putBoolean("auto",isAutoLogin);
                    }else {
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("user",usernameEdit);
                    intent.putExtra("pass",passwordEdit);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this,"账号密码错误或者不存在",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void RegClick(View view){//单击TextView注册跳转
        Intent intent = new Intent(MainActivity.this,RegActivity.class);
        startActivity(intent);
    }
}
