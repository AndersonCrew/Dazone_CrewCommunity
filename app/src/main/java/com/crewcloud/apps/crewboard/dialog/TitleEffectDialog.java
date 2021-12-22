package com.crewcloud.apps.crewboard.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseDialog;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Prefs;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dazone on 3/24/2017.
 */

public class TitleEffectDialog extends BaseDialog {


    private int regUserNo;


    public TitleEffectDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_effect);
        ButterKnife.bind(this);
    }

    @Override
    public void show() {
        super.show();
        Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
    }

}

