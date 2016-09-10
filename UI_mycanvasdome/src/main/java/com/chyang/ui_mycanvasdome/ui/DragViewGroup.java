package com.chyang.ui_mycanvasdome.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by chyang on 2016/9/10.
 */
public class DragViewGroup extends ViewGroup {

    private static final boolean DBUG = true;
    private static final String TAG = "DragViewGroup";
    public static final int DRAG_STATE_INVALID = -1;
    public static final int DRAG_STATE_DOWN = 0;
    public static final int DRAG_STATE_UP = 1;

    private int mCurrentDragState = DRAG_STATE_UP;
    private int mGapHeight = 0;

    private View mTopView;
    private View mContentView;
    private Scroller mScroller;




    public DragViewGroup(Context context) {
        super(context);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);

    }

    public DragViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthMode  = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        if(DBUG) Log.d(TAG, " widthMode:"+ widthMode+"  heightMode:"+ heightMode + "    sizeWidth:"+sizeWidth +"   sizeHeight: "+sizeHeight);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        if(getChildCount() > 2) throw  new RuntimeException("view count error");
        if(mTopView == null) mTopView = getChildAt(0);
        if(mContentView == null) mContentView = getChildAt(1);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    public void setGapHeight(int gapHeight) {
        this.mGapHeight = gapHeight;
        scrollTo(0,-gapHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mContentView != null && mTopView != null) {
            int mTopViewWidth = mTopView.getMeasuredWidth();
            int mTopViewHeight = mTopView.getMeasuredHeight();
            int mContentViewWidth = mContentView.getMeasuredWidth();
            int mContentViewHeight = mContentView.getMeasuredHeight();
            if(DBUG) Log.d(TAG,"onLayout: mTopViewWidth:"+mTopViewWidth +"  mTopViewHeight:"+mTopViewHeight + " mContentViewWidth:"+mContentViewWidth +"   mContentViewHeight:"+ mContentViewHeight);
            mContentView.layout(l, t, mContentViewWidth, mContentViewHeight);
            mTopView.layout(l, -mContentViewHeight, mTopViewWidth, mTopViewHeight);
        }
    }


    private void snapToState(int whichState) {
        enableChildrenCache();
        //TODO chyang  next  FocusedChild   YES ?
//        boolean isChange = mCurrentDragState != whichState;
//        View focusedChild = getFocusedChild();
        int changeValue = whichState - 1;
        int gap = changeValue == 0 ? -mGapHeight : mGapHeight;
        int newY = changeValue * getMeasuredHeight();
        int startY = getScrollY();
        int dy = newY - getScrollY() + gap;
        mScroller.startScroll(0, startY, 0, dy, 2000);
        invalidate();
    }


    public void startDown() {
        snapToState(DRAG_STATE_DOWN);
        mCurrentDragState = DRAG_STATE_DOWN;
    }

    public void startUp() {
        snapToState(DRAG_STATE_UP);
        mCurrentDragState = DRAG_STATE_UP;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()) {
           scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
        } else {
            clearChildrenCache();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        postInvalidate();
    }

    void enableChildrenCache() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View layout = (View) getChildAt(i);
            layout.setDrawingCacheEnabled(true);
        }
    }

    void clearChildrenCache() {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View layout = (View) getChildAt(i);
            layout.setDrawingCacheEnabled(false);
        }
    }
}
