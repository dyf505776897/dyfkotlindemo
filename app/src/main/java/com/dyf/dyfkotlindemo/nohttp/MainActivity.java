package com.dyf.dyfkotlindemo.nohttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dyf.dyfkotlindemo.R;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.StringRequest;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nohttp);
    }

    public void Test(View v) {
        RequestQueue queue = NoHttp.newRequestQueue(); // 默认三个并发，此处可以传入并发数量。
        // 请求String：
        final StringRequest request = new StringRequest("http://106.14.218.31:8020/API/Article/ArticleClick", RequestMethod.POST);
        request.add("Id", 64); // int类型
//        request.setHeader("","");
        // 发起请求：
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                Log.i("zzz", "onStart");
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                Log.i("zzz", "onSucceed" + response.get());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Log.i("zzz", "onFailed" + response);
            }

            @Override
            public void onFinish(int what) {
                Log.i("zzz", "onFinish");
            }
        });

    }
}
