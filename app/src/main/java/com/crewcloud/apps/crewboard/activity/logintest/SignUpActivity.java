package com.crewcloud.apps.crewboard.activity.logintest;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.interfaces.BaseHTTPCallBackWithString;
import com.crewcloud.apps.crewboard.util.HttpRequest;

public class SignUpActivity extends BaseActivity {
    private EditText mEtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initToolBar();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.myColor_PrimaryDark));
        }

        mEtEmail = findViewById(R.id.sign_up_edt_email);
        RelativeLayout mBtnSignUp = findViewById(R.id.login_btn_register);

        if (mEtEmail != null) {
            mEtEmail.setSelection(0);
        }

        if (mBtnSignUp != null) {
            mBtnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = mEtEmail.getText().toString().trim();
                    signUp(email);
                }
            });
        }
    }

    private void signUp(String email) {
        HttpRequest.getInstance().signUp(new BaseHTTPCallBackWithString() {
            @Override
            public void onHTTPSuccess(String message) {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onHTTPFail(ErrorDto errorDto) {
                Toast.makeText(SignUpActivity.this, errorDto.message, Toast.LENGTH_LONG).show();
                mEtEmail.requestFocus();
            }
        }, email);
    }

    public void initToolBar() {
        Toolbar toolbar = findViewById(R.id.tbSignUpToolbar);

        if (toolbar == null) {
            return;
        }

        toolbar.setTitle(R.string.title_sign_up_screen);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.nav_back_ic);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}