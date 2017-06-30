package com.dyf.dyfkotlindemo.view.f;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by dyf on 2017/6/27.
 */

public class MyView extends ViewGroup {

    private Scroller mScroller;
    private int mScreenHeight;


    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(){
        mScroller = new Scroller(getContext());
        //
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mScreenHeight= MeasureSpec.getSize(heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++ ){
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }


    //对子view 进行放置位置的设定
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //这是ViewGroup高度
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mScreenHeight * childCount;
        setLayoutParams(mlp);
        for (int i =0; i < childCount; i++){
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE){
                child.layout(l,i * mScreenHeight, r, (i + 1) * mScreenHeight);
            }
        }
    }

    int mLastY ;
    int mStart ;
    int mEnd ;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                mLastY = y;
                mStart = getScrollY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                int dy = mLastY - y;
                if (getScrollY() < 0) {
                    dy = 0;
                }
                if (getScrollY() > getHeight() - mScreenHeight){
                    dy = 0;
                }
                scrollBy(0, dy);
                mLastY = y;
                break;
            }
            case MotionEvent.ACTION_UP:{
                mEnd = getScrollY();
                int dScrollY = mEnd - mStart;
                if (dScrollY > 0){
                    if (dScrollY < mScreenHeight / 3){
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }else {
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    }
                }else{
                    if (-dScrollY < mScreenHeight / 3){
                        mScroller.startScroll(0, getScrollY(), 0 , -dScrollY);
                    }else{
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight-dScrollY);
                    }
                }
                break;
            }
        }
        postInvalidate();
        return false;
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}
