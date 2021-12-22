package com.crewcloud.apps.crewboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.v2.LeftMenuAdapterV2;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.event.EventChildBoard;
import com.crewcloud.apps.crewboard.event.EventGroupList;
import com.crewcloud.apps.crewboard.event.MenuItem;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 3/3/2017.
 */

public class ChildBoardsAdapter extends BaseAdapter<ChildBoards, ChildBoardsAdapter.ViewHolder> {


    public ChildBoardsAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu, parent, false);
        return new ChildBoardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_menu)
        TextView tvMenu;

        public ViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventGroupList eventChildBoard = new EventGroupList(getItem(getAdapterPosition()));
                    EventBus.getDefault().post(eventChildBoard);
                }
            });
        }

        public void bind(final int position) {
            final ChildBoards leftMenu = getItems().get(position);
            tvMenu.setText(leftMenu.getName());


        }
    }


}
