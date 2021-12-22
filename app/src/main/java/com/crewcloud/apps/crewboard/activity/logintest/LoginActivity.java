package com.crewcloud.apps.crewboard.activity.logintest;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.MainActivity;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.interfaces.BaseHTTPCallBack;
import com.crewcloud.apps.crewboard.interfaces.ICheckSSL;
import com.crewcloud.apps.crewboard.interfaces.OnAutoLoginCallBack;
import com.crewcloud.apps.crewboard.interfaces.OnHasAppCallBack;
import com.crewcloud.apps.crewboard.util.Constants;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DialogUtil;
import com.crewcloud.apps.crewboard.util.HttpRequest;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.SoftKeyboardDetectorView;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.firebase.iid.FirebaseInstanceId;


public class LoginActivity extends BaseActivity implements BaseHTTPCallBack, OnHasAppCallBack {
    private ImageView imgLogo;
    private TextView tvLogo;
    private EditText etDomain, etUserName, etPassword;
    private Button btnLogin;
    private LinearLayout llSignUp;
    public Prefs mPrefs;
    private boolean mFirstLogin = true;
    private boolean mFirstStart = false;
    private boolean isAutoLoginShow = false;

    private String mRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            setContentView(R.layout.activity_login_v2);
        } else {
            setContentView(R.layout.activity_login_v19);
        }

        mPrefs = CrewBoardApplication.getInstance().getmPrefs();
        if (Util.versionCompare(BuildConfig.VERSION_NAME, "1.3.7") <= 0) {
            CrewBoardApplication.getInstance().getPreferenceUtilities().setBoolean(Constants.IS_FIRST_INSTALL_VER, true);
            if (!CrewBoardApplication.getInstance().getPreferenceUtilities().getString("company", "").isEmpty() &&
                    CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.DOMAIN, "").isEmpty()) {
                CrewBoardApplication.getInstance().getPreferenceUtilities().setString(Constants.DOMAIN, CrewBoardApplication.getInstance().getPreferenceUtilities().getString("company", ""));
            }
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(accountReceiver, intentFilter);
        registerInBackground();

        final SoftKeyboardDetectorView softKeyboardDetectorView = new SoftKeyboardDetectorView(this);
        addContentView(softKeyboardDetectorView, new FrameLayout.LayoutParams(-1, -1));

        softKeyboardDetectorView.setOnShownKeyboard(new SoftKeyboardDetectorView.OnShownKeyboardListener() {
            @Override
            public void onShowSoftKeyboard() {
                if (imgLogo != null) {
                    imgLogo.setVisibility(View.GONE);
                    llSignUp.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvLogo.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    tvLogo.setLayoutParams(params);
                    tvLogo.setText(Util.getString(R.string.app_name));
                }
            }
        });

        softKeyboardDetectorView.setOnHiddenKeyboard(new SoftKeyboardDetectorView.OnHiddenKeyboardListener() {
            @Override
            public void onHiddenSoftKeyboard() {
                if (imgLogo != null) {

                    imgLogo.setVisibility(View.VISIBLE);
                    llSignUp.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvLogo.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    tvLogo.setLayoutParams(params);
                    tvLogo.setText(Util.getString(R.string.app_name));

                }
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.app_base_color));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(accountReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrefs.getintrocount() < 1) {
            mFirstStart = true;
            mPrefs.putaesorttype(2);
        }
        initAtStart();
    }

    public void initAtStart() {
        Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
        if (!TextUtils.isEmpty(CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.DOMAIN, ""))) {
            firstChecking();
        } else {
            prefs.putBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, false);
            init();
        }
    }

    private void firstChecking() {
        if (mFirstLogin) {
            if (Util.isNetworkAvailable()) {
                if (mFirstStart) {
                    doLogin();
                    mFirstStart = false;
                } else {
                    doLogin();
                }
            } else {
                showNetworkDialog();
            }
        }
    }

    private void doLogin() {
        if (Util.checkStringValue(mPrefs.getaccesstoken()) && !mPrefs.getBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, false)) {
            HttpRequest.getInstance().checkLogin(this);
        } else {
            mPrefs.putBooleanValue(Statics.PREFS_KEY_SESSION_ERROR, false);
            mFirstLogin = false;
            init();
        }
    }

    @Override
    public void showNetworkDialog() {
        if (Util.isWifiEnable()) {
            DialogUtil.customAlertDialog(this, getString(R.string.no_connection_error), getString(R.string.string_ok), null,
                    new DialogUtil.OnAlertDialogViewClickEvent() {
                        @Override
                        public void onOkClick(DialogInterface alertDialog) {
                            finish();
                        }

                        @Override
                        public void onCancelClick() {

                        }
                    });
        } else {
            DialogUtil.customAlertDialog(this, getString(R.string.no_wifi_error), getString(R.string.turn_wifi_on), getString(R.string.string_cancel),
                    new DialogUtil.OnAlertDialogViewClickEvent() {
                        @Override
                        public void onOkClick(DialogInterface alertDialog) {
                            Intent wireLess = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(wireLess);
                        }

                        @Override
                        public void onCancelClick() {
                            finish();
                        }
                    });
        }
    }

    public static String BROADCAST_ACTION = "com.dazone.crewcloud.account.receive";
    BroadcastReceiver accountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String receiverPackageName = intent.getExtras().getString("receiverPackageName");
            if (receiverPackageName.equals(LoginActivity.this.getPackageName())) {
                String companyID = intent.getExtras().getString("companyID");
                String userID = intent.getExtras().getString("userID");
                if (!TextUtils.isEmpty(companyID) && !TextUtils.isEmpty(userID) && !isAutoLoginShow) {
                    isAutoLoginShow = true;
                    showPopupAutoLogin(companyID, userID);
                }
            }

        }
    };

    private void showPopupAutoLogin(final String companyID, final String userID) {

        String alert1 = Util.getString(R.string.auto_login_company_ID) + companyID;
        String alert2 = Util.getString(R.string.auto_login_user_ID) + userID;
        String alert3 = Util.getString(R.string.auto_login_text);
        String msg = alert1 + "\n" + alert2 + "\n\n" + alert3;

        DialogUtil.customAlertDialog(this, getString(R.string.auto_login_title), msg,
                getString(R.string.auto_login_button_yes), getString(R.string.auto_login_button_no),
                new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {
                        autoLogin(companyID, userID);
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
    }

    /**
     * AUTO LOGIN
     */
    public void autoLogin(final String companyID, final String userID) {
        registerInBackground();
        Util.setServerSite(companyID);

        showProgressDialog();
        HttpRequest.getInstance().checkSSL(new ICheckSSL() {
            @Override
            public void hasSSL(boolean hasSSL) {
                Util.setServerSite(companyID);
                HttpRequest.getInstance().AutoLogin(userID, new OnAutoLoginCallBack() {
                    @Override
                    public void OnAutoLoginSuccess(String response) {
                        CrewBoardApplication.getInstance().getmPrefs().putUserName(userID);
                        loginSuccess();
                    }

                    @Override
                    public void OnAutoLoginFail(ErrorDto dto) {
                        if (mFirstLogin) {
                            dismissProgressDialog();

                            mFirstLogin = false;
                            init();
                        } else {
                            dismissProgressDialog();
                            String error_msg = dto.message;

                            if (TextUtils.isEmpty(error_msg)) {
                                error_msg = getString(R.string.connection_falsed);
                            }

                            showSaveDialog(error_msg);
                        }
                    }
                });
            }

            @Override
            public void checkSSLError(ErrorDto errorData) {
                dismissProgressDialog();
                Toast.makeText(LoginActivity.this, "Cannot check ssl this domain!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        /** SEND BROADCAST */
        Intent intent = new Intent();
        intent.setAction("com.dazone.crewcloud.account.get");
        intent.putExtra("senderPackageName", this.getPackageName());
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(intent);

        imgLogo = findViewById(R.id.img_login_logo);
        tvLogo = findViewById(R.id.tv_login_logo_text);
        etUserName = findViewById(R.id.login_edt_username);
        etPassword = findViewById(R.id.login_edt_password);
        etDomain = findViewById(R.id.login_edt_server);
        llSignUp = findViewById(R.id.ll_login_sign_up);

        etUserName.setPrivateImeOptions("defaultInputmode=english;");
        etDomain.setPrivateImeOptions("defaultInputmode=english;");

        etDomain.setText(new PreferenceUtilities().getCompany());
        etUserName.setText(new Prefs().getUserid());
        etPassword.setText(new Prefs().getPass());

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");
                if (!s.toString().equals(result)) {
                    etUserName.setText(result);
                    etUserName.setSelection(result.length());
                }
            }
        });

        etDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String result = s.toString().replaceAll(" ", "");

                if (!s.toString().equals(result)) {
                    etDomain.setText(result);
                    etDomain.setSelection(result.length());
                }
            }
        });

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnLogin.callOnClick();
                }

                return false;
            }
        });

        btnLogin = findViewById(R.id.login_btn_login);
        RelativeLayout login_btn_sign_up = findViewById(R.id.login_btn_sign_up);

        if (login_btn_sign_up != null) {
            login_btn_sign_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            });
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerInBackground();
                    final String userName = etUserName.getText().toString();
                    final String password = etPassword.getText().toString();
                    final String domain = etDomain.getText().toString();

                    Util.setServerSite(domain);
                    if (TextUtils.isEmpty(checkStringValue(domain, userName, password))) {

                        showProgressDialog();
                        HttpRequest.getInstance().checkSSL(new ICheckSSL() {
                            @Override
                            public void hasSSL(boolean hasSSL) {
                                Util.setServerSite(domain);
                                HttpRequest.getInstance().login(LoginActivity.this, userName, password);
                            }

                            @Override
                            public void checkSSLError(ErrorDto errorData) {
                                dismissProgressDialog();
                                Toast.makeText(LoginActivity.this, "Cannot check ssl this domain!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        DialogUtil.customAlertDialog(LoginActivity.this, checkStringValue(domain, userName, password), getString(R.string.string_ok), null,
                                new DialogUtil.OnAlertDialogViewClickEvent() {
                                    @Override
                                    public void onOkClick(DialogInterface alertDialog) {

                                    }

                                    @Override
                                    public void onCancelClick() {

                                    }
                                });
                    }
                }
            });
        }
    }

    private String checkStringValue(String server_site, String username, String password) {
        String result = "";

        if (TextUtils.isEmpty(server_site)) {
            result += getString(R.string.string_server_site);
        }

        if (TextUtils.isEmpty(username)) {
            if (TextUtils.isEmpty(result)) {
                result += getString(R.string.login_username);
            } else {
                result += ", " + getString(R.string.login_username);
            }
        }

        if (TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(result)) {
                result += getString(R.string.login_password);
            } else {
                result += ", " + getString(R.string.login_password);
            }
        }

        if (TextUtils.isEmpty(result)) {
            return result;
        } else {
            return result + " " + getString(R.string.login_empty_input);
        }
    }

    @Override
    public void onHTTPSuccess() {
        loginSuccess();
    }

    private void loginSuccess() {
        dismissProgressDialog();
        insertAndroidDevice();
    }

    @Override
    public void onHTTPFail(ErrorDto errorDto) {
        if (mFirstLogin) {
            dismissProgressDialog();

            mFirstLogin = false;
            init();
        } else {
            dismissProgressDialog();
            String error_msg = errorDto.message;

            if (TextUtils.isEmpty(error_msg)) {
                error_msg = getString(R.string.connection_falsed);
            }

            showSaveDialog(error_msg);
        }
    }

    @Override
    public void hasApp(String url) {
        loginSuccess();
    }

    @Override
    public void noHas(ErrorDto errorDto) {
        if (mFirstLogin) {
            mFirstLogin = false;
            init();
        } else {
            dismissProgressDialog();
            if (!errorDto.message.isEmpty())
                showSaveDialog(errorDto.message);
        }
    }

    private void showSaveDialog(String message) {
        DialogUtil.customAlertDialog(this, message, getString(R.string.string_ok), null,
                new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
    }


    public void insertAndroidDevice() {
        PreferenceUtilities pu = CrewBoardApplication.getInstance().getPreferenceUtilities();
        String start_time = pu.getSTART_TIME();
        String end_time = pu.getEND_TIME();

        if (start_time.length() == 0) {
            start_time = "AM 08:00";
            pu.setSTART_TIME(start_time);
        }

        if (end_time.length() == 0) {
            end_time = "PM 06:00";
            pu.setEND_TIME(end_time);
        }

        String notificationOptions = "{" +
                "\"enabled\": " + pu.getNOTIFI_MAIL() + "," +
                "\"sound\": " + pu.getNOTIFI_SOUND() + "," +
                "\"vibrate\": " + pu.getNOTIFI_VIBRATE() + "," +
                "\"notitime\": " + pu.getNOTIFI_TIME() + "," +
                "\"starttime\": \"" + Util.getFullHour(start_time) + "\"," +
                "\"endtime\": \"" + Util.getFullHour(end_time) + "\"" + "}";

        notificationOptions = notificationOptions.trim();
        HttpRequest.getInstance().insertAndroidDevice(new BaseHTTPCallBack() {
            @Override
            public void onHTTPSuccess() {
                callActivity(MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }

            @Override
            public void onHTTPFail(ErrorDto errorDto) {
                Log.d(">>> insert token fail", errorDto.message);

                callActivity(MainActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, mRegId, notificationOptions);
    }

    private void registerInBackground() {
        new register().execute("");
    }

    public class register extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                mRegId = FirebaseInstanceId.getInstance().getToken();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void unused) {
            mPrefs.setGCMregistrationid(mRegId);
        }
    }
}