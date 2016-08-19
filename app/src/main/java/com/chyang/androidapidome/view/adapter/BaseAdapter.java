package com.chyang.androidapidome.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chyang.androidapidome.R;
import com.chyang.androidapidome.view.enter.Actor;

import java.util.List;

/**
 * Created by chyang on 2016/8/12.
 */
public class BaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    public static final int HEADER_VIEW = 0;
    public static final int BASE_VIEW = 1;


    private List<Actor> mActors;
    private Context mContext;

    public BaseAdapter(Context context) {
        mContext = context;
    }

    public void setActor(List<Actor> actor){
        mActors = actor;
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
            mBaseViewHolder.mTextView.setText(p.name);
            mBaseViewHolder.ivCover.setImageResource(p.res);
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

//        private EditText ivHeader;
        private View mView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            itemView.setVisibility(View.INVISIBLE);
        }
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageView ivCover;

        public BaseViewHolder( View v ) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            ivCover = (ImageView) v.findViewById(R.id.pic);
        }
    }
}
