package com.example.zhaojp.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sqlCreate extends SQLiteOpenHelper {
    private final static String NAME = "user.db";
    private final static int VERSION = 1;

    public sqlCreate(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table users (id integer primary key autoincrement,email varchar(64),username varchar(64),password varchar(64))";
        db.execSQL(sql);
        //db.execSQL("create table user (email varchar(64),username varchar(64),password varchar(64))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
