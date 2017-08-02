package com.dyf.dyfkotlindemo.audio;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dyf.dyfkotlindemo.R;

/**
 * liuyu
 * @author HX-ZJ-001
 *
 */
public class AudioInputView extends View {
	/**
	 * 准备录制 初始状态
	 */
	private final int READY_TYPE = 0;
	/**
	 * 正在录制中
	 */
	private final int RECORDINGS_TYPE = 1;
	/**
	 * 录制完成 等待播放状态
	 */
	private final int PLAYREADY_TYPE = 2;
	/**
	 * 播放暂停
	 */
	private final int PLAYPAUSE_TYPE = 3;

	private int work_type = 0;

	private Context mContext = getContext();

	/**
	 * 最外层的圆环
	 */
	private Paint circular = null;

	/**
	 * 最外层的颜色
	 * 
	 * @param context
	 */
	private int circularColor = 0xffd7d7d7;

	/**
	 * 最外层圆环的边宽
	 * 
	 * @param context
	 */
	private float circularwidth = 4f;
	private int centerX;
	private int centerY;
	private int radius;
	private int width;
	private int height;
	/**
	 * 圆环背景
	 * 
	 * @param context
	 */
	private Paint circularBackground = null;
	/**
	 * 圆环背景颜色
	 * 
	 * @param context
	 */
	private int circularBackgroundColor = 0xffffffff;

	private int circularRecordBackgroundColor = 0xfff2f2f2;

	private Bitmap icon = null;

	private Paint iconPaint = null;

	private Paint timePint = null;

	private Bitmap timeIcon = null;

	private String timeText = "10";

	private Paint timeFontPaint = null;

	private Bitmap PlayIcon = null;

	private int progressColor = 0xfff8db00;
	private float progresswidth = 8f;
	private Paint progress1 = null;
	private Paint progress2 = null;
	private Paint progress3 = null;

	private float textSize = 40;

	private Paint playButton = null;

	private Paint playButtonBackground = null;

	private RectF rec = null;

	private final int COMPLEMENT_NUMBER = 10;

	private final String TEXT_PLAY = "重录";

	private Bitmap pauseIcon = null;

	private String fileCountTime = "0";
	
	public AudioInputView(Context context) {
		super(context);
		this.mContext = context;
		init(null);
	}

