package com.chyang.androidapidome.view.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.chyang.androidapidome.R;
import com.chyang.androidapidome.view.adapter.X_ScreenAdapter;
import com.chyang.androidapidome.view.enter.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class XScreenActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "XScreenActivity";
    private RecyclerView mRecyclerView;
    private List<Actor> mActorList;
    private X_ScreenAdapter mBaseAdapter;
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
        mRecyclerView.setItemAnimator(new MyItemAnimator());
        // 设置固定大小
        mRecyclerView.setHasFixedSize(true);

        mSerchView = (ImageButton) findViewById(R.id.iv_search);
        mSerchView.setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.ed);
        mBaseAdapter = new X_ScreenAdapter(this);
        mBaseAdapter.setActor(mActorList);
        mRecyclerView.setAdapter(mBaseAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.scheduleLayoutAnimation();
        mRts = (ViewGroup)findViewById(R.id.rls);
        headerViewHeight = getResources().getDimension(R.dimen.x_screen_header_height);



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
                System.out.println(viewHolder.getAdapterPosition() +"==========="+dX +"=---------"+dY);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                System.out.println(viewHolder.getAdapterPosition() +"=====childDraw======"+dX +"=---------"+dY);
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
                 //   mBaseAdapter.notifyDataSetChanged();
                    mItemTouchHelper.startDrag(mViewHolder);

                } else{
                    mBaseAdapter.setIsHide(false);
                    mBaseAdapter.notifyItemRangeChanged(1, mBaseAdapter.getItemCount());
                 //   mBaseAdapter.notifyDataSetChanged();
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                if(!mBaseAdapter.isHide()) {
                    mBaseAdapter.setIsHide(true);
                    mBaseAdapter.notifyDataSetChanged();
                } else{
                    mBaseAdapter.setIsHide(false);
                    mBaseAdapter.notifyDataSetChanged();
                }
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

    static class  MyItemAnimator extends SimpleItemAnimator {
        private static final boolean DEBUG = true;

        private ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
        private ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
        private ArrayList<MoveInfo> mPendingMoves = new ArrayList<>();
        private ArrayList<ChangeInfo> mPendingChanges = new ArrayList<>();

        private ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<>();
        private ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<>();
        private ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<>();

        private ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
        private ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
        private ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
        private ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();

        private static class MoveInfo {
            public RecyclerView.ViewHolder holder;
            public int fromX, fromY, toX, toY;

            private MoveInfo(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                this.holder = holder;
                this.fromX = fromX;
                this.fromY = fromY;
                this.toX = toX;
                this.toY = toY;
            }
        }

        private static class ChangeInfo {
            public RecyclerView.ViewHolder oldHolder, newHolder;
            public int fromX, fromY, toX, toY;
            private ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder) {
                this.oldHolder = oldHolder;
                this.newHolder = newHolder;
            }

            private ChangeInfo(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                               int fromX, int fromY, int toX, int toY) {
                this(oldHolder, newHolder);
                this.fromX = fromX;
                this.fromY = fromY;
                this.toX = toX;
                this.toY = toY;
            }

            @Override
            public String toString() {
                return "ChangeInfo{" +
                        "oldHolder=" + oldHolder +
                        ", newHolder=" + newHolder +
                        ", fromX=" + fromX +
                        ", fromY=" + fromY +
                        ", toX=" + toX +
                        ", toY=" + toY +
                        '}';
            }
        }

        @Override
        public void runPendingAnimations() {
            if(DEBUG) Log.e(TAG, "runPendingAnimations");
            boolean removalsPending = !mPendingRemovals.isEmpty();
            boolean movesPending = !mPendingMoves.isEmpty();
            boolean changesPending = !mPendingChanges.isEmpty();
            boolean additionsPending = !mPendingAdditions.isEmpty();
            if (!removalsPending && !movesPending && !additionsPending && !changesPending) {
                // nothing to animate
                return;
            }
            // First, remove stuff
            for (RecyclerView.ViewHolder holder : mPendingRemovals) {
                animateRemoveImpl(holder);
            }
            mPendingRemovals.clear();
            // Next, move stuff
            if (movesPending) {
                final ArrayList<MoveInfo> moves = new ArrayList<>();
                moves.addAll(mPendingMoves);
                mMovesList.add(moves);
                mPendingMoves.clear();
                Runnable mover = new Runnable() {
                    @Override
                    public void run() {
                        for (MoveInfo moveInfo : moves) {
                            animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY,
                                    moveInfo.toX, moveInfo.toY);
                        }
                        moves.clear();
                        mMovesList.remove(moves);
                    }
                };
                if (removalsPending) {
                    View view = moves.get(0).holder.itemView;
                    ViewCompat.postOnAnimationDelayed(view, mover, getRemoveDuration());
                } else {
                    mover.run();
                }
            }
            // Next, change stuff, to run in parallel with move animations
            if (changesPending) {
                final ArrayList<ChangeInfo> changes = new ArrayList<>();
                changes.addAll(mPendingChanges);
                mChangesList.add(changes);
                mPendingChanges.clear();
                Runnable changer = new Runnable() {
                    @Override
                    public void run() {
                        for (ChangeInfo change : changes) {
                            animateChangeImpl(change);
                        }
                        changes.clear();
                        mChangesList.remove(changes);
                    }
                };
                if (removalsPending) {
                    RecyclerView.ViewHolder holder = changes.get(0).oldHolder;
                    ViewCompat.postOnAnimationDelayed(holder.itemView, changer, getRemoveDuration());
                } else {
                    changer.run();
                }
            }
            // Next, add stuff
            if (additionsPending) {
                final ArrayList<RecyclerView.ViewHolder> additions = new ArrayList<>();
                additions.addAll(mPendingAdditions);
                mAdditionsList.add(additions);
                mPendingAdditions.clear();
                Runnable adder = new Runnable() {
                    public void run() {
                        for (RecyclerView.ViewHolder holder : additions) {
                            animateAddImpl(holder);
                        }
                        additions.clear();
                        mAdditionsList.remove(additions);
                    }
                };
                if (removalsPending || movesPending || changesPending) {
                    long removeDuration = removalsPending ? getRemoveDuration() : 0;
                    long moveDuration = movesPending ? getMoveDuration() : 0;
                    long changeDuration = changesPending ? getChangeDuration() : 0;
                    long totalDelay = removeDuration + Math.max(moveDuration, changeDuration);
                    View view = additions.get(0).itemView;
                    ViewCompat.postOnAnimationDelayed(view, adder, totalDelay);
                } else {
                    adder.run();
                }
            }
        }

        @Override
        public boolean animateRemove(final RecyclerView.ViewHolder holder) {
            if(DEBUG) Log.e(TAG, "animateRemove");
            resetAnimation(holder);
            mPendingRemovals.add(holder);
            return true;
        }

        private void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
            if(DEBUG) Log.e(TAG, "animateRemoveImpl");
            final View view = holder.itemView;
            final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
            mRemoveAnimations.add(holder);
            animation.setDuration(getRemoveDuration())
                    .alpha(0).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchRemoveStarting(holder);
                }

                @Override
                public void onAnimationEnd(View view) {
                    animation.setListener(null);
                    ViewCompat.setAlpha(view, 1);
                    dispatchRemoveFinished(holder);
                    mRemoveAnimations.remove(holder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }

        @Override
        public boolean animateAdd(final RecyclerView.ViewHolder holder) {
            if(DEBUG) Log.e(TAG, "animateAdd");
            resetAnimation(holder);
            ViewCompat.setAlpha(holder.itemView, 0);
            mPendingAdditions.add(holder);
            return true;
        }

        private void animateAddImpl(final RecyclerView.ViewHolder holder) {
            if(DEBUG) Log.e(TAG, "animateAddImpl");
            final View view = holder.itemView;
            final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
            mAddAnimations.add(holder);
            animation.alpha(1).setDuration(getAddDuration()).
                    setListener(new VpaListenerAdapter() {
                        @Override
                        public void onAnimationStart(View view) {
                            dispatchAddStarting(holder);
                        }
                        @Override
                        public void onAnimationCancel(View view) {
                            ViewCompat.setAlpha(view, 1);
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            animation.setListener(null);
                            dispatchAddFinished(holder);
                            mAddAnimations.remove(holder);
                            dispatchFinishedWhenDone();
                        }
                    }).start();
        }

        @Override
        public boolean animateMove(final RecyclerView.ViewHolder holder, int fromX, int fromY,
                                   int toX, int toY) {
            if(DEBUG) Log.e(TAG, "animateMove");
            final View view = holder.itemView;
            fromX += ViewCompat.getTranslationX(holder.itemView);
            fromY += ViewCompat.getTranslationY(holder.itemView);
            resetAnimation(holder);
            int deltaX = toX - fromX;
            int deltaY = toY - fromY;
            if (deltaX == 0 && deltaY == 0) {
                dispatchMoveFinished(holder);
                return false;
            }
            if (deltaX != 0) {
                ViewCompat.setTranslationX(view, -deltaX);
            }
            if (deltaY != 0) {
                ViewCompat.setTranslationY(view, -deltaY);
            }
            mPendingMoves.add(new MoveInfo(holder, fromX, fromY, toX, toY));
            return true;
        }

        private void animateMoveImpl(final RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
            if(DEBUG) Log.e(TAG, "animateMoveImpl");
            final View view = holder.itemView;
            final int deltaX = toX - fromX;
            final int deltaY = toY - fromY;
            if (deltaX != 0) {
                ViewCompat.animate(view).translationX(0);
            }
            if (deltaY != 0) {
                ViewCompat.animate(view).translationY(0);
            }
            // TODO: make EndActions end listeners instead, since end actions aren't called when
            // vpas are canceled (and can't end them. why?)
            // need listener functionality in VPACompat for this. Ick.
            final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
            mMoveAnimations.add(holder);
            animation.setDuration(getMoveDuration()).setListener(new VpaListenerAdapter() {
                @Override
                public void onAnimationStart(View view) {
                    dispatchMoveStarting(holder);
                }
                @Override
                public void onAnimationCancel(View view) {
                    if (deltaX != 0) {
                        ViewCompat.setTranslationX(view, 0);
                    }
                    if (deltaY != 0) {
                        ViewCompat.setTranslationY(view, 0);
                    }
                }
                @Override
                public void onAnimationEnd(View view) {
                    animation.setListener(null);
                    dispatchMoveFinished(holder);
                    mMoveAnimations.remove(holder);
                    dispatchFinishedWhenDone();
                }
            }).start();
        }

        @Override
        public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder,
                                     int fromX, int fromY, int toX, int toY) {
            if(DEBUG) Log.e(TAG, "animateChange");
            if (oldHolder == newHolder) {
                // Don't know how to run change animations when the same view holder is re-used.
                // run a move animation to handle position changes.
                return animateMove(oldHolder, fromX, fromY, toX, toY);
            }
            final float prevTranslationX = ViewCompat.getTranslationX(oldHolder.itemView);
            final float prevTranslationY = ViewCompat.getTranslationY(oldHolder.itemView);
            final float prevAlpha = ViewCompat.getAlpha(oldHolder.itemView);
            resetAnimation(oldHolder);
            int deltaX = (int) (toX - fromX - prevTranslationX);
            int deltaY = (int) (toY - fromY - prevTranslationY);
            // recover prev translation state after ending animation
            ViewCompat.setTranslationX(oldHolder.itemView, prevTranslationX);
            ViewCompat.setTranslationY(oldHolder.itemView, prevTranslationY);
            ViewCompat.setAlpha(oldHolder.itemView, prevAlpha);
            if (newHolder != null) {
                // carry over translation values
                resetAnimation(newHolder);
                ViewCompat.setTranslationX(newHolder.itemView, -deltaX);
                ViewCompat.setTranslationY(newHolder.itemView, -deltaY);
                ViewCompat.setAlpha(newHolder.itemView, 0);
            }
            mPendingChanges.add(new ChangeInfo(oldHolder, newHolder, fromX, fromY, toX, toY));
            return true;
        }

        private void animateChangeImpl(final ChangeInfo changeInfo) {
            if(DEBUG) Log.e(TAG, "animateChangeImpl");
            final RecyclerView.ViewHolder holder = changeInfo.oldHolder;
            final View view = holder == null ? null : holder.itemView;
            final RecyclerView.ViewHolder newHolder = changeInfo.newHolder;
            final View newView = newHolder != null ? newHolder.itemView : null;
            if (view != null) {
                final ViewPropertyAnimatorCompat oldViewAnim = ViewCompat.animate(view).setDuration(
                        getChangeDuration());
                mChangeAnimations.add(changeInfo.oldHolder);
                oldViewAnim.translationX(changeInfo.toX - changeInfo.fromX);
                oldViewAnim.translationY(changeInfo.toY - changeInfo.fromY);
                oldViewAnim.alpha(0).setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchChangeStarting(changeInfo.oldHolder, true);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        oldViewAnim.setListener(null);
                        ViewCompat.setAlpha(view, 1);
                        ViewCompat.setTranslationX(view, 0);
                        ViewCompat.setTranslationY(view, 0);
                        dispatchChangeFinished(changeInfo.oldHolder, true);
                        mChangeAnimations.remove(changeInfo.oldHolder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
            }
            if (newView != null) {
                final ViewPropertyAnimatorCompat newViewAnimation = ViewCompat.animate(newView);
                mChangeAnimations.add(changeInfo.newHolder);
                newViewAnimation.translationX(0).translationY(0).setDuration(getChangeDuration()).
                        alpha(1).setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchChangeStarting(changeInfo.newHolder, false);
                    }
                    @Override
                    public void onAnimationEnd(View view) {
                        newViewAnimation.setListener(null);
                        ViewCompat.setAlpha(newView, 0.5f);
                        ViewCompat.setTranslationX(newView, 0);
                        ViewCompat.setTranslationY(newView, 0);
                        dispatchChangeFinished(changeInfo.newHolder, false);
                        mChangeAnimations.remove(changeInfo.newHolder);
                        dispatchFinishedWhenDone();
                    }
                }).start();
            }
        }

        private void endChangeAnimation(List<ChangeInfo> infoList, RecyclerView.ViewHolder item) {
            if(DEBUG) Log.e(TAG, "endChangeAnimation");
            for (int i = infoList.size() - 1; i >= 0; i--) {
                ChangeInfo changeInfo = infoList.get(i);
                if (endChangeAnimationIfNecessary(changeInfo, item)) {
                    if (changeInfo.oldHolder == null && changeInfo.newHolder == null) {

                        infoList.remove(changeInfo);
                    }
                }
            }
        }

        private void endChangeAnimationIfNecessary(ChangeInfo changeInfo) {
            if(DEBUG) Log.e(TAG, "endChangeAnimationIfNecessary");
            if (changeInfo.oldHolder != null) {
                endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
            }
            if (changeInfo.newHolder != null) {
                endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
            }
        }
        private boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder item) {
            if(DEBUG) Log.e(TAG, "endChangeAnimationIfNecessary");
            boolean oldItem = false;
            if (changeInfo.newHolder == item) {
                changeInfo.newHolder = null;
            } else if (changeInfo.oldHolder == item) {
                changeInfo.oldHolder = null;
                oldItem = true;
            } else {
                return false;
            }
            ViewCompat.setAlpha(item.itemView, 1);
            ViewCompat.setTranslationX(item.itemView, 0);
            ViewCompat.setTranslationY(item.itemView, 0);
            dispatchChangeFinished(item, oldItem);
            return true;
        }

        @Override
        public void endAnimation(RecyclerView.ViewHolder item) {
            if(DEBUG) Log.e(TAG, "endAnimation");
            final View view = item.itemView;
            // this will trigger end callback which should set properties to their target values.
            ViewCompat.animate(view).cancel();
            // TODO if some other animations are chained to end, how do we cancel them as well?
            for (int i = mPendingMoves.size() - 1; i >= 0; i--) {
                MoveInfo moveInfo = mPendingMoves.get(i);
                if (moveInfo.holder == item) {
                    ViewCompat.setTranslationY(view, 0);
                    ViewCompat.setTranslationX(view, 0);
                    dispatchMoveFinished(item);
                    mPendingMoves.remove(i);
                }
            }
            endChangeAnimation(mPendingChanges, item);
            if (mPendingRemovals.remove(item)) {
                ViewCompat.setAlpha(view, 1);
                dispatchRemoveFinished(item);
            }
            if (mPendingAdditions.remove(item)) {
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
            }

            for (int i = mChangesList.size() - 1; i >= 0; i--) {
                ArrayList<ChangeInfo> changes = mChangesList.get(i);
                endChangeAnimation(changes, item);
                if (changes.isEmpty()) {
                    mChangesList.remove(i);
                }
            }
            for (int i = mMovesList.size() - 1; i >= 0; i--) {
                ArrayList<MoveInfo> moves = mMovesList.get(i);
                for (int j = moves.size() - 1; j >= 0; j--) {
                    MoveInfo moveInfo = moves.get(j);
                    if (moveInfo.holder == item) {
                        ViewCompat.setTranslationY(view, 0);
                        ViewCompat.setTranslationX(view, 0);
                        dispatchMoveFinished(item);
                        moves.remove(j);
                        if (moves.isEmpty()) {
                            mMovesList.remove(i);
                        }
                        break;
                    }
                }
            }
            for (int i = mAdditionsList.size() - 1; i >= 0; i--) {
                ArrayList<RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
                if (additions.remove(item)) {
                    ViewCompat.setAlpha(view, 1);
                    dispatchAddFinished(item);
                    if (additions.isEmpty()) {
                        mAdditionsList.remove(i);
                    }
                }
            }

            // animations should be ended by the cancel above.
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (mRemoveAnimations.remove(item) && DEBUG) {
                throw new IllegalStateException("after animation is cancelled, item should not be in "
                        + "mRemoveAnimations list");
            }

            //noinspection PointlessBooleanExpression,ConstantConditions
            if (mAddAnimations.remove(item) && DEBUG) {
                throw new IllegalStateException("after animation is cancelled, item should not be in "
                        + "mAddAnimations list");
            }

            //noinspection PointlessBooleanExpression,ConstantConditions
            if (mChangeAnimations.remove(item) && DEBUG) {
                throw new IllegalStateException("after animation is cancelled, item should not be in "
                        + "mChangeAnimations list");
            }

            //noinspection PointlessBooleanExpression,ConstantConditions
            if (mMoveAnimations.remove(item) && DEBUG) {
                throw new IllegalStateException("after animation is cancelled, item should not be in "
                        + "mMoveAnimations list");
            }
            dispatchFinishedWhenDone();
        }

        private void resetAnimation (RecyclerView.ViewHolder holder) {
            if(DEBUG) Log.e(TAG, "resetAnimation");
            AnimatorCompatHelper.clearInterpolator(holder.itemView);
            endAnimation(holder);
        }

        @Override
        public boolean isRunning() {
            if(DEBUG) Log.e(TAG, "isRunning"+(!mPendingAdditions.isEmpty() ||
                    !mPendingChanges.isEmpty() ||
                    !mPendingMoves.isEmpty() ||
                    !mPendingRemovals.isEmpty() ||
                    !mMoveAnimations.isEmpty() ||
                    !mRemoveAnimations.isEmpty() ||
                    !mAddAnimations.isEmpty() ||
                    !mChangeAnimations.isEmpty() ||
                    !mMovesList.isEmpty() ||
                    !mAdditionsList.isEmpty() ||
                    !mChangesList.isEmpty()));


            return (!mPendingAdditions.isEmpty() ||
                    !mPendingChanges.isEmpty() ||
                    !mPendingMoves.isEmpty() ||
                    !mPendingRemovals.isEmpty() ||
                    !mMoveAnimations.isEmpty() ||
                    !mRemoveAnimations.isEmpty() ||
                    !mAddAnimations.isEmpty() ||
                    !mChangeAnimations.isEmpty() ||
                    !mMovesList.isEmpty() ||
                    !mAdditionsList.isEmpty() ||
                    !mChangesList.isEmpty());
        }

        /**
         * Check the state of currently pending and running animations. If there are none
         * pending/running, call {@link #dispatchAnimationsFinished()} to notify any
         * listeners.
         */
        private void dispatchFinishedWhenDone() {
            if(DEBUG) Log.e(TAG, "dispatchFinishedWhenDone");
            if (!isRunning()) {
                dispatchAnimationsFinished();
            }
        }

        @Override
        public void endAnimations() {
            if(DEBUG) Log.e(TAG, "endAnimations");
            int count = mPendingMoves.size();
            for (int i = count - 1; i >= 0; i--) {
                MoveInfo item = mPendingMoves.get(i);
                View view = item.holder.itemView;
                ViewCompat.setTranslationY(view, 0);
                ViewCompat.setTranslationX(view, 0);
                dispatchMoveFinished(item.holder);
                mPendingMoves.remove(i);
            }
            count = mPendingRemovals.size();
            for (int i = count - 1; i >= 0; i--) {
                RecyclerView.ViewHolder item = mPendingRemovals.get(i);
                dispatchRemoveFinished(item);
                mPendingRemovals.remove(i);
            }
            count = mPendingAdditions.size();
            for (int i = count - 1; i >= 0; i--) {
                RecyclerView.ViewHolder item = mPendingAdditions.get(i);
                View view = item.itemView;
                ViewCompat.setAlpha(view, 1);
                dispatchAddFinished(item);
                mPendingAdditions.remove(i);
            }
            count = mPendingChanges.size();
            for (int i = count - 1; i >= 0; i--) {
                endChangeAnimationIfNecessary(mPendingChanges.get(i));
            }
            mPendingChanges.clear();
            if (!isRunning()) {
                return;
            }

            int listCount = mMovesList.size();
            for (int i = listCount - 1; i >= 0; i--) {
                ArrayList<MoveInfo> moves = mMovesList.get(i);
                count = moves.size();
                for (int j = count - 1; j >= 0; j--) {
                    MoveInfo moveInfo = moves.get(j);
                    RecyclerView.ViewHolder item = moveInfo.holder;
                    View view = item.itemView;
                    ViewCompat.setTranslationY(view, 0);
                    ViewCompat.setTranslationX(view, 0);
                    dispatchMoveFinished(moveInfo.holder);
                    moves.remove(j);
                    if (moves.isEmpty()) {
                        mMovesList.remove(moves);
                    }
                }
            }
            listCount = mAdditionsList.size();
            for (int i = listCount - 1; i >= 0; i--) {
                ArrayList<RecyclerView.ViewHolder> additions = mAdditionsList.get(i);
                count = additions.size();
                for (int j = count - 1; j >= 0; j--) {
                    RecyclerView.ViewHolder item = additions.get(j);
                    View view = item.itemView;
                    ViewCompat.setAlpha(view, 1);
                    dispatchAddFinished(item);
                    additions.remove(j);
                    if (additions.isEmpty()) {
                        mAdditionsList.remove(additions);
                    }
                }
            }
            listCount = mChangesList.size();
            for (int i = listCount - 1; i >= 0; i--) {
                ArrayList<ChangeInfo> changes = mChangesList.get(i);
                count = changes.size();
                for (int j = count - 1; j >= 0; j--) {
                    endChangeAnimationIfNecessary(changes.get(j));
                    if (changes.isEmpty()) {
                        mChangesList.remove(changes);
                    }
                }
            }

            cancelAll(mRemoveAnimations);
            cancelAll(mMoveAnimations);
            cancelAll(mAddAnimations);
            cancelAll(mChangeAnimations);

            dispatchAnimationsFinished();
        }

        void cancelAll(List<RecyclerView.ViewHolder> viewHolders) {
            if(DEBUG) Log.e(TAG, "cancelAll");
            for (int i = viewHolders.size() - 1; i >= 0; i--) {
                ViewCompat.animate(viewHolders.get(i).itemView).cancel();
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * If the payload list is not empty, DefaultItemAnimator returns <code>true</code>.
         * When this is the case:
         * <ul>
         * <li>If you override {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)}, both
         * ViewHolder arguments will be the same instance.
         * </li>
         * <li>
         * If you are not overriding {@link #animateChange(RecyclerView.ViewHolder, RecyclerView.ViewHolder, int, int, int, int)},
         * then DefaultItemAnimator will call {@link #animateMove(RecyclerView.ViewHolder, int, int, int, int)} and
         * run a move animation instead.
         * </li>
         * </ul>
         */
        @Override
        public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,
                                                 @NonNull List<Object> payloads) {
            if(DEBUG) Log.e(TAG, "canReuseUpdatedViewHolder");
            return !payloads.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, payloads);
        }

        private static class VpaListenerAdapter implements ViewPropertyAnimatorListener {
            @Override
            public void onAnimationStart(View view) {}

            @Override
            public void onAnimationEnd(View view) {}

            @Override
            public void onAnimationCancel(View view) {}
        }

    }

}
