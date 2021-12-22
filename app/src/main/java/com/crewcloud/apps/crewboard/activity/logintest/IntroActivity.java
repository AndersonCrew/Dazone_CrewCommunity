package com.crewcloud.apps.crewboard.activity.logintest;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.interfaces.OnHasAppCallBack;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DialogUtil;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.Util;
import com.crewcloud.apps.crewboard.util.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class IntroActivity extends BaseActivity implements OnHasAppCallBack {
    private Activity context;
    private String updateUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        checkLogout();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_base_color));
        }

        context = this;

        Thread thread = new Thread(new UpdateRunnable());
        thread.setDaemon(true);
        thread.start();
    }

    private void checkLogout() {
        if (Util.versionCompare(BuildConfig.VERSION_NAME, "1.3.7")  <= 0 && !CrewBoardApplication.getInstance().getPreferenceUtilities().getBoolean(Constants.IS_FIRST_INSTALL_VER, false)) {
            CrewBoardApplication.getInstance().getPreferenceUtilities().setBoolean(Constants.IS_FIRST_INSTALL_VER, true);
            if (!CrewBoardApplication.getInstance().getPreferenceUtilities().getString("company", "").isEmpty() ) {
                PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
                preferenceUtilities.setCurrentMobileSessionId("");
                preferenceUtilities.setCurrentCompanyNo(0);
                preferenceUtilities.setCurrentUserID("");
            }
        }
    }

    @Override
    public void hasApp(String url) {
        updateUrl = url;
        mActivityHandler2.sendEmptyMessage(Constants.ACTIVITY_HANDLER_START_UPDATE);
    }

    @Override
    public void noHas(ErrorDto dto) {
        mActivityHandler2.sendEmptyMessageDelayed(Constants.ACTIVITY_HANDLER_NEXT_ACTIVITY, 1);
    }

    private class WebClientAsync_HasApplication_v2 extends AsyncTask<Void, Void, Void> {
        private boolean mIsFailed;
        private boolean mHasApplication;
        private String mMessage;

        @Override
        protected Void doInBackground(Void... params) {
            Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
            if (TextUtils.isEmpty(prefs.getServerSite())) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                WebClient.HasApplication_v2(Util.getPhoneLanguage(), (int) Util.getTimeOffsetInMinute(), CrewBoardApplication.getProjectCode(), prefs.getServerSite(), new WebClient.OnWebClientListener() {
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mIsFailed) {
                if (mMessage != null && !mMessage.isEmpty())
                    Toast.makeText(IntroActivity.this, mMessage, Toast.LENGTH_LONG).show();
                finish();
            } else {
                if (mHasApplication) {
                    new WebClientAsync_CheckSessionUser_v2().execute();
                } else {
                    if (mMessage != null && !mMessage.isEmpty())
                        Toast.makeText(IntroActivity.this, mMessage, Toast.LENGTH_LONG).show();
                    new WebClientAsync_Logout_v2().execute();
                }
            }
        }
    }

    private class WebClientAsync_Logout_v2 extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            PreferenceUtilities prefs = CrewBoardApplication.getInstance().getPreferenceUtilities();
            WebClient.Logout_v2(prefs.getCurrentMobileSessionId(), prefs.getCurrentServiceDomain(), new WebClient.OnWebClientListener() {
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
            Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
            prefs.putaccesstoken("");
            finish();
        }
    }

    private class WebClientAsync_CheckSessionUser_v2 extends AsyncTask<Void, Void, Void> {
        private boolean mIsSuccess;

        @Override
        protected Void doInBackground(Void... params) {
            Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();

            WebClient.CheckSessionUser_v2(Util.getPhoneLanguage(), (int) Util.getTimeOffsetInMilis(), prefs.getaccesstoken(), prefs.getServerSite(), new WebClient.OnWebClientListener() {
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
                startApplication();
            } else {
                PreferenceUtilities prefs = CrewBoardApplication.getInstance().getPreferenceUtilities();
                prefs.setCurrentMobileSessionId("");

                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void startApplication() {
        if (TextUtils.isEmpty(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId())) {
            BaseActivity.Instance.callActivity(LoginActivity.class);
            finish();
        } else {
            BaseActivity.Instance.callActivity(com.crewcloud.apps.crewboard.activity.MainActivity.class);
            finish();
        }
    }

    private class ActivityHandler2 extends Handler {
        private final WeakReference<IntroActivity> mWeakActivity;
        public ActivityHandler2(IntroActivity activity) {
            mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final IntroActivity activity = mWeakActivity.get();

            if (activity != null) {
                if (msg.what == Constants.ACTIVITY_HANDLER_NEXT_ACTIVITY) {
                    new WebClientAsync_HasApplication_v2().execute();
                } else if (msg.what == Constants.ACTIVITY_HANDLER_START_UPDATE) {
                    DialogUtil.customAlertDialog(context, getString(R.string.string_update_content), getString(R.string.auto_login_button_yes), getString(R.string.auto_login_button_no),
                            new DialogUtil.OnAlertDialogViewClickEvent() {
                                @Override
                                public void onOkClick(DialogInterface alertDialog) {
                                    new Async_DownloadApkFile(IntroActivity.this, Constants.APP_NAME).execute();
                                }

                                @Override
                                public void onCancelClick() {
                                    new WebClientAsync_HasApplication_v2().execute();
                                }
                            });
                }
            }
        }
    }

    private class UpdateRunnable implements Runnable {
        @Override
        public void run() {
            try {
                URL txtUrl = new URL(Constants.ROOT_URL_ANDROID + Constants.VERSION + Constants.APP_NAME + ".txt");
                HttpURLConnection urlConnection = (HttpURLConnection) txtUrl.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String serverVersion = bufferedReader.readLine();
                inputStream.close();
                Util.printLogs("serverVersion: " + serverVersion);
                String appVersion = BuildConfig.VERSION_NAME;

                if (Util.versionCompare(serverVersion, appVersion) <= 0) {
                    mActivityHandler2.sendEmptyMessageDelayed(Constants.ACTIVITY_HANDLER_NEXT_ACTIVITY, 1);
                } else {
                    mActivityHandler2.sendEmptyMessage(Constants.ACTIVITY_HANDLER_START_UPDATE);
                }
            } catch (Exception e) {
                mActivityHandler2.sendEmptyMessageDelayed(Constants.ACTIVITY_HANDLER_NEXT_ACTIVITY, 1);
                e.printStackTrace();
            }
        }
    }

    private final ActivityHandler2 mActivityHandler2 = new ActivityHandler2(this);

    private class Async_DownloadApkFile extends AsyncTask<Void, Void, Void> {
        private String mApkFileName;
        private final WeakReference<IntroActivity> mWeakActivity;
        private ProgressDialog mProgressDialog = null;

        public Async_DownloadApkFile(IntroActivity activity, String apkFileName) {
            mWeakActivity = new WeakReference<>(activity);
            mApkFileName = apkFileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            IntroActivity activity = mWeakActivity.get();

            if (activity != null) {
                mProgressDialog = new ProgressDialog(activity);
                mProgressDialog.setMessage(getString(R.string.mailActivity_message_download_apk));
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            BufferedInputStream bufferedInputStream = null;
            FileOutputStream fileOutputStream = null;

            try {
                URL apkUrl = new URL(updateUrl);
                urlConnection = (HttpURLConnection) apkUrl.openConnection();
                inputStream = urlConnection.getInputStream();
                bufferedInputStream = new BufferedInputStream(inputStream);

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + mApkFileName + "_new.apk";
                fileOutputStream = new FileOutputStream(filePath);

                byte[] buffer = new byte[4096];
                int readCount;

                while (true) {
                    readCount = bufferedInputStream.read(buffer);
                    if (readCount == -1) {
                        break;
                    }

                    fileOutputStream.write(buffer, 0, readCount);
                    fileOutputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (urlConnection != null) {
                    try {
                        urlConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            IntroActivity activity = mWeakActivity.get();

            if (activity != null) {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + mApkFileName + "_new.apk";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (checkPermissions()) {
                        startIntentUpdate(activity, filePath);
                    } else {
                        setPermissions();
                    }

                    Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
                    activity.startActivity(intent);
                }
            }

            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }

    private boolean checkPermissions() {
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }

    private void setPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_WIFI_STATE
        }, MY_PERMISSIONS_REQUEST_CODE);
    }

    private final int MY_PERMISSIONS_REQUEST_CODE = 1;
    String filePath = "";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_CODE) {
            return;
        }

        boolean isGranted = true;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }

        if (isGranted) {
            startIntentUpdate(this, filePath);
        } else {
            Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void startIntentUpdate(Activity activity, String filePath) {
        Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(intent);
    }
}
