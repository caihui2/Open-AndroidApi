package com.chyang.ui_x_screen.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chyang.ui_x_screen.R;
import com.chyang.ui_x_screen.Utils.AnimationHelper;
import com.chyang.ui_x_screen.adapter.X_ScreenAdapter;
import com.chyang.ui_x_screen.enter.Actor;
import com.chyang.ui_x_screen.fragment.BlankFragment;
import com.chyang.ui_x_screen.ui.LovelyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class XScreenActivity extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener{

    private static final String TAG = "XScreenActivity";
    private LovelyRecyclerView mRecyclerView;
    private List<Actor> mActorList;
    private X_ScreenAdapter mBaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private AnimationHelper pickUpAnimationHelper;

    private ImageButton mSerchView;
    private ImageButton mSerchViewIn;
    private ImageButton ivDrop;
    private EditText mEditText;

    private RelativeLayout rtMainGroup;
    private RelativeLayout rtInputViewGroup;
    private LinearLayout llListsGroup;
    private LinearLayout llSearchContentGroup;


    private boolean isDropAnimator = true;
    private boolean isUpAnimator = false;
    private int upHeight = -1;
    private int dropHeight = -1;


//    private ValueAnimator mDropAnimator;
     private ValueAnimator mUpAnimator;



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
        mLinearLayoutManager =  new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mBaseAdapter = new X_ScreenAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.scheduleLayoutAnimation();
        mRecyclerView.setOnScrollHeaderOffsetListener(mOnScrollHeaderOffsetListener);



        mSerchView = (ImageButton) findViewById(R.id.iv_search);
        mSerchViewIn = (ImageButton) findViewById(R.id.iv_search_in);
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        ivDrop = (ImageButton) findViewById(R.id.iv_drop);
        ivDrop.setOnClickListener(this);
        ivDrop.setAlpha(0.0f);
        ivDrop.setVisibility(View.GONE);

        rtMainGroup = (RelativeLayout)findViewById(R.id.rl_main_group);
        rtInputViewGroup = (RelativeLayout)findViewById(R.id.rt_input);
        llListsGroup = (LinearLayout)findViewById(R.id.ll_lists_group);
        pickUpAnimationHelper = new AnimationHelper(mRecyclerView, mBaseAdapter);
        llSearchContentGroup = (LinearLayout)findViewById(R.id.ll_search_content_group);
        llSearchContentGroup.setAlpha(0.0f);
        rtMainGroup.getViewTreeObserver().addOnGlobalLayoutListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(pickUpAnimationHelper != null) {
            pickUpAnimationHelper.doRecycleWaste();
            pickUpAnimationHelper = null;
        }
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        rtMainGroup.getWindowVisibleDisplayFrame(r);
        int screenHeight = rtMainGroup.getRootView().getHeight();
        int heightDifference = screenHeight - (r.bottom - r.top);
        float topHeight = getResources().getDimension(R.dimen.actionbar_height);
        if(heightDifference > 200) {
            dropHeight = (int) (r.bottom - topHeight * 2) - r.top;
            upHeight = dropHeight;
            if(isDropAnimator && !isUpAnimator) {
                isDropAnimator =false;
                rtInputViewGroup.setTranslationY(-dropHeight);
                llSearchContentGroup.setTranslationY(-dropHeight);
                RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                rtInputViewGroup.setLayoutParams(mLayoutParams);
                AnimatorSet mDropAnimatorSet = new AnimatorSet();
                ObjectAnimator inputViewGroupTY =  ObjectAnimator.ofFloat(rtInputViewGroup, "translationY", -dropHeight, 0);
                ObjectAnimator llSearchContentGroupTY = ObjectAnimator.ofFloat(llSearchContentGroup,"translationY", -dropHeight, 0);
                ObjectAnimator llListGroupTY = ObjectAnimator.ofFloat(llListsGroup,"translationY", 0, dropHeight);
                ObjectAnimator llSearchContentGroupAlpha = ObjectAnimator.ofFloat(llSearchContentGroup , "alpha", 0f, 1f);
                ObjectAnimator llListGroupAlpha = ObjectAnimator.ofFloat(llListsGroup, "alpha", 1f, 0f);
                ObjectAnimator ivDrpAlpha = ObjectAnimator.ofFloat(ivDrop, "alpha", 0f, 1f);
                mDropAnimatorSet.play(inputViewGroupTY).with(llSearchContentGroupTY).with(ivDrpAlpha).with(llListGroupTY).with(llListGroupAlpha).with(llSearchContentGroupAlpha);
                mDropAnimatorSet.addListener(mDropAnimatorListener);
                mDropAnimatorSet.setDuration(1000);
                mDropAnimatorSet.start();

            }
        } else {
            upHeight = (int) (screenHeight - topHeight * 2 - r.top);
        }
    }




    private AnimatorListenerAdapter mDropAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
          FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
            BlankFragment mBlankFragment = new BlankFragment();
            mFragmentTransaction.replace(R.id.ll_search_content_group, mBlankFragment);
            mFragmentTransaction.commit();
            ivDrop.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            llListsGroup.setVisibility(View.GONE);
            isUpAnimator = true;
        }
    };


    private AnimatorListenerAdapter mUpAnimatorListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            llListsGroup.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rtMainGroup.getWindowToken(), 0);

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            ivDrop.setVisibility(View.GONE);
            isDropAnimator =true;
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_drop && !isDropAnimator  && isUpAnimator) {
            isUpAnimator = false;
            rtInputViewGroup.setTranslationY(upHeight);
            llSearchContentGroup.setTranslationY(upHeight);
            RelativeLayout.LayoutParams mLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rtInputViewGroup.setLayoutParams(mLayoutParams);
            AnimatorSet mUpAnimatorSet = new AnimatorSet();
            ObjectAnimator inputViewGroupTY =  ObjectAnimator.ofFloat(rtInputViewGroup, "translationY", upHeight, 0);
            ObjectAnimator llSearchContentGroupTY = ObjectAnimator.ofFloat(llSearchContentGroup,"translationY", upHeight, 0);
            ObjectAnimator llListGroupTY = ObjectAnimator.ofFloat(llListsGroup,"translationY", upHeight, 0);
            ObjectAnimator llListGroupAlpha = ObjectAnimator.ofFloat(llListsGroup, "alpha", 0f, 1f);
            ObjectAnimator llSearchContentGroupAlpha = ObjectAnimator.ofFloat(llSearchContentGroup , "alpha", 1f, 0f);
            ObjectAnimator ivDrpAlpha = ObjectAnimator.ofFloat(ivDrop, "alpha", 1f, 0f);
            mUpAnimatorSet.play(inputViewGroupTY).with(llSearchContentGroupTY).with(ivDrpAlpha).with(llListGroupTY).with(llListGroupAlpha).with(llSearchContentGroupAlpha);
            mUpAnimatorSet.addListener(mUpAnimatorListener);
            mUpAnimatorSet.setDuration(1000);
            mUpAnimatorSet.start();

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
}
