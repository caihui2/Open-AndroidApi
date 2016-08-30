package com.chyang.ui_x_screen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.chyang.ui_x_screen.R;
import com.chyang.ui_x_screen.enter.Actor;

import java.util.List;

/**
 * Created by chyang on 2016/8/12.
 */
public class X_ScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static final int HEADER_VIEW = 0;
    public static final int BASE_VIEW = 1;
    private boolean isHide = false;
    private boolean isPlayAnimation = false;
    private int selectPosition = -1;

    public OnItemClickListener itemClickListener;


    public interface OnItemClickListener{
         public void onItemClick(int position, RecyclerView.ViewHolder mViewHolder);
    }


    private List<Actor> mActors;
    private Context mContext;

    public X_ScreenAdapter(Context context) {
        mContext = context;
    }

    public void setActor(List<Actor> actor){
        mActors = actor;
    }

    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        if(viewType == HEADER_VIEW) {
            View v = mLayoutInflater.inflate(R.layout.xscreem_item_header_view, parent, false);
            mViewHolder = new HeaderViewHolder(v);
        } else {
            View v = mLayoutInflater.inflate(R.layout.xscreen_card_view, parent, false);
            mViewHolder = new BaseViewHolder(v);
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  BaseViewHolder) {
            BaseViewHolder  mBaseViewHolder = (BaseViewHolder) holder;
            Actor p = mActors.get(position -1);
            TextView mTextView = mBaseViewHolder.mTextView;
            ImageView mImageView = mBaseViewHolder.ivCover;
            mTextView.setText(p.name);
            mImageView.setImageResource(p.res);
            float translationX = (float) ((mBaseViewHolder.itemView.getWidth() - mBaseViewHolder.itemView.getWidth() * 0.5f) /2);
            if(isHide) {
                if(position != selectPosition) {
                    ViewCompat.setTranslationX(mBaseViewHolder.itemView, -256f);
                    ViewCompat.setAlpha(mBaseViewHolder.itemView, 0.5f);
                    ViewCompat.setScaleX(mBaseViewHolder.itemView, 0.5f);
                } else{
                    ViewCompat.setAlpha(mBaseViewHolder.itemView, 1.0f);
                    ViewCompat.setScaleX(mBaseViewHolder.itemView, 1.0f);
                    ViewCompat.setTranslationX(mBaseViewHolder.itemView, 0f);
                }
                mImageView.setVisibility(View.GONE);
                mTextView.setBackgroundColor(Color.BLACK);

            }  else {
                ViewCompat.setAlpha(mBaseViewHolder.itemView, 1.0f);
                ViewCompat.setScaleX(mBaseViewHolder.itemView, 1.0f);
                mBaseViewHolder.itemView.setTranslationX(0.0f);
                mImageView.setVisibility(View.VISIBLE);
                mTextView.setBackgroundColor(Color.TRANSPARENT);

            }

        } else if(holder instanceof HeaderViewHolder) {}
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER_VIEW : BASE_VIEW;
    }

    @Override
    public int getItemCount() {
        return mActors == null ? 0 : mActors.size() + 1;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setVisibility(View.INVISIBLE);
        }
    }



    class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public Button mTextView;
        public ImageView ivCover;

        public BaseViewHolder( View v ) {
            super(v);
            mTextView = (Button) v.findViewById(R.id.name);
            ivCover = (ImageView) v.findViewById(R.id.pic);
            mTextView.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            selectPosition = getAdapterPosition();
            if(itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition(), this);
            }
            return false;
        }
    }
}
