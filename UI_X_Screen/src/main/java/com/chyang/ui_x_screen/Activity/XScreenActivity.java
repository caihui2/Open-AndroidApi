package com.chyang.ui_x_screen.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chyang.ui_x_screen.R;
import com.chyang.ui_x_screen.Utils.AnimationHelper;
import com.chyang.ui_x_screen.adapter.X_ScreenAdapter;
import com.chyang.ui_x_screen.enter.Actor;
import com.chyang.ui_x_screen.fragment.BlankFragment;
import com.chyang.ui_x_screen.ui.DragViewGroup;
import com.chyang.ui_x_screen.ui.LovelyRecyclerView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class XScreenActivity extends AppCompatActivity implements View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "XScreenActivity";
    private RecyclerView mRecyclerView;
    private List<Actor> mActorList;
    private X_ScreenAdapter mBaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    private ImageButton mSerchView;
    private ImageButton mSerchViewIn;
    private ImageButton ivDrop;
    private EditText mEditText;


    private RelativeLayout rtMainGroup;
    private RelativeLayout rtInputViewGroup;
    private DragViewGroup rvContentGroup;
    private ViewGroup llListsGroup;
    private LinearLayout llSearchContentGroup;

    private float headViewHeight;
    private InputMethodManager imm;

    private static final int DROP_MOVE_NORM = 300;
    private float dropHeight;
    private float upHeight;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xsreen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mBaseAdapter = new X_ScreenAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        headViewHeight = getResources().getDimension(R.dimen.actionbar_height);


        mSerchView = (ImageButton) findViewById(R.id.iv_search);
        mSerchViewIn = (ImageButton) findViewById(R.id.iv_search_in);
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        ivDrop = (ImageButton) findViewById(R.id.iv_drop);
        ivDrop.setAlpha(0f);
        mEditText.setOnEditorActionListener(mOnEditorActionListener);

        rtMainGroup = (RelativeLayout) findViewById(R.id.rl_main_group);
        rtInputViewGroup = (RelativeLayout) findViewById(R.id.rt_input);
        llListsGroup = (ViewGroup) findViewById(R.id.ll_lists_group);
        rvContentGroup = (DragViewGroup) findViewById(R.id.rl_content_group);
        llSearchContentGroup = (LinearLayout) findViewById(R.id.ll_search_content_group);
        rvContentGroup.setGapHeight((int) headViewHeight);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onGlobalLayout() {
        Rect mRect = new Rect();
        rtMainGroup.getWindowVisibleDisplayFrame(mRect);
        int screenHeight = rtMainGroup.getRootView().getHeight();
        int visibleDisplayHeight = mRect.bottom - mRect.top;
        int keyboardHeight = screenHeight - visibleDisplayHeight;
        if (keyboardHeight > DROP_MOVE_NORM) {
            dropHeight = visibleDisplayHeight - headViewHeight * 2;
            upHeight = dropHeight;
            System.out.println("laile " + dropHeight);
            rvContentGroup.startDown((int) dropHeight, 1000);
        } else {
            upHeight = screenHeight - headViewHeight * 2 - mRect.top;
        }
    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ed) {
            System.out.println("lailehah");
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            float offset = Math.max((headViewHeight - getTopScrollOffset(recyclerView)) / headViewHeight, 0f);
            searchBarChange(offset);
        }
    };


    private void searchBarChange(float offset) {
        mEditText.setAlpha(offset);
        if (offset == 1) {
            mSerchView.setVisibility(View.GONE);
            mSerchViewIn.setVisibility(View.VISIBLE);
        } else {
            mSerchViewIn.setVisibility(View.GONE);
            mSerchView.setVisibility(View.VISIBLE);
            mEditText.getText().clear();
//            edSearchIn.clearFocus();
        }
        mSerchView.setTranslationY(mSerchView.getHeight() * offset);
    }


    private float getTopScrollOffset(RecyclerView view) {
        View cHeadView = mLinearLayoutManager.getChildAt(0);
        if (cHeadView == null) return 0;
        int cTopPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int cTopOffset = cHeadView.getTop();
        float headHeight = 0;
        if (cTopPosition >= 1) headHeight = headViewHeight;
        return -cTopOffset + cTopPosition * cHeadView.getHeight() + headHeight;
    }

    private TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            System.out.println("laile ha");
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            return false;
        }
    };

}
