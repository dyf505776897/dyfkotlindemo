package com.dyf.dyfkotlindemo.view.reflash.aini;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dyf.dyfkotlindemo.utils.DensityUtil;

/**
 * Created by dyf on 2017/6/26.
 */

public class Ghost extends LVBase{


    //View 宽高
    private int mWidth, mHeight;
    // 默认
    private int mDefaultWidth = dip2px(120);
    private int mDefaultHeight = dip2px(180);

    public Ghost(Context context) {
        super(context);
    }

    public Ghost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Ghost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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




    //画笔
    Paint mBodyPaint, mEyesPaint, mShadowPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHead(canvas);
    }



    //头部半径
    private int mHeadRadius;
    //圆心(头部)的x坐标
    private int mHeadCentreX;
    //圆心(头部)的y坐标
    private int mHeadCentreY;
    //头部最左侧的坐标
    private int mHeadLeftX;
    //头部最右侧的坐标
    private int mHeadRightX;
    //距离View顶部的内边距
    private int mPaddingTop = dip2px(20);

    /** **/
    private void drawHead(Canvas canvas){
        mHeadRadius = mWidth / 3;
        mHeadCentreX = mWidth / 2;
        mHeadCentreY = mWidth / 3 + mPaddingTop;
        mHeadLeftX = mHeadCentreX + mHeadRadius;
        mHeadRightX = mHeadCentreX + mHeadRadius;
        canvas.drawCircle(mHeadCentreX, mHeadCentreY, mHeadRadius, mBodyPaint);
    }


    @Override
    protected void InitPaint() {
        mBodyPaint = new Paint();
        mBodyPaint.setAntiAlias(true);
        mBodyPaint.setStyle(Paint.Style.FILL);
        mBodyPaint.setColor(Color.WHITE);

        //
        mEyesPaint = new Paint();
        mEyesPaint.setAntiAlias(true);
        mEyesPaint.setStyle(Paint.Style.FILL);
        mEyesPaint.setColor(Color.BLACK);

        //
        mShadowPaint = new Paint();
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(Color.argb(60, 0, 0, 0));
    }

    @Override
    protected void OnAnimationUpdate(ValueAnimator valueAnimator) {

    }

    @Override
    protected void OnAnimationRepeat(Animator animation) {

    }

    @Override
    protected int OnStopAnim() {
        return 0;
    }

    @Override
    protected int SetAnimRepeatMode() {
        return 0;
    }

    @Override
    protected int SetAnimRepeatCount() {
        return 0;
    }

    @Override
    protected void AinmIsRunning() {

    }
}
