package com.chyang.ui_x_screen.Utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by chyang on 2016/8/29.
 */
public class AnimationHelper {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    private EasyItemAnimator mItemAnimator;
    private SimpleItemTouchHelperCallback mItemTouchCallback;
    private ItemTouchHelper mItemTouchHelper;
    private boolean isChange = false;

    public AnimationHelper(RecyclerView recyclerView , RecyclerView.Adapter adapter) {
        mAdapter = adapter;
        mRecyclerView = recyclerView;

        //Animator
        mItemAnimator = new PoliteItemAnimator();
        mRecyclerView.setItemAnimator(mItemAnimator);

        //Touch Helper
        mItemTouchCallback = new PoliteItemTouchHelperCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, -1);
        mItemTouchHelper = new ItemTouchHelper(mItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }


    public ItemTouchHelper getItemTouchHelper() {
        return mItemTouchHelper;
    }


//
//    public void startChange(boolean isChange, Card card) {
//        this.isChange = isChange;
//      //  this.mCurrentClickCard = card;
//        mAdapter.notifyItemRangeChanged(1, mAdapter.getItemCount());
//        mRecyclerView.requestLayout();
//    }

    public void  doRecycleWaste() {
        if(mAdapter != null) mAdapter = null;
        if(mRecyclerView != null) mRecyclerView = null;
        if(mItemAnimator != null) mItemAnimator = null;
        if(mItemTouchHelper != null) mItemTouchHelper = null;
        if(mItemTouchCallback != null) mItemTouchCallback = null;
    }

    class PoliteItemAnimator extends EasyItemAnimator {

    }

    class PoliteItemTouchHelperCallback extends SimpleItemTouchHelperCallback{

        public PoliteItemTouchHelperCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }
    }
}
