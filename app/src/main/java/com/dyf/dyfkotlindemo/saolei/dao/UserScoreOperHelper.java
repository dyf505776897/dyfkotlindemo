package com.dyf.dyfkotlindemo.saolei.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UserScoreOperHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "create table score_info(_id integer primary key autoincrement,user_name text,score integer)";

    public UserScoreOperHelper(Context context, int version) {
        super(context, "score_info", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}
