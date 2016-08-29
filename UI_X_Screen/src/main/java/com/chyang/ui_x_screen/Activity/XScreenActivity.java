package com.chyang.ui_x_screen.Activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

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
    private ImageButton mSerchViewIn;
    private ViewGroup mRts;
    private EditText mEditText;

    private float headerViewHeight;
    private boolean isChange = false;

    private RelativeLayout mainViewGroup;
    private RelativeLayout serchViewGroup;
    private RelativeLayout rtInputViewGroup;
    private boolean isAnima = true;


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
        mSerchViewIn = (ImageButton) findViewById(R.id.iv_search_in);
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        mBaseAdapter = new X_ScreenAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.scheduleLayoutAnimation();
        mRts = (ViewGroup)findViewById(R.id.rls);
        headerViewHeight = getResources().getDimension(R.dimen.x_screen_header_height);
        mRecyclerView.setOnScrollHeaderOffsetListener(mOnScrollHeaderOffsetListener);
        mainViewGroup = (RelativeLayout)findViewById(R.id.main);
         serchViewGroup = (RelativeLayout) findViewById(R.id.rls);
        rtInputViewGroup = (RelativeLayout)findViewById(R.id.rt_input);

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int windowHeight = windowManager.getDefaultDisplay().getHeight();
       // startDropAnimator(windowHeight -9);

        mainViewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainViewGroup.getWindowVisibleDisplayFrame(r);

                int screenHeight = mainViewGroup.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                System.out.println("Keyboard Size, Size: " + heightDifference);
                if(heightDifference > 200) {
                  float height =  getResources().getDimension(R.dimen.actionbar_height);
                    int moveHeight = (int) (r.bottom - height * 2 ) - r.top;
                    System.out.println(moveHeight+"=====keybrotSize:"+(r.bottom) +"===== mainScreen"+screenHeight
                    );
                    startDropAnimator(moveHeight);
                }
                //boolean visible = heightDiff > screenHeight / 3;
            }
        });






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

    private void startDropAnimator(int height) {
        ValueAnimator mValueAnimator = ValueAnimator.ofInt(0, height);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
             int value = (int) animation.getAnimatedValue();
           //     System.out.println(value+"==-=-=-=-=");
                rtInputViewGroup.setTranslationY(value);

            }
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnima =false;
                rtInputViewGroup.setTranslationY(0);
                RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rtInputViewGroup.setLayoutParams(mLayoutParams);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        if(isAnima) {
            mValueAnimator.setDuration(1000);
            mValueAnimator.start();
        }
    }


    private LovelyRecyclerView.OnScrollHeaderOffsetListener mOnScrollHeaderOffsetListener = new LovelyRecyclerView.OnScrollHeaderOffsetListener() {
        @Override
        public float getHeaderHeight() {
            return getResources().getDimension(R.dimen.x_screen_header_height);
        }

        @Override
        public void onHeaderScrollOffset(float offset) {
            mEditText.setAlpha(offset);
            if(offset == 1) {
                mSerchView.setVisibility(View.GONE);
                mSerchViewIn.setVisibility(View.VISIBLE);
            } else {
                mSerchViewIn.setVisibility(View.GONE);
                mSerchView.setVisibility(View.VISIBLE);
            }
            mSerchView.setTranslationY(mSerchView.getHeight()  * offset);
        }
    };

    @Override
    public void onClick(View v) {
    }

}
