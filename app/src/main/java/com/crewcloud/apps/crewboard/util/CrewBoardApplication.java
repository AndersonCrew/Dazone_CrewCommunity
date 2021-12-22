package com.crewcloud.apps.crewboard.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.di.component.AppComponent;
import com.crewcloud.apps.crewboard.di.component.DaggerAppComponent;
import com.crewcloud.apps.crewboard.di.module.AppModule;
import com.crewcloud.apps.crewboard.di.module.CommonModule;
import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.fabric.sdk.android.Fabric;
import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Admin on 8/10/2016.
 */
public class CrewBoardApplication extends Application{
    private static CrewBoardApplication _instance;
    private static Prefs mPrefs;
    private RequestQueue mRequestQueue;
    private String TAG = "Application";
    private AppComponent appComponent;
    private static PreferenceUtilities mPreferenceUtilities;
    public static String getProjectCode() {
        return "Board";
    }
    public CrewBoardApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        _instance = this;
        mPrefs = new Prefs();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .commonModule(new CommonModule())
                .build();
        configDBBrowser();
        handleSSLHandshake();
    }

    /**
     * Enables https connections
     */
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void configDBBrowser() {

        RealmInspectorModulesProvider provider = RealmInspectorModulesProvider.builder(this)
                .withMetaTables()
                .withDescendingOrder()
                .build();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(provider)
                        .build());
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public Prefs getmPrefs() {
        return mPrefs;
    }
    public static synchronized CrewBoardApplication getInstance() {
        return _instance;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setRetryPolicy(new DefaultRetryPolicy(Statics.REQUEST_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setRetryPolicy(new DefaultRetryPolicy(Statics.REQUEST_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
    public synchronized PreferenceUtilities getPreferenceUtilities() {
        if (mPreferenceUtilities == null) {
            mPreferenceUtilities = new PreferenceUtilities();
        }

        return mPreferenceUtilities;
    }

    public void setShortcut(int count) {
        ShortcutBadger.applyCount(this, count);
    }

    public void removeShortcut() {
        ShortcutBadger.removeCount(this);
    }
}
