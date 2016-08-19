package com.chyang.androidapidome.view.activity;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.chyang.androidapidome.R;
import com.chyang.androidapidome.view.adapter.BaseAdapter;
import com.chyang.androidapidome.view.enter.Actor;

import java.util.ArrayList;
import java.util.List;

public class XScreenActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView mRecyclerView;
    private List<Actor> mActorList;
    private BaseAdapter mBaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private ImageButton mSerchView;
    private ViewGroup mRts;
    private EditText mEditText;

    private float headerViewHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsreen);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
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
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        mBaseAdapter = new BaseAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.scheduleLayoutAnimation();
        mRts = (ViewGroup)findViewById(R.id.rls);

        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN | ItemTouchHelper.UP, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int position = viewHolder.getAdapterPosition();

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                System.out.println(viewHolder.getAdapterPosition() +"==========="+dX +"=---------"+dY);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                System.out.println(viewHolder.getAdapterPosition() +"=====childDraw======"+dX +"=---------"+dY);
            }
        };

        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(mCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        headerViewHeight = getResources().getDimension(R.dimen.x_screen_header_height);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                System.out.println("我被点击了");
                break;
        }
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            float scrollY = getScrollY(recyclerView);
            float offset = Math.max((headerViewHeight - scrollY) / headerViewHeight, 0f);
            mEditText.setAlpha(offset);
            mSerchView.setTranslationY(mSerchView.getHeight()  * offset);
        }
    };

    public float getScrollY(RecyclerView view) {
        View c = view.getChildAt(0);
        if (c == null) return 0;
        int firstVisiblePosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int top = c.getTop();
        float headerHeight = 0;
        if(firstVisiblePosition >= 1) headerHeight = headerViewHeight;
        float offset = -top + firstVisiblePosition * c.getHeight() + headerHeight;
        return offset;
    }

}
