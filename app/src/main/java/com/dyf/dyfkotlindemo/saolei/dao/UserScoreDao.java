package com.dyf.dyfkotlindemo.saolei.dao;


import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dyf.dyfkotlindemo.saolei.entity.UserScoreEntity;

public class UserScoreDao {

    private Context context;
    private UserScoreOperHelper helper;

    private static final String TABLE_NAME = "score_info";
    private static final String ID = "_id";
    private static final String USER_NAME = "user_name";
    private static final String USER_SCORE = "score";

    public UserScoreDao(Context context) {
        this.context = context;
        helper = new UserScoreOperHelper(context, 1);
    }

    /**添加用户*/
    public long addUser(UserScoreEntity entity){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, entity.getName());
        values.put(USER_SCORE, entity.getScore());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    /**查询前10位应用*/
    public ArrayList<UserScoreEntity> getList(){
        ArrayList<UserScoreEntity> scores = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,  new String[]{ID,USER_NAME,USER_SCORE}, null, null, null, null, USER_SCORE+","+ID+" desc", 10+"");
        if(c != null){
            scores = new ArrayList<UserScoreEntity>();
            while(c.moveToNext()){
                scores.add(new UserScoreEntity(c.getString(c.getColumnIndex(USER_NAME)), c.getInt(c.getColumnIndex(USER_SCORE))));
            }
            c.close();
        }
        db.close();
        return scores;
    }

    /**清空排行榜*/
    public int deleteAllUser(){
        SQLiteDatabase db = helper.getReadableDatabase();
        int count = db.delete(TABLE_NAME, null, null);
        db.close();
        return count;
    }

}
