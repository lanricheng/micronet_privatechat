package com.vdunpay.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HY on 2018/8/16.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "vchat.db";
    private static final int VERSION = 1;

    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据库创建
//        db.execSQL("create table person (_id integer primary key autoincrement, " +
//                "name char(10), phone char(20), money integer(20))");

        String sql="create table chathistory(userid varchar(20) not null ," +
                " username varchar(1000) ," +
                " usericon varchar(20) not null" +
                " );";
        db.execSQL(sql);

//        sql="create table client(name varchar(20) not null,"
//                +"age varchar(10),detail varchar(100),"
//                +"address varchar(50),phone varchar(12));";
//        db.execSQL(sql);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //数据库升级
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
