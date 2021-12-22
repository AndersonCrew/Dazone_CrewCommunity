package com.crewcloud.apps.crewboard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.v2.ChildLeftMenuViewHolder;
import com.crewcloud.apps.crewboard.activity.v2.GroupLeftMenuViewHolder;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;


/**
 * Created by dazone on 2/20/2017.
 */

public class LeftMenuAdapter extends ExpandableRecyclerViewAdapter<GroupLeftMenuViewHolder, ChildLeftMenuViewHolder> {

    public LeftMenuAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public GroupLeftMenuViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_group, parent, false);
        return new GroupLeftMenuViewHolder(view);
    }

    @Override
    public ChildLeftMenuViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_child, parent, false);
        return new ChildLeftMenuViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildLeftMenuViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
    }

    @Override
    public void onBindGroupViewHolder(GroupLeftMenuViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind(group);
    }


}