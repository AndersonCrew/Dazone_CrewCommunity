package com.crewcloud.apps.crewboard.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;


import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dazone on 1/10/2017.
 */

public class EditCommentDialog extends BaseDialog {

    @Bind(R.id.dialog_edit_et_comment)
    EditText etComment;
    private String comment;

    public EditCommentDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_comment);
        ButterKnife.bind(this);
    }

    @Override
    public void show() {
        super.show();
        if (!TextUtils.isEmpty(comment)) {
            etComment.setText(comment);
        }
    }

    @OnClick(R.id.dialog_edit_bt_ok)
    public void onClickOk() {
        dismiss();
        if (getListener() != null) {
            getListener().onPositive(etComment.getText().toString());
        }
    }

    @OnClick(R.id.dialog_edit_bt_cancel)
    public void onClickCancel() {
        dismiss();
        if (getListener() != null) {
            getListener().onNegative();
        }
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
