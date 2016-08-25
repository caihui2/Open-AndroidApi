package com.chyang.ui_x_screen.Activity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chyang.ui_x_screen.R;
import com.chyang.ui_x_screen.adapter.X_ScreenAdapter;
import com.chyang.ui_x_screen.enter.Actor;
import com.chyang.ui_x_screen.ui.LovelyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class XScreenActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "XScreenActivity";
    private LovelyRecyclerView mRecyclerView;
    private List<Actor> mActorList;
    private X_ScreenAdapter mBaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ImageButton mSerchView;
    private ViewGroup mRts;
    private EditText mEditText;

    private float headerViewHeight;
    private boolean isChange = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsreen);
        mRecyclerView = (LovelyRecyclerView) findViewById(R.id.list);
        mActorList = new ArrayList<Actor>();
        Actor anglababy = new Actor("anglababy", R.mipmap.anglababy);
        Actor fanbingbing = new Actor("范冰冰", R.mipmap.fanbingbing);
        Actor limengyin = new Actor("李梦颖", R.mipmap.limengyin);
        Actor linxinru = new Actor("林心如", R.mipmap.linxinru);
        Actor tangyan = new Actor("唐嫣", R.mipmap.tangyan);
        Actor yangmi = new Actor("杨幂", R.mipmap.yangmi);
        Actor zhaoliyin = new Actor("赵丽颖", R.mipmap.zhaoliyin);
//        mActorList.add(anglababy);
//        mActorList.add(anglababy);
//        mActorList.add(anglababy);
//        mActorList.add(anglababy);
//        mActorList.add(anglababy);
        mActorList.add(anglababy);
        mActorList.add(fanbingbing);
        mActorList.add(limengyin);
        mActorList.add(linxinru);
        mActorList.add(tangyan);
        mActorList.add(yangmi);
        mActorList.add(zhaoliyin);

        // 设置LinearLayoutManager
        mLinearLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        // 设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        mRecyclerView.setHasFixedSize(true);

        mSerchView = (ImageButton) findViewById(R.id.iv_search);
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        mBaseAdapter = new X_ScreenAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.scheduleLayoutAnimation();
        mRts = (ViewGroup)findViewById(R.id.rls);
        headerViewHeight = getResources().getDimension(R.dimen.x_screen_header_height);
        mRecyclerView.setOnScrollHeaderOffsetListener(mOnScrollHeaderOffsetListener);


        //TODO Temporarily not never ItemTouchHelper
       final ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, -1) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int position = viewHolder.getAdapterPosition();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

           @Override
           public boolean isLongPressDragEnabled() {
               return false;
           }

           @Override
           public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
               super.onSelectedChanged(viewHolder, actionState);
           }

           @Override
           public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               super.clearView(recyclerView, viewHolder);
           }

           @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
             //   System.out.println(viewHolder.getAdapterPosition() +"==========="+dX +"=---------"+dY);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            //    System.out.println(viewHolder.getAdapterPosition() +"=====childDraw======"+dX +"=---------"+dY);
            }
        };

        final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);


        mBaseAdapter.setOnItemClickListener(new X_ScreenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerView.ViewHolder mViewHolder) {
                if(!mBaseAdapter.isHide()) {

                    mBaseAdapter.setIsHide(true);
                    mBaseAdapter.notifyItemRangeChanged(1, mBaseAdapter.getItemCount());
                    mRecyclerView.requestLayout();
                    mItemTouchHelper.startDrag(mViewHolder);

                } else{
                    mBaseAdapter.setIsHide(false);
                    mRecyclerView.requestLayout();
                    mBaseAdapter.notifyItemRangeChanged(1, mBaseAdapter.getItemCount());

                }
            }
        });



    }


    private LovelyRecyclerView.OnScrollHeaderOffsetListener mOnScrollHeaderOffsetListener = new LovelyRecyclerView.OnScrollHeaderOffsetListener() {
        @Override
        public float getHeaderHeight() {
            return getResources().getDimension(R.dimen.x_screen_header_height);
        }

        @Override
        public void onHeaderScrollOffset(float offset) {
            mEditText.setAlpha(offset);
            mSerchView.setTranslationY(mSerchView.getHeight()  * offset);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.iv_search:
//                if(!mBaseAdapter.isHide()) {
//                    mBaseAdapter.setIsHide(true);
//                    mBaseAdapter.notifyDataSetChanged();
//                } else{
//                    mBaseAdapter.setIsHide(false);
//                    mBaseAdapter.notifyDataSetChanged();
//                }
 //               break;
        }
    }

}
