package com.crewcloud.apps.crewboard.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.adapter.GroupAdapter;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseDialog;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.event.EventGroupList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 3/7/2017.
 */

public class GroupDialog extends BaseDialog {

    @Bind(R.id.dialog_job_lv_job)
    RecyclerView lvJob;

    private GroupAdapter adapter;
    private List<ChildFolders> datas;
    private Context activity;

    public GroupDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_job);
        ButterKnife.bind(this);
    }


    public void setDatas(List<ChildFolders> datas) {
        this.datas = datas;
    }

    @Override
    public void show() {
        super.show();
        adapter = new GroupAdapter(getContext());
        lvJob.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvJob.setAdapter(adapter);
        adapter.addAll(datas);
    }


}
