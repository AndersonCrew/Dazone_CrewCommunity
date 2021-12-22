package com.crewcloud.apps.crewboard.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseDialog;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;

import com.crewcloud.apps.crewboard.util.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dazone on 2/24/2017.
 */

public class InfoCommentDialog extends BaseDialog {

    private int regUserNo;

    @Bind(R.id.dialog_info_tv_delete)
    TextView tvDelete;

    @Bind(R.id.dialog_info_tv_edit)
    TextView tvEdit;

    public InfoCommentDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_info_comment);
        ButterKnife.bind(this);
    }

    @Override
    public void show() {
        super.show();
        Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
        if (regUserNo != prefs.getUserNo()) {
            tvDelete.setVisibility(View.GONE);
            tvEdit.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.dialog_info_tv_delete)
    public void onClickDelete() {
        if (listener != null) {
            listener.deleteListerner();
        }
    }

    @OnClick(R.id.dialog_info_tv_edit)
    public void onClickEdit() {
        if (listener != null) {
            listener.editListener();
        }
    }

    @OnClick(R.id.dialog_info_tv_copy)
    public void onclickCopy() {
        if (listener != null) {
            listener.copyListener();
        }

    }

    public void setRegUserNo(int regUserNo) {
        this.regUserNo = regUserNo;
    }

    public interface onClickInfoListener {
        void deleteListerner();

        void editListener();

        void copyListener();
    }

    private onClickInfoListener listener;


    public void setOnclickListener(onClickInfoListener listener) {
        this.listener = listener;
    }
}
