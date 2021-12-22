package com.crewcloud.apps.crewboard.activity.v2;

import android.view.View;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 3/3/2017.
 */

public class ChildLeftMenuViewHolder extends ChildViewHolder {

    @Bind(R.id.tv_menu)
    TextView tvMenu;

    public ChildLeftMenuViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(String name) {
        tvMenu.setText(name);

    }
}
