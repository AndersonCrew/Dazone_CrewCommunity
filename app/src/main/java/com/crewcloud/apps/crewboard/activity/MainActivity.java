package com.crewcloud.apps.crewboard.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
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
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.BaseActivity;
import com.crewcloud.apps.crewboard.activity.logintest.LoginActivity;
import com.crewcloud.apps.crewboard.base.WebContentClient;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.interfaces.OnHasAppCallBack;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DeviceUtilities;
import com.crewcloud.apps.crewboard.util.DialogUtil;
import com.crewcloud.apps.crewboard.util.HttpRequest;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Util;
import com.crewcloud.apps.crewboard.util.WebClient;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnHasAppCallBack {
    private final int START_PERMISSIONS_REQUEST_CODE = 0;
    private final int UPDATE_PERMISSIONS_REQUEST_CODE = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 2;
    private WebView wvContent = null;
    private ProgressBar mProgressLoading;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private String updateUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        wvContent = findViewById(R.id.wvContent);

        mProgressLoading = findViewById(R.id.pbProgress);
        setWebViewClient();
        initWebContent();
        HttpRequest.getInstance().checkApplicationUpdate(this);
    }

    private void initWebContent() {
        WebSettings webSettings = wvContent.getSettings();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setSaveFormData(false);
        } else {
            webSettings.setSaveFormData(false);
            webSettings.setSavePassword(false);
        }

        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }


        wvContent.clearCache(true);
        wvContent.clearHistory();
        wvContent.addJavascriptInterface(new JavaScriptExtension(), "crewcloud");
        wvContent.setDownloadListener(mDownloadListener);

        PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
        String domain = preferenceUtilities.getString(Constants.DOMAIN, "");

        CookieManager.getInstance().setCookie(domain, "skey0=" + preferenceUtilities.getCurrentMobileSessionId());
        CookieManager.getInstance().setCookie(domain, "skey1=" + "123123123123132");
        CookieManager.getInstance().setCookie(domain, "skey2=" + DeviceUtilities.getLanguageCode());
        CookieManager.getInstance().setCookie(domain, "skey3=" + preferenceUtilities.getCurrentCompanyNo());

        String url = domain + "/UI/MobileBoard/";
        wvContent.loadUrl(url);
    }

    private void setWebViewClient() {
        wvContent.setWebViewClient(new WebContentClient(this, mProgressLoading));

        if(!checkPermissions())
            setPermissions(START_PERMISSIONS_REQUEST_CODE);

        wvContent.setWebChromeClient(new WebChromeClient() {
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();

                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Open File Chooser", Toast.LENGTH_LONG).show();
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


    }

    @Override
    public void hasApp(String url) {
        updateUrl = url;
        DialogUtil.customAlertDialog(this, getString(R.string.string_update_content), getString(R.string.auto_login_button_yes), null,
                new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if (checkPermissions()) {
                                new MainActivity.Async_DownloadApkFile(MainActivity.this, Constants.APP_NAME).execute();
                            } else {
                                setPermissions(UPDATE_PERMISSIONS_REQUEST_CODE);
                            }
                        } else {
                            new MainActivity.Async_DownloadApkFile(MainActivity.this, Constants.APP_NAME).execute();
                        }
                    }

                    @Override
                    public void onCancelClick() {
                    }
                });
    }

    @Override
    public void noHas(ErrorDto dto) {

    }

    private final class JavaScriptExtension {

        @JavascriptInterface
        public void logout() {
            Util.printLogs("Logout javascript is called ###");
            new WebClientAsync_Logout_v2().execute();
        }

        @JavascriptInterface
        public void openSetting() {
            BaseActivity.Instance.callActivity(SettingActivity.class);
        }
    }

    public void logout() {
        DialogUtil.customAlertDialog(this, getString(R.string.are_you_sure_loguot), getString(R.string.auto_login_button_yes), getString(R.string.auto_login_button_no),
                new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {
                        new WebClientAsync_Logout_v2().execute();
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    private boolean mIsBackPressed = false;

    private static class ActivityHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public ActivityHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.setBackPressed(false);
            }
        }
    }

    private final ActivityHandler mActivityHandler = new ActivityHandler(this);

    public void setBackPressed(boolean isBackPressed) {
        mIsBackPressed = isBackPressed;
    }

    @Override
    public void onBackPressed() {
        if (wvContent.canGoBack()) {
            wvContent.goBack();
        } else {
            if (!mIsBackPressed) {
                Toast.makeText(this, R.string.mainActivity_message_exit, Toast.LENGTH_SHORT).show();
                mIsBackPressed = true;
                mActivityHandler.sendEmptyMessageDelayed(0, 2000);
            } else {
                finish();
            }
        }
    }

    // ----------------------------------------------------------------------------------------------

    public String content;
    public String linkDownload;
    public String type;

    private DownloadListener mDownloadListener = new DownloadListener() {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.d(">>>", "onDownloadStart " + url);
            linkDownload = url;
            content = contentDisposition;
            type = mimetype;
            //check permission
            if (ContextCompat.checkSelfPermission((MainActivity.this),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale((MainActivity.this),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            EXTERNAL_STORAGE_PERMISSION_CONSTANT);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                downloadFile(url, contentDisposition, mimetype);
            }
        }

    };

    private class GetFileInfo extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            URL url;
            String filename = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                conn.setInstanceFollowRedirects(false);


                String depoSplit[] = conn.getURL().getQuery().split("name=");
                filename = depoSplit[1].split("&")[0];


