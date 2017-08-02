package com.dyf.dyfkotlindemo.audio;

import java.util.Random;


import android.app.Activity;
import android.os.Bundle;

import com.dyf.dyfkotlindemo.R;

public class MainActivity extends Activity {
private AudioInputView audio ;
private int [] progress = {0,1,2,3};
private Random random = new Random();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        audio = (AudioInputView) this.findViewById(R.id.audio_input_view);
        audio.setOnAudioInputListener(listener);
    }
    
    
    private int legth = 6;
    
    AudioInputView.OnAudioInputListener listener = new AudioInputView.OnAudioInputListener() {
		
		@Override
		public void onStopRecording() {  //暂停
			// TODO Auto-generated method stub
			isPlay = false;
			audio.onPlayReady(legth);
			
		}
		
		@Override
		public void onStartRecording() {  //开始录音
			// TODO Auto-generated method stub 
			isPlay = false;  
			audio.onStartRecording(3 , 0);   // 这里应该传入 分贝值 ，和 录音时间    ，分贝分为 四个档  0 1 2 3
		}
		
		@Override
		public void onReset() {  //重录  
			// TODO Auto-generated method stub
			isPlay = false;
			audio.onInitial();
		}
		
		@Override
		public void onPlayRecording() {  //开始播放 刚才录制的
			// TODO Auto-generated method stub
			isPlay = true;
			new Thread(showPlayRunnable).start();
		}
		
		@Override
		public void onError(int type) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onEndRecord() { //录音结束时  
			// TODO Auto-generated method stub
			isPlay = false;
			audio.onPlayReady(legth);
		}
		
		@Override
		public void onCancel() {  //取消录音时调用
		isPlay = false;
		audio.onInitial();
			
		}
	};
	
	private boolean isPlay = false;
	
	ShowPlayRunnable showPlayRunnable = new ShowPlayRunnable();

	class ShowPlayRunnable implements Runnable {

		@Override
		public void run() {
			while(isPlay) //这里实际应该用播放去判断
//			while (mPlayer.isPlaying ) {
				try {
					audio.onPlayRecording(legth, random.nextInt(4));
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
	
