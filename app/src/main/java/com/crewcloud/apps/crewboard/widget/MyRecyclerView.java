package com.crewcloud.apps.crewboard.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.listener.EndlessRecyclerViewScrollListener;
import com.crewcloud.apps.crewboard.listener.OnRecyclerViewClickListener;
import com.crewcloud.apps.crewboard.listener.RecyclerTouchListener;


/**
 * Created by mb on 8/16/16.
 */

public class MyRecyclerView extends RelativeLayout {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyRecyclerViewListener myRecyclerViewListener;
    private boolean finishLoadMore;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private OnRecyclerViewClickListener onRecyclerViewClickListener;
    private boolean isRefresh;

    public MyRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_my_recyclerview, this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        setLayoutManager(new LinearLayoutManager(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (endlessRecyclerViewScrollListener != null) {
                    endlessRecyclerViewScrollListener.reset();
                }

                if (myRecyclerViewListener != null) {
                    myRecyclerViewListener.onRefresh();
                }
            }
        });
        if (onRecyclerViewClickListener != null) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, onRecyclerViewClickListener));
        }

        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setPadding(0, 0, 0, 0);
    }

    public void setLoadMore(boolean isShow) {
        if (progressBar == null) {
            return;
        }
        progressBar.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setRefreshing(boolean isShow) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setRefreshing(isShow);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setLayoutManager(layoutManager);
        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (myRecyclerViewListener != null) {
                    myRecyclerViewListener.onLoadMore(page - 1, totalItemsCount);
                }
            }

            @Override
            public void onScrolled(int lastVisibleItemPosition, int firstVisibleItemPosition) {
                if (myRecyclerViewListener == null) {
                    return;
                }

                if (lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1 && !finishLoadMore) {
                    progressBar.setVisibility(VISIBLE);
                } else if (lastVisibleItemPosition >= 0) {
                    progressBar.setVisibility(GONE);
                }
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.setAdapter(adapter);
    }

    public void scrollToPosition(int position) {
        if (recyclerView == null) {
            return;
        }
        recyclerView.scrollToPosition(position);
    }

    public void setMyRecyclerViewListener(MyRecyclerViewListener myRecyclerViewListener) {
        this.myRecyclerViewListener = myRecyclerViewListener;
    }

    public void setNotShowLoadMore(boolean finishLoadMore) {
        this.finishLoadMore = finishLoadMore;
    }

    public void stopShowLoading() {
        setRefreshing(false);
        setLoadMore(false);
    }

    public void setEnableSwipeRefresh(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }

    public void setOnRecyclerViewClickListener(OnRecyclerViewClickListener onRecyclerViewClickListener) {
        this.onRecyclerViewClickListener = onRecyclerViewClickListener;
        if (onRecyclerViewClickListener != null) {
            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, onRecyclerViewClickListener));
        }
    }

    public interface MyRecyclerViewListener {
        void onRefresh();

        void onLoadMore(int page, int totalItemsCount);
    }

    public void setRefresh(boolean refresh) {
        if (refresh) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }
}
