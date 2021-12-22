package com.crewcloud.apps.crewboard.util;

import android.content.Context;

import java.util.Locale;
import java.util.TimeZone;

public class DeviceUtilities {

    public static String getLanguageCode() {
        Context context = CrewBoardApplication.getInstance().getBaseContext();
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();

        switch (language) {
            case "ko":
                return "KO";
            case "vi":
                return "VN";
            default:
                return "EN";
        }
    }

    public static int getTimeZoneOffset() {
        return TimeZone.getDefault().getRawOffset() / 1000 / 60;
    }

    public static String getOSVersion() {
        return "Android " + android.os.Build.VERSION.RELEASE;
    }
}