	public AudioInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init(attrs);
	}

	public AudioInputView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		init(attrs);
	}

	public void init(AttributeSet attrs) {
		icon = BitmapFactory.decodeResource(getResources(), R.drawable.voice);
		timeIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.voice_time);
		PlayIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.voice_play);
		pauseIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.voicel_suspend);
		initView();
	}

	@SuppressLint("ClickableViewAccessibility")
	public void initView() {

		circular = new Paint();
		circular.setColor(circularColor);
		circular.setDither(true);
		circular.setAntiAlias(true);
		circular.setStyle(Style.STROKE);
		circular.setStrokeWidth(circularwidth);

		circularBackground = new Paint();
		circularBackground.setColor(circularBackgroundColor);
		circularBackground.setDither(true);
		circularBackground.setAntiAlias(true);
		circularBackground.setStyle(Style.FILL);

		iconPaint = new Paint();

		timePint = new Paint();

		timeFontPaint = new Paint();
		timeFontPaint.setColor(0xffffffff);
		timeFontPaint.setTextSize(textSize);
		timeFontPaint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体

		progress1 = new Paint();
		progress2 = new Paint();
		progress3 = new Paint();

		progress1.setColor(progressColor);
		progress1.setDither(true);
		progress1.setAntiAlias(true);
		progress1.setStyle(Style.STROKE);
		progress1.setStrokeWidth(progresswidth);
		progress1.setAlpha(200);

		progress2.setColor(progressColor);
		progress2.setDither(true);
		progress2.setAntiAlias(true);
		progress2.setStyle(Style.STROKE);
		progress2.setStrokeWidth(progresswidth);
		progress2.setAlpha(140);

		progress3.setColor(progressColor);
		progress3.setDither(true);
		progress3.setAntiAlias(true);
		progress3.setStyle(Style.STROKE);
		progress3.setStrokeWidth(progresswidth);
		progress3.setAlpha(80);

		playButton = new Paint();
		playButton.setColor(circularColor);
		playButton.setDither(true);
		playButton.setAntiAlias(true);
		playButton.setStyle(Style.STROKE);
		playButton.setStrokeWidth(circularwidth);

		rec = new RectF();

		playButtonBackground = new Paint();
		playButtonBackground.setColor(0xffffffff);
		playButtonBackground.setDither(true);
		playButtonBackground.setAntiAlias(true);
		playButtonBackground.setStyle(Style.FILL);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		 super.onLayout(changed, left, top, right, bottom);
		width = getWidth();
		height = getHeight();
		centerX = (width) / 2;
		centerY = (height) / 2;
		radius = centerX - 75;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		float textPlayWidths = timeFontPaint.measureText(TEXT_PLAY);
		switch (work_type) {
		case READY_TYPE:
			circularBackground.setColor(circularBackgroundColor);
			canvas.drawCircle(centerX, centerY, radius, circularBackground);
			canvas.drawCircle(centerX, centerY, radius, circular);
			canvas.drawBitmap(icon, centerX - (icon.getWidth() / 2), centerY
					- (icon.getHeight() / 2), iconPaint);
			break;

		case RECORDINGS_TYPE:
			timeFontPaint.setColor(0xffffffff);
			circularBackground.setColor(circularRecordBackgroundColor);
			canvas.drawCircle(centerX, centerY, radius, circularBackground);
			canvas.drawCircle(centerX, centerY, radius, circular);
			canvas.drawBitmap(icon, centerX - (icon.getWidth() / 2), centerY
					- (icon.getHeight() / 2), iconPaint);
			setDrawProgress(canvas);
			canvas.drawBitmap(timeIcon, centerX - (timeIcon.getWidth() / 2), 0,
					timePint);
			if (timeText == null || TextUtils.isEmpty(timeText)) {
				timeText = "0";
			}
			float textWidth = timeFontPaint.measureText(timeText + "”");
			canvas.drawText(timeText + "”", centerX - textWidth / 2,
					textSize + 4, timeFontPaint);

			break;

		case PLAYREADY_TYPE:
			timeFontPaint.setColor(0xff000000);

			circularBackground.setColor(circularBackgroundColor);
			canvas.drawCircle(centerX, centerY, radius, circularBackground);
			canvas.drawCircle(centerX, centerY, radius, circular);
			canvas.drawBitmap(PlayIcon, centerX - (PlayIcon.getWidth() / 2),
					centerY - (PlayIcon.getHeight() / 2), iconPaint);
			if (timeText == null || TextUtils.isEmpty(timeText)) {
				timeText = "0";
			}
			float textWidths = timeFontPaint.measureText(timeText + "”");
			canvas.drawText(timeText + "”", centerX - textWidths / 2, centerY
					+ (centerY / 2) - COMPLEMENT_NUMBER, timeFontPaint);
			rec.set(centerX - (centerX / 4) - COMPLEMENT_NUMBER, centerY * 2
					- 75 - (centerY / 6), centerX + (centerX / 4)
					+ COMPLEMENT_NUMBER, (centerY * 2) - 75 + (centerY / 6));
			canvas.drawRoundRect(rec, 20, 15, playButton);
			canvas.drawRoundRect(rec, 20, 15, playButtonBackground);
			canvas.drawText(TEXT_PLAY, centerX - textPlayWidths / 2, centerY
					* 2 - 75 + COMPLEMENT_NUMBER, timeFontPaint);

			break;
		case PLAYPAUSE_TYPE:
			timeFontPaint.setColor(0xff000000);
			if (timeText == null || TextUtils.isEmpty(timeText)) {
				timeText = "0";
			}
			float textWidth2 = timeFontPaint.measureText(timeText + "”");
			circularBackground.setColor(circularBackgroundColor);
			canvas.drawCircle(centerX, centerY, radius, circularBackground);
			canvas.drawCircle(centerX, centerY, radius, circular);
			canvas.drawBitmap(pauseIcon, centerX - (pauseIcon.getWidth() / 2),
					centerY - (pauseIcon.getHeight() / 2), iconPaint);
			canvas.drawText(timeText + "”", centerX - textWidth2 / 2, centerY
					+ (centerY / 2) - COMPLEMENT_NUMBER, timeFontPaint);
			rec.set(centerX - (centerX / 4) - COMPLEMENT_NUMBER, centerY * 2
					- 75 - (centerY / 6), centerX + (centerX / 4)
					+ COMPLEMENT_NUMBER, (centerY * 2) - 75 + (centerY / 6));
			setDrawProgress(canvas);
			canvas.drawRoundRect(rec, 20, 15, playButton);
			canvas.drawRoundRect(rec, 20, 15, playButtonBackground);
			canvas.drawText(TEXT_PLAY, centerX - textPlayWidths / 2, centerY
					* 2 - 75 + COMPLEMENT_NUMBER, timeFontPaint);

			break;
		default:
			// 安装第一模式来处理

			break;
		}

	}

	private int progress = 0;

	public void setDrawProgress(Canvas canvas) {
		switch (progress) {
		case 1:
			canvas.drawCircle(centerX, centerY, centerX - 50, progress1);
			break;
		case 2:
			canvas.drawCircle(centerX, centerY, centerX - 50, progress1);
			canvas.drawCircle(centerX, centerY, centerX - 30, progress2);
			break;
		case 3:
			canvas.drawCircle(centerX, centerY, centerX - 50, progress1);
			canvas.drawCircle(centerX, centerY, centerX - 30, progress2);
			canvas.drawCircle(centerX, centerY, centerX - 10, progress3);

			break;
		default:
			break;
		}
	}

	public synchronized int getWorkTpye(){
		return work_type;
	}
	
	public synchronized void onInitial() {
		work_type = 0;
		postInvalidate();
	}

	/**
	 * 开始录音
	 * 
	 * @param progress
	 */
	public synchronized void onStartRecording(int progress , int time) {
		if (progress < 0 || time < 0) {
			throw new IllegalArgumentException("progress  || time not less than 0");
		}
		this.timeText = time+"";
		this.progress = progress;
		work_type = 1;
		postInvalidate();
	}

	public synchronized void onStartRecording(int time) {
		if (time < 0) {
			throw new IllegalArgumentException(" time not less than 0");
		}
		this.timeText = time+"";
		work_type = 1;
		this.progress = 0;
		postInvalidate();
	}

	/**
	 * 等待播放状态 或暂停 状态
	 * 
	 * @return
	 */
	public synchronized void onPlayReady(int length) {
		work_type = 2;
		this.timeText = length+"";
		postInvalidate();
	}

	/**
	 * 开始播放录音
	 */
	public synchronized void onPlayRecording(int lenght, int progress) {
		work_type = 3;
		this.progress = progress;
		this.timeText = lenght+"";
		postInvalidate();
	}

	public synchronized void onPlayRecording(int lenght) {
		work_type = 3;
		this.progress = 0;
		this.timeText = lenght+""; 
		postInvalidate();
	}

	/**
	 * 暂停
	 * 
	 * @param lenght
	 */
	public synchronized void onPause(int lenght) {
		work_type = 2;
		this.timeText = lenght+"";
		onPlayReady(lenght);
	}

	public synchronized void onEndRecording() {
		work_type = 2;
		postInvalidate();
	}

	
	
	public void setOnAudioInputListener(OnAudioInputListener l){
		this.listener = l;
	}
	
	public interface OnAudioInputListener {

		/**
		 * 开始录音
		 */
		public void onStartRecording();

		/**
		 * 结束录音  即录音完成
		 */
		public void onEndRecord();

		/**
		 * 取消录音  
 		 */
		public void onCancel();
		
		/**
		 * 开始播放
		 */
		public void onPlayRecording();

		/**
		 * 暂停播放
		 */
		public void onStopRecording();

		/**
		 * 重录
		 */
		public void onReset();

		/**
		 * 错误 type 是当前类型
		 */
		public void onError(int type);

	}

	/**
	 * if ((x-radius)*(x-radius)+(y-radius)*(y-radius)<radius*radius) {
	 * Log.e("TAG", "1111");
	 * 
	 * 
	 * } else
	 * if((x-rec.left)*(x-rec.right)+(y-rec.top)*(y-rec.bottom)<(rec.bottom
	 * -rec.top)*(rec.right-rec.left)){ Log.e("TAG", "wwwww"); }
	 */

	private OnAudioInputListener listener = null;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		try {
			// TODO Auto-generated method stub
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				switch (work_type) {
				case READY_TYPE:
					if ((x - radius) * (x - radius) + (y - radius)
							* (y - radius) < radius * radius) {
						if (null != listener) {
							listener.onStartRecording();
						}
						return true;
					}
					break;
				case RECORDINGS_TYPE:
					break;
				case PLAYREADY_TYPE:
					if (radius <= 0 || rec == null)
						return true;
					if ((x - radius) * (x - radius) + (y - radius)
							* (y - radius) < radius * radius) {
						if (null != listener) {
							listener.onPlayRecording();
						}
					} else if ((x - rec.left) * (x - rec.right) + (y - rec.top)
							* (y - rec.bottom) < (rec.bottom - rec.top)
							* (rec.right - rec.left)) {
						if (null != listener) {
							listener.onReset();
						}
					}

					break;

				case PLAYPAUSE_TYPE:
					if (radius <= 0 || rec == null)
						return true;
					if ((x - radius) * (x - radius) + (y - radius)
							* (y - radius) < radius * radius) {
						if (null != listener) {
							listener.onStopRecording();
						}
					} else if ((x - rec.left) * (x - rec.right) + (y - rec.top)
							* (y - rec.bottom) < (rec.bottom - rec.top)
							* (rec.right - rec.left)) {
						if (null != listener) {
							listener.onReset();
						}
					}

					break;
				default:
					break;
				}
				break;
			case MotionEvent.ACTION_UP:
					if(work_type==READY_TYPE){
						if(listener!=null){
							listener.onCancel();
						}
						
						return true;
					}
				
				if (work_type == RECORDINGS_TYPE) {
						if (listener != null) {
							listener.onEndRecord();
						}
						
						return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_CANCEL:
				if(work_type==READY_TYPE){
					if(listener!=null){
						listener.onCancel();
					}
					
					return true;
				}
				
				
				if (work_type == RECORDINGS_TYPE) {
					
					if ((x - radius) * (x - radius) + (y - radius)
							* (y - radius) >radius * radius) {
						if (listener != null) {
							listener.onEndRecord();
						}
						return true;
					}
					
						
				}

				break;
			default:
				break;
			}
		} catch (Exception e) {
			if (listener != null) {
				listener.onError(work_type);
			}
			Log.e("TAG", "ly AudioInputView error " + e.getMessage());
			e.printStackTrace();
			return true;
		}
		return true;

	}
	
	
	
	
}
