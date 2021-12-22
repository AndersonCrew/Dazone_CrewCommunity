package com.crewcloud.apps.crewboard.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.v2.GroupLeftMenuViewHolder;
import com.crewcloud.apps.crewboard.activity.v2.LeftMenuAdapterV2;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 3/7/2017.
 */

public class GroupAdapter extends BaseAdapter<ChildFolders, RecyclerView.ViewHolder> {


    public GroupAdapter(Context context) {
        super(context);
    }


    private List<ChildFolders> leftMenus = new ArrayList<>();
    private BaseActivity mActivity;

    public void addAll(List<ChildFolders> data) {
        if (data == null) {
            return;
        }
        int pos = getItemCount();
        leftMenus.addAll(data);
        notifyItemRangeChanged(pos, leftMenus.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new GroupAdapter.GroupViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu_group, parent, false));
        } else {
            return new GroupAdapter.ChildViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_left_menu, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((GroupAdapter.GroupViewHolder) holder).bind();
        } else {
            ((GroupAdapter.ChildViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (leftMenus.get(position).getFolderNo() > 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return leftMenus.size();
    }

    public ChildFolders getItem(int position) {
        if (position >= 0 && position < getItemCount()) {
            return leftMenus.get(position);
        }
        return null;
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_group_menu)
        TextView tvGroupMenu;

        @Bind(R.id.item_leftmenu_lv_childgroup)
        RecyclerView lvChildGroup;

        @Bind(R.id.item_leftmenu_lv_childleft)
        RecyclerView lvChild;

        GroupAdapter adapter;
        ChildBoardsGroupAdapter childBoardsAdapter;

        GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new GroupAdapter(mActivity);
            childBoardsAdapter = new ChildBoardsGroupAdapter(mActivity);


        }

        public void bind() {
            ChildFolders childFolders = leftMenus.get(getAdapterPosition());
            lvChildGroup.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            lvChildGroup.setAdapter(adapter);
            if (childFolders.getLstChildFolders() != null && childFolders.getLstChildBoards().size() > 0) {
                adapter.addAll(childFolders.getLstChildFolders());
            }
            lvChild.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            lvChild.setAdapter(childBoardsAdapter);
            if (childFolders.getLstChildBoards() != null && childFolders.getLstChildBoards().size() > 0) {
                childBoardsAdapter.addAll(childFolders.getLstChildBoards());

            }

            tvGroupMenu.setText(leftMenus.get(getAdapterPosition()).getName());
        }


    }


    public class ChildViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_menu)
        TextView tvMenu;

        public ChildViewHolder(View itemView) {
            super(itemView);
            try {
                ButterKnife.bind(this, itemView);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void bind(final int position) {
            final ChildBoards leftMenu = leftMenus.get(position).getLstChildBoards().get(position);
            tvMenu.setText(leftMenu.getName());
        }
    }


}
