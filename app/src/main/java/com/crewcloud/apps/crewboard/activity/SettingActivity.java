package com.crewcloud.apps.crewboard.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.BaseActivity;
import com.crewcloud.apps.crewboard.activity.logintest.LoginActivity;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.interfaces.BaseHTTPCallBack;
import com.crewcloud.apps.crewboard.interfaces.GetUserCallBack;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DialogUtil;
import com.crewcloud.apps.crewboard.util.HttpRequest;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Util;
import com.crewcloud.apps.crewboard.util.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

public class SettingActivity extends BaseActivity implements View.OnClickListener, GetUserCallBack {
    private ImageView img_avatar;
    private LinearLayout ln_profile, ln_general, ln_notify, ln_logout, ln_about;
    public PreferenceUtilities prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_page_layout);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        prefs = CrewBoardApplication.getInstance().getPreferenceUtilities();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.myColor_PrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_back_ic);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ln_profile = findViewById(R.id.ln_profile);
        ln_general = findViewById(R.id.ln_general);
        ln_notify = findViewById(R.id.ln_notify);
        ln_logout = findViewById(R.id.ln_logout);
        ln_about = findViewById(R.id.ln_about);
        ln_profile.setOnClickListener(this);
        ln_general.setOnClickListener(this);
        ln_notify.setOnClickListener(this);
        ln_logout.setOnClickListener(this);
        ln_about.setOnClickListener(this);
        img_avatar = findViewById(R.id.img_avatar);
        String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getUserAvatar();

        if (url.contains("/Images/Avatar.jpg") || url.isEmpty())
            HttpRequest.getInstance().GetUser(prefs.getCurrentUserNo(), this);
        Util.showImage(url, img_avatar);

    }

    @Override
    public void onClick(View v) {
        if (v == ln_profile) {
            BaseActivity.Instance.callActivity(ProfileUserActivity.class);
        } else if (v == ln_general) {
            Toast.makeText(getApplicationContext(), "undev", Toast.LENGTH_SHORT).show();
        } else if (v == ln_notify) {
            BaseActivity.Instance.callActivity(NotificationSettingActivity.class);
        } else if (v == ln_logout) {
            logout();
        } else if (v == ln_about) {
            String title = getString(R.string.about) + " " + getString(R.string.app_name);
            String versionName = BuildConfig.VERSION_NAME;
            String user_version = getResources().getString(R.string.user_version) + " " + versionName;
            DialogUtil.oneButtonAlertDialog(this, title, user_version, getString(R.string.confirm));
        }
    }

    public void logout() {
        DialogUtil.customAlertDialog(this, getString(R.string.are_you_sure_loguot),
                getString(R.string.auto_login_button_yes), getString(R.string.auto_login_button_no), new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {
                        deletedAndroidDevice();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });

    }

    private void deletedAndroidDevice() {
        HttpRequest.getInstance().deleteAndroidDevice(new BaseHTTPCallBack() {
            @Override
            public void onHTTPSuccess() {
                new WebClientAsync_Logout_v2().execute();
            }

            @Override
            public void onHTTPFail(ErrorDto errorDto) {
                new WebClientAsync_Logout_v2().execute();
            }
        });
    }

    @Override
    public void onGetUserSuccess(Profile profile) {
        prefs.setUserAvatar(profile.avatar);
        Util.showImage(profile.avatar, img_avatar);
    }

    @Override
    public void onError() {
        Util.printLogs("GetUser Error");
    }

    private class WebClientAsync_Logout_v2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            final PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

            WebClient.Logout_v2(preferenceUtilities.getCurrentMobileSessionId(),
                    preferenceUtilities.getString(Constants.DOMAIN, ""), new WebClient.OnWebClientListener() {
                        @Override
                        public void onSuccess(JsonNode jsonNode) {
                        }

                        @Override
                        public void onFailure() {
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
            preferenceUtilities.setCurrentMobileSessionId("");
            preferenceUtilities.setCurrentCompanyNo(0);
            preferenceUtilities.setCurrentUserID("");
            preferenceUtilities.setUserAvatar("");
            preferenceUtilities.clearNotificationSetting();
            CrewBoardApplication.getInstance().removeShortcut();
            BaseActivity.Instance.startSingleActivity(LoginActivity.class);
            finish();
        }
    }
}