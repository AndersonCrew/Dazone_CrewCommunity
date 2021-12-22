package com.crewcloud.apps.crewboard;

import android.app.Application;

import com.crewcloud.apps.crewboard.util.PreferenceUtilities;


public class _Application extends Application {
    private static _Application mInstance;
    private static PreferenceUtilities mPreferenceUtilities;

    public static String getProjectCode() {
        return "Board";
    }

    public _Application() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized _Application getInstance() {
        return mInstance;
    }

    public synchronized PreferenceUtilities getPreferenceUtilities() {
        if (mPreferenceUtilities == null) {
            mPreferenceUtilities = new PreferenceUtilities();
        }

        return mPreferenceUtilities;
    }
}