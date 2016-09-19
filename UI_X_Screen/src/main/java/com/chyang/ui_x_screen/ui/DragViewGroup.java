package com.chyang.ui_x_screen.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
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

    private boolean mIsKeyboardActive = false; //　输入法是否激活
    private int mKeyboardHeight = 0; // 输入法高度
    private KeyboardLayoutListener mListener;

    private View mTopView;
    private View mContentView;
    private Scroller mScroller;




    public DragViewGroup(Context context) {
        super(context);
    }

    public DragViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new LinearInterpolator());
        getViewTreeObserver().addOnGlobalLayoutListener(new KeyboardOnGlobalChangeListener());

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

        if(getChildCount() > 2) throw  new RuntimeException("view count error");
        if(mTopView == null) mTopView = getChildAt(0);
        if(mContentView == null) mContentView = getChildAt(1);

        measureChild(mTopView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mContentView,widthMeasureSpec, heightMeasureSpec - mGapHeight);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    public void setGapHeight(int gapHeight) {
        this.mGapHeight = gapHeight;
        scrollTo(0, -mGapHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mContentView != null && mTopView != null) {
            int mTopViewWidth = mTopView.getMeasuredWidth();
            int mTopViewHeight = mTopView.getMeasuredHeight();
            int mContentViewWidth = mContentView.getMeasuredWidth();
            int mContentViewHeight = mContentView.getMeasuredHeight();
            if(DBUG) Log.d(TAG,"onLayout: mTopViewWidth:"+mTopViewWidth +"  mTopViewHeight:"+mTopViewHeight + " mContentViewWidth:"+mContentViewWidth +"   mContentViewHeight:"+ mContentViewHeight);
            mContentView.layout(0, 0, mContentViewWidth, mContentViewHeight);
            mTopView.layout(0, -mContentViewHeight , mTopViewWidth,  0);
        }
    }



    private void snapToState(int whichState , int scrollValue, int duration) {


        //TODO chyang  next  FocusedChild   YES ?
//        boolean isChange = mCurrentDragState != whichState;
//        View focusedChild = getFocusedChild();

        int targetValue =Math.abs(Math.abs(getScrollY()) - Math.abs(scrollValue + mGapHeight));
        if(DBUG)Log.d(TAG,"limit scroll condition targetValue:"+targetValue);
        if(whichState == DRAG_STATE_DOWN && targetValue <= 100) return;

        enableChildrenCache();
        int changeValue = whichState - 1;
        int gap = changeValue  == 0 ? -mGapHeight : mGapHeight;
        int newY = changeValue *getMeasuredHeight();
        int startY = getScrollY();
        int dy = newY - getScrollY() + gap;
        if(scrollValue != -1) {
            int adsDy = Math.abs(dy);
            int adsScrollValue =  Math.abs(scrollValue);
            dy = (whichState == DRAG_STATE_UP ? 1 : - 1) * (adsDy > adsScrollValue ? adsScrollValue : adsDy);
        }
        mScroller.startScroll(0, startY, 0, dy, duration);
        invalidate();
    }






    public void startDown() {
        snapToState(DRAG_STATE_DOWN, -1, 800);
        mCurrentDragState = DRAG_STATE_DOWN;
    }

    public void startUp() {
        snapToState(DRAG_STATE_UP, -1, 800);
        mCurrentDragState = DRAG_STATE_UP;
    }

    public void startDown(int scrollValue, int duration) {
        snapToState(DRAG_STATE_DOWN, scrollValue, duration);
        mCurrentDragState = DRAG_STATE_DOWN;
    }

    public void startUp(int scrollValue, int duration) {
        snapToState(DRAG_STATE_UP, scrollValue, duration);
        mCurrentDragState = DRAG_STATE_UP;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final int scrollY = getScrollY();
        super.dispatchDraw(canvas);
        canvas.translate(0, scrollY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()) {
           scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
          // System.out.println((float) (mScroller.getCurrY()));
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

    private class KeyboardOnGlobalChangeListener implements ViewTreeObserver.OnGlobalLayoutListener {

        int mScreenHeight = 0;

        private int getScreenHeight() {
            if (mScreenHeight > 0) {
                return mScreenHeight;
            }
            mScreenHeight = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getHeight();
            return mScreenHeight;
        }

        @Override
        public void onGlobalLayout() {
            Rect rect = new Rect();
            // 获取当前页面窗口的显示范围
            ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            int screenHeight = getScreenHeight();
            int keyboardHeight = screenHeight - rect.bottom; // 输入法的高度
            boolean isActive = false;
            if (Math.abs(keyboardHeight) > screenHeight / 5) {
                isActive = true; // 超过屏幕五分之一则表示弹出了输入法
                mKeyboardHeight = keyboardHeight;
                int scrollValue = screenHeight - keyboardHeight - mGapHeight * 2;
                startDown(scrollValue, 1000);
            } else  {

            }
            mIsKeyboardActive = isActive;
            if (mListener != null) {
                mListener.onKeyboardStateChanged(isActive, keyboardHeight);
            }
        }
    }

    public void setKeyboardListener(KeyboardLayoutListener listener) {
        mListener = listener;
    }

    public KeyboardLayoutListener getKeyboardListener() {
        return mListener;
    }

    public boolean isKeyboardActive() {
        return mIsKeyboardActive;
    }


    public int getKeyboardHeight() {
        return mKeyboardHeight;
    }

    public interface KeyboardLayoutListener {

        void onKeyboardStateChanged(boolean isActive, int keyboardHeight);
    }
}