//                filename = depoSplit[1].replace("filename=", "").replace("\"", "").trim();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
            }
            return filename;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            name = fileName;

            if (linkDownload == null || linkDownload.isEmpty()) {
                Log.d(">>>", "linkDownload.isEmpty");
                return;
            }
            DownloadManager.Request request = new DownloadManager.Request(
                    Uri.parse(linkDownload));
            request.setMimeType(type);
            String cookies = CookieManager.getInstance().getCookie(linkDownload);
            request.addRequestHeader("cookie", cookies);
//            request.addRequestHeader("User-Agent", content);
            request.setDescription("Downloading file...");
            request.allowScanningByMediaScanner();
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            dm.enqueue(request);

            Toast.makeText(MainActivity.this, "다운로드를 시작합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    String name;

    private void downloadFile(String url, String contentDisposition, String mimeType) {
        new GetFileInfo().execute(url);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case EXTERNAL_STORAGE_PERMISSION_CONSTANT: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    if (linkDownload != null && !linkDownload.isEmpty())
//                        downloadFile(linkDownload, content, type);
//                    else
//                        Log.d(">>>", "linkDownload.isEmpty");
//                } else {
//                    // permission denied, boo! Disable the
//                    Toast.makeText(this, " permission denied, try again!", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(MainActivity.this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                || (ContextCompat.checkSelfPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) == PackageManager.PERMISSION_GRANTED));

    }

    private void setPermissions(int requestCode) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            ActivityCompat.requestPermissions(this, new String[]{
//                    Manifest.permission.INTERNET,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_WIFI_STATE,
//                    Manifest.permission.REQUEST_INSTALL_PACKAGES
//            }, UPDATE_PERMISSIONS_REQUEST_CODE);
//            Log.d(">>> ", "setPermissions N");
//        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CAMERA,
            }, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (linkDownload != null && !linkDownload.isEmpty())
                        downloadFile(linkDownload, content, type);
                } else {
                    Toast.makeText(this, " permission denied, try again!", Toast.LENGTH_LONG).show();
                }
            }
            case UPDATE_PERMISSIONS_REQUEST_CODE: {
                boolean isGranted = true;
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                        break;
                    }
                }

                if (isGranted) {
                    new MainActivity.Async_DownloadApkFile(MainActivity.this, Constants.APP_NAME).execute();
                } else {
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }


    private class Async_DownloadApkFile extends AsyncTask<Void, Void, Void> {
        private String mApkFileName;
        private final WeakReference<MainActivity> mWeakActivity;
        private ProgressDialog mProgressDialog = null;

        public Async_DownloadApkFile(MainActivity activity, String apkFileName) {
            mWeakActivity = new WeakReference<>(activity);
            mApkFileName = apkFileName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            MainActivity activity = mWeakActivity.get();

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

            Log.d(">>>>", "onPostExecute");
            MainActivity activity = mWeakActivity.get();

            if (activity != null) {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/" + mApkFileName + "_new.apk";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    if (checkPermissions()) {
//                        startIntentUpdate(activity, filePath);
//                    } else {
//                        setPermissions();
//                    }

                    Log.d(">>>>", filePath);
                    Uri apkUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(filePath));
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    activity.startActivity(intent);

//                    String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//                    String fileName = mApkFileName + "_new.apk";
//                    destination += fileName;
//                    final Uri uri = Uri.parse("file://" + destination);
//
//                    Intent install = new Intent(Intent.ACTION_VIEW);
//                    install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    install.setDataAndType(uri,"application/vnd.android.package-archive");
//                    startActivity(install);
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
}