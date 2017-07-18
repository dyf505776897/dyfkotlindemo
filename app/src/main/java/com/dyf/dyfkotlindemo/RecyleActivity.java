package com.dyf.dyfkotlindemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dyf.dyfkotlindemo.adapter.SampleAdapter;
import com.dyf.dyfkotlindemo.model.GoodsBean;
import com.dyf.dyfkotlindemo.utils.ParseUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dyf on 2017/7/13.
 */

public class RecyleActivity extends Activity {


    private LinearLayoutManager mLayoutManager;
    RecyclerView ad_cyclerview;
    SampleAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r);


        ad_cyclerview = (RecyclerView) findViewById(R.id.ad_cyclerview);

        ad_cyclerview.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ad_cyclerview.setLayoutManager(mLayoutManager);
        ad_cyclerview.setItemAnimator(new DefaultItemAnimator());

        adapter = new SampleAdapter(RecyleActivity.this);
        ad_cyclerview.setAdapter(adapter);

        //
        getData();
    }

    private void getData(){
        String data = getResources().getString(R.string.data);
        try {
            JSONObject ddata = new JSONObject(data);
            GoodsBean goodsBean = ParseUtil.parseDataToEntity(ddata, GoodsBean.class);
            adapter.getList().addAll(goodsBean.getResult());
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
