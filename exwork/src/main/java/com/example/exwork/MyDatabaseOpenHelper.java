package com.example.exwork;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    private  Context context;
    public static final String SQL="create table user (" +  //创建表的sql语句
            "id integer primary key autoincrement," +
            "username text," +
            "password text)";
    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //只在创建时执行一次
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
