package com.crewcloud.apps.crewboard.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.LoginActivity;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DeviceUtilities;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.lang.ref.WeakReference;

public class IntroActivityBackup extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    private final ActivityHandler mActivityHandler = new ActivityHandler(this);

    private static class ActivityHandler extends Handler {
        private final WeakReference<IntroActivityBackup> mWeakActivity;

        public ActivityHandler(IntroActivityBackup activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final IntroActivityBackup activity = mWeakActivity.get();

            if (activity != null) {
                if (msg.what == 2) {
                    activity.startApplication();
                } else if (msg.what == 1) {
                }
            }
        }
    }

    // ----------------------------------------------------------------------------------------------

    private void startApplication() {
        PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

        if (!TextUtils.isEmpty(preferenceUtilities.getCurrentMobileSessionId())) {
            new WebClientAsync_HasApplication_v2().execute();
        } else {
            preferenceUtilities.setCurrentMobileSessionId("");
            preferenceUtilities.setCurrentCompanyNo(0);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // ----------------------------------------------------------------------------------------------

    private class WebClientAsync_HasApplication_v2 extends AsyncTask<Void, Void, Void> {
        private boolean mIsFailed;
        private boolean mHasApplication;
        private String mMessage;

        @Override
        protected Void doInBackground(Void... params) {
            PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

            WebClient.HasApplication_v2(DeviceUtilities.getLanguageCode(), DeviceUtilities.getTimeZoneOffset(), CrewBoardApplication.getProjectCode(),
                    preferenceUtilities.getString(Constants.DOMAIN, ""), new WebClient.OnWebClientListener() {
                        @Override
                        public void onSuccess(JsonNode jsonNode) {
                            try {
                                mIsFailed = false;
                                mHasApplication = jsonNode.get("HasApplication").asBoolean();
                                mMessage = jsonNode.get("Message").asText();
                            } catch (Exception e) {
                                e.printStackTrace();

                                mIsFailed = true;
                                mHasApplication = false;
                                mMessage = getString(R.string.loginActivity_message_wrong_server_site);
                            }
                        }

                        @Override
                        public void onFailure() {
                            mIsFailed = true;
                            mHasApplication = false;
                            mMessage = getString(R.string.loginActivity_message_wrong_server_site);
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mIsFailed) {
                if (!mMessage.isEmpty())
                    Toast.makeText(IntroActivityBackup.this, mMessage, Toast.LENGTH_LONG).show();
                finish();
            } else {
                if (mHasApplication) {
                    new WebClientAsync_CheckSessionUser_v2().execute();
                } else {
                    if (!mMessage.isEmpty())
                        Toast.makeText(IntroActivityBackup.this, mMessage, Toast.LENGTH_LONG).show();
                    new WebClientAsync_Logout_v2().execute();
                }
            }
        }
    }

    private class WebClientAsync_CheckSessionUser_v2 extends AsyncTask<Void, Void, Void> {
        private boolean mIsSuccess;

        @Override
        protected Void doInBackground(Void... params) {
            PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

            WebClient.CheckSessionUser_v2(DeviceUtilities.getLanguageCode(),
                    DeviceUtilities.getTimeZoneOffset(), preferenceUtilities.getCurrentMobileSessionId(),
                    preferenceUtilities.getString(Constants.DOMAIN, ""),
                    new WebClient.OnWebClientListener() {
                        @Override
                        public void onSuccess(JsonNode jsonNode) {
                            try {
                                mIsSuccess = (jsonNode.get("success").asInt() == 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                mIsSuccess = false;
                            }
                        }

                        @Override
                        public void onFailure() {
                            mIsSuccess = false;
                        }
                    });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mIsSuccess) {
                Intent intent = new Intent(IntroActivityBackup.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

                preferenceUtilities.setCurrentMobileSessionId("");
                preferenceUtilities.setCurrentCompanyNo(0);

                Intent intent = new Intent(IntroActivityBackup.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private class WebClientAsync_Logout_v2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();

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

            finish();
        }
    }
}