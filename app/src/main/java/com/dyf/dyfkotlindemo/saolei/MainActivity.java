package com.dyf.dyfkotlindemo.saolei;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.saolei.adapter.LeiAdapter;
import com.dyf.dyfkotlindemo.saolei.dao.UserScoreDao;
import com.dyf.dyfkotlindemo.saolei.entity.ItemEntity;
import com.dyf.dyfkotlindemo.saolei.entity.UserScoreEntity;
import com.dyf.dyfkotlindemo.saolei.util.VibratorUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

	/**难度设置，通过直接改变这个变量可以修改游戏难度（棋盘的旗子个数）*/
	private static final int LEVEL = 10;

	private GridView gv;
	private LeiAdapter adapter;
	private Button startBtn;
	private Timer timer;
	private int howTime;
	private Handler handler;
	private TextView showTimeTv;
	private boolean isGameing = false;
	private UserScoreDao dao;

	private static final int MESSAGE_UPDATE_TIME = 1;
	private void initView(){
		dao = new UserScoreDao(this);
		showTimeTv = (TextView)findViewById(R.id.time);
		/**游戏开始后启动定时器，并通过该handler更新游戏已用时间*/
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == MESSAGE_UPDATE_TIME){
					showTimeTv.setText("已用时间："+howTime+"秒");
				}
			}
		};
		startBtn = (Button) findViewById(R.id.startbtn);
		gv = (GridView) findViewById(R.id.gv);
		//用来计时的定时器
		timer = new Timer();
		adapter = new LeiAdapter(LEVEL, this,gv);
		gv.setNumColumns(LEVEL);
		gv.setAdapter(adapter);
	}

	/**开始游戏需要进行的任务*/
	private void startGame(){
		timer.cancel();
		timer = new Timer();
		isGameing = true;
		howTime = 0;
		adapter = new LeiAdapter(LEVEL, MainActivity.this, gv);
		gv.setAdapter(adapter);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				howTime++;
				handler.sendEmptyMessage(MESSAGE_UPDATE_TIME);
			}
		}, 0, 1000);
	}

	/**游戏结束后的状态调整*/
	private void stopGame(){
		timer.cancel();
		isGameing = false;
	}

	private void addListener(){
		/**点击开始按钮则充值状态*/
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((Button)v).setText("重新开始");
				startGame();
			}
		});

		/**长按为单元格添加旗子标记*/
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				if(!isGameing){     //游戏没开始，则长按不生效
					return true;
				}
				ItemEntity entity = adapter.getItem(position);
				if(entity.isShow()){        //该单元格是显示状态，则长按不生效
					return true;
				}
				entity.setBiaoJi(!entity.isBiaoJi());   //将旗子标记位置返
				adapter.notifyDataSetChanged();
				//添加成功后提供震动反馈，需要权限
				VibratorUtil.Vibrate(MainActivity.this, 500);
				return true;
			}

		});
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if(!isGameing){   //游戏没开始，则点击不生效
					return;
				}
				ItemEntity entity = adapter.getItem(position);
				if(entity.isBiaoJi()){    //该单元格被标记，则点击不生效
					return;
				}
				if(!entity.isShow()){
					if(entity.isLei()){    //点击的单元格是雷，则游戏结束
						entity.setShow(true);
						stopGame();
						Toast.makeText(MainActivity.this, "游戏失败，请重新来过", Toast.LENGTH_SHORT).show();
						adapter.showLei();
						adapter.showBiaoJi();
						return;
					}
					//点击的是空白单元格，则显示周围无害的单元格（模仿window扫雷的功能）
					if(entity.getLeiCount() == 0 &&!entity.isLei()){
						adapter.shouNoLei(position);
					}
					entity.setShow(true);
					//判断游戏是否胜利
					if(adapter.isWin()){
						stopGame();
						View dialogView = getLayoutInflater().inflate(R.layout.dialog_win_name, null);
						final EditText et = (EditText) dialogView.findViewById(R.id.editText1);
						new AlertDialog.Builder(MainActivity.this).setTitle("恭喜").setPositiveButton("递交成绩", new Dialog.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								String userName = et.getText().toString().trim();
								if(userName == null || "".equals(userName)){
									Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
								}else{
									UserScoreEntity entity= new UserScoreEntity(userName, howTime);
									dao.addUser(entity);
									Intent intent = new Intent(MainActivity.this,PaiHangActivity.class);
									startActivity(intent);
								}
							}
						}).setNegativeButton("取消", null).setView(dialogView).create().show();;
					}
					adapter.notifyDataSetChanged();
				}
			}

		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saolei);
		initView();
		addListener();
	}

}