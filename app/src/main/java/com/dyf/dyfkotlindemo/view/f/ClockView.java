package com.dyf.dyfkotlindemo.view.f;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dyf on 2017/6/28.
 */

public class ClockView extends View {



    private Paint paintCircle;
    private Paint paintDegree;
    private Paint paintHour;
    private Paint paintMinute;

    private int mWidth, mHeight;
    // 默认
    private int mDefaultWidth = dip2px(120);
    private int mDefaultHeight = dip2px(180);


    public ClockView(Context context) {
        super(context);
        initView();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView(){
        paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(5);

        paintDegree = new Paint();
        paintDegree.setStrokeWidth(3);

        paintHour = new Paint();
        paintHour.setStrokeWidth(20);

        paintMinute = new Paint();
        paintMinute.setStrokeWidth(10);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
    }

    private void drawBg(Canvas canvas){
        canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2, paintCircle);
        //
        for (int i = 0; i < 24; i++){
            if (i == 0 || i == 6 || i == 12 || i == 18){
                paintDegree.setStrokeWidth(5);
                paintDegree.setTextSize(30);
                canvas.drawLine(mWidth/2, mHeight/2 - mWidth/2, mWidth/2, mHeight/2-mWidth/2 + 60, paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree, mWidth/2 - paintDegree.measureText(degree)/2, mHeight/2 - mWidth/2 + 90, paintDegree);
            }else{
                paintDegree.setStrokeWidth(3);
                paintDegree.setTextSize(15);
                canvas.drawLine(mWidth/2, mHeight/2-mWidth/2, mWidth/2,mHeight/2-mWidth/2+30,paintDegree);
                String degree = String.valueOf(i);
                canvas.drawText(degree, mWidth/2 - paintDegree.measureText(degree)/2, mHeight/2 - mWidth/2 + 60, paintDegree);
            }
            //通过旋转画布简化坐标运算
            canvas.rotate(15, mWidth/2, mHeight/2);
        }
        //保存画布
        canvas.save();
        canvas.translate(mWidth/2, mHeight/2);
        canvas.drawLine(0, 0, 100, 100, paintHour);
        canvas.drawLine(0, 0, 100, 200, paintMinute);
        canvas.restore();//合并图层
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec){
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            mWidth = Math.min(mDefaultWidth, specSize);
        }
        return mWidth;
    }


    private int measureHeight(int heightMeasureSpec){
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            mHeight = Math.min(mDefaultHeight, specSize);
        }
        return mHeight;
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    //
    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
