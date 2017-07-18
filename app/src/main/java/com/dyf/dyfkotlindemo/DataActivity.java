package com.dyf.dyfkotlindemo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dyf.dyfkotlindemo.database.MyDatabaseHelper;
import com.dyf.dyfkotlindemo.model.data.Book;
import com.nostra13.universalimageloader.utils.L;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

/**
 * Created by dyf on 2017/7/17.
 */

public class DataActivity extends Activity {
    public static final String TAG = DataActivity.class.getSimpleName();


    Button createDatabase;
    Button add_data;
    Button update_data;
    Button update_data1;
    Button delete_data;
    Button query_data;
//    private MyDatabaseHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        createDatabase = (Button) findViewById(R.id.createDatabase);
        add_data = (Button) findViewById(R.id.add_data);
        update_data = (Button) findViewById(R.id.update_data);
        update_data1 = (Button) findViewById(R.id.update_data1);
        delete_data = (Button) findViewById(R.id.delete_data);
        query_data = (Button) findViewById(R.id.query_data);
//        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dbHelper.getWritableDatabase();
                Connector.getDatabase();
            }
        });
        add_data.setOnClickListener(v -> {
            Book book = new Book();
            book.setName("jhon");
            book.setAuthor("jhon");
            book.setPages(454);
            book.setPrice(16.96);
            book.setPress("UnKnow");
            book.save();
        });

        //
        update_data.setOnClickListener(v -> {
            Book book = new Book();
            book.setName("jhon");
            book.setAuthor("jhon");
            book.setPages(454);
            book.setPrice(16.96);
            book.setPress("UnKnow");
            book.save();
            book.setPrice(17.36);
            book.save();
        });

        //
        update_data1.setOnClickListener(v -> {
            Book book = new Book();
            book.setPrice(14.95);
            book.setPress("Anchor");
            book.updateAll("name = ? and author = ?", "jhon", "jhon");
        });

        //删除价格小于 15 的数据
        delete_data.setOnClickListener(v -> {
            DataSupport.deleteAll(Book.class, "price < ?", "15");
        });

        //
        query_data.setOnClickListener(v -> {
            List<Book> books = DataSupport.findAll(Book.class);
//            //只查询 name and author
//            DataSupport.select("name", "author").find(Book.class);
//            //只查询 页数大于 400
//            DataSupport.where("pages > >", "400").find(Book.class);
//            //指定结果 排序  asd 或者不写 表示升序
//            DataSupport.order("price desc").find(Book.class);
//            //指定查询的数量 offset偏移量
//            DataSupport.limit(3).offset(1).find(Book.class);
            //支持sql
//            Cursor c = DataSupport.findBySQL("select * from Book where pages > ? and price < ?", "400", "20");

            for (Book book : books){
                Log.d(TAG, "book name: " + book.getName());
                Log.d(TAG, "book author: " + book.getAuthor());
                Log.d(TAG, "book pages: " + book.getPages());
                Log.d(TAG, "book price: " + book.getPrice());
                Log.d(TAG, "book press: " + book.getPress());
            }
        });
    }
}
