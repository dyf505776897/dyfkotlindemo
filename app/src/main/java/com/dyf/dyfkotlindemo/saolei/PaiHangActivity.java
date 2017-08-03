package com.dyf.dyfkotlindemo.saolei;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.saolei.dao.UserScoreDao;
import com.dyf.dyfkotlindemo.saolei.entity.UserScoreEntity;

import java.util.ArrayList;

/**显示游戏排行的界面*/
public class PaiHangActivity extends Activity {

	private ListView lv;
	private Button clearBtn;
	private Button backBtn;

	private ArrayList<UserScoreEntity> scores;
	private UserScoreDao dao;
	private ArrayAdapter adapter;

	private void initView(){
		dao = new UserScoreDao(this);
		scores = dao.getList();

		lv = (ListView)findViewById(R.id.listView1);
		clearBtn = (Button)findViewById(R.id.clear);
		backBtn = (Button)findViewById(R.id.back);
	}

	private void addListener(){
		adapter = new ArrayAdapter<UserScoreEntity>(this, android.R.layout.simple_list_item_1, scores);
		lv.setAdapter(adapter);
		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dao.deleteAllUser();
				scores.clear();
				adapter.notifyDataSetChanged();
			}
		});

		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_paihang);
		initView();
		addListener();
	}
}