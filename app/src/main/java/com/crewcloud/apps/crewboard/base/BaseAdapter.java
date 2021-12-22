package com.crewcloud.apps.crewboard.base;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on 6/7/16.
 */
public abstract class BaseAdapter<T, V extends ViewHolder> extends RecyclerView.Adapter<V> {
    private int lastPosition = -1;
    protected BaseActivity mActivity;
    protected Context mContext;
    protected List<T> list;


    public BaseAdapter(BaseActivity mActivity) {
        this.mActivity = mActivity;
        this.list = new ArrayList<>();
    }

    public BaseAdapter(Context context) {
        this.mContext = context;
        this.list = new ArrayList<>();
    }
    public BaseAdapter(BaseActivity mActivity, boolean enableAnimation) {
        this.mActivity = mActivity;
        this.list = new ArrayList<>();
    }

//    /**
//     * Here is the key method to apply the animation
//     */
//    private void setAnimation(View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            Animation animation = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_in_left);
//            viewToAnimate.startAnimation(animation);
//            lastPosition = position;
//        }
//    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }


    public List<T> getList() {
        return list;
    }


    public void add(T data) {
        if (data == null) {
            return;
        }
        list.add(data);
        notifyItemInserted(list.size());
    }

    public void add(int position, T data) {
        if (data == null) {
            return;
        }
        if (position < 0) {
            return;
        }
        list.add(position, data);
        notifyItemInserted(list.size());
    }

    public void addAll(List<T> data) {
        if (data == null) {
            return;
        }
        int pos = getItemCount();
        list.addAll(data);
        notifyItemRangeChanged(pos, list.size());
    }

    public void clearAndAddAll(List<T> data) {
        if (data == null) {
            return;
        }
        list.clear();
        addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


    public void deleteItem(int pos) {
        list.remove(pos);
        notifyItemRemoved(pos);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);
    }


    public T getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return list.get(position);
        }
        return null;
    }

    public List<T> getItems() {
        return list;
    }

    public BaseActivity getContext() {
        return mActivity;
    }


}
