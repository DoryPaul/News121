package com.example.yyz.news121.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * appname:News121
 * Author:  应义正
 * CreateDate: 2017/3/15
 * Function:
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // 数据库版本号
    private static final int DATABASE_VERSION = 1;
    // 数据库名
    private static final String DATABASE_NAME = "NBA.db";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Zan("
                + "id integer primary key autoincrement,"
                + "news_id text not null,"
                + "zan integer ,"
                + "shoucang integer ,"
                + "yonghu_id text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Create tables again
        onCreate(db);
    }

}