package com.dyf.dyfkotlindemo.view.reflash;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.dyf.dyfkotlindemo.R;
import com.dyf.dyfkotlindemo.utils.DensityUtil;

/**
 * Created by dyf on 2017/6/22.
 */

public class PullableLayout extends ViewGroup {

    private View mHeader;
    private View mFooter;

    private TextView tvPullHeader;
    private ProgressBar pbPullHeader;

    private Scroller mLayoutScroller;

    private onRefreshListener mRefreshListener;

    public void setRefreshListener(onRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public PullableLayout(Context context) {
        super(context);
    }

    public PullableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeader = LayoutInflater.from(context).inflate(R.layout.header_pullable_layout, null);
        mFooter = LayoutInflater.from(context).inflate(R.layout.footer_pullable_layout, null);
        mLayoutScroller = new Scroller(context);
    }


    public PullableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PullableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    //xml加载完后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mHeader.setLayoutParams(params);
        mFooter.setLayoutParams(params);
        addView(mHeader);
        addView(mFooter);

        //
        tvPullHeader = (TextView) mHeader.findViewById(R.id.tv_pullable_header);
        pbPullHeader = (ProgressBar) mHeader.findViewById(R.id.pb_pullable_header);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
        for (int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }
    }


    private int mLayoutContentHeight;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutContentHeight = 0;
        //置位
        for (int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            if (child == mHeader){//头视图隐藏在顶端
                //左上右下
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(), 0);
            }else if(child == mFooter){//尾视图隐藏在layout所有视图之后
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
            }else{//视图内容根据定义(插入)顺序,按由上到下的顺序垂直方向排列
                child.layout(0, mLayoutContentHeight, child.getMeasuredWidth(), mLayoutContentHeight + child.getMeasuredHeight());
                mLayoutContentHeight += child.getMeasuredHeight();
            }
        }
    }


    //视图能够跟随着我们的手指滚动起来
    private int mLastMoveY;
    private int effectiveScrollY = DensityUtil.dip2px(getContext(), 50);
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        return super.onTouchEvent(event);
        int y = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = mLastMoveY - y;
                // dy < 0 代表针对下拉刷新的操作
                if (dy < 0){
                    if (Math.abs(getScrollY()) <= mHeader.getMeasuredHeight() / 2 ){
                        scrollBy(0, dy);
                        if (Math.abs(getScrollY()) >= effectiveScrollY){
                            tvPullHeader.setText("松开刷新");
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                 if (Math.abs(getScrollY()) >= effectiveScrollY) {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + effectiveScrollY));
                    tvPullHeader.setVisibility(View.GONE);
                    pbPullHeader.setVisibility(View.VISIBLE);
                    //
                    if (null != mRefreshListener) mRefreshListener.onRefresh();
                }else{
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
//                invalidate();
                break;
        }

        mLastMoveY = y;
        return true;
    }


    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        boolean intercept = false;
        //记录此次触摸事件的y坐标
        int y = (int)event.getY();

        //
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: {
                intercept = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (y > mLastMoveY) {  //下滑操作
                    View child = getChildAt(0);
                    if (child instanceof AdapterView) {
                        AdapterView adapterChild = (AdapterView) child;
                        //判断ABSListView是否已经到达内容的最顶部(如果已经到达顶部,就拦截事件)
                        if (adapterChild.getFirstVisiblePosition() == 0
                                || adapterChild.getChildAt(0).getTop() == 0) {
                            intercept = true;
                        }
                    }
                }
                break;
            }

            //up 事件
            case MotionEvent.ACTION_UP :{
                intercept = false;
                break;
            }
        }

        mLastMoveY = y;
        return intercept;
    }

    @Override
    public void computeScroll() {
        //判断Scroller是否执行完毕：
        if (mLayoutScroller.computeScrollOffset()) {
            scrollTo(mLayoutScroller.getCurrX(), mLayoutScroller.getCurrY());
            //通知View重绘-invalidate()->onDraw()->computeScroll()
            invalidate();
        }
    }

    public interface onRefreshListener{
        void onRefresh();
    }
}
