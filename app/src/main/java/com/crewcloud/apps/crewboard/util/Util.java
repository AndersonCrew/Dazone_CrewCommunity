package com.crewcloud.apps.crewboard.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard._Application;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Util {
    private static Toast toast;

    public static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    public static void showShortMessage(String text) {
        if (toast == null) {
            toast = Toast.makeText(CrewBoardApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CrewBoardApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public static boolean isWifiEnable() {
        WifiManager wifi = (WifiManager) CrewBoardApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) CrewBoardApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isWifiEnabled() && mWifi.isConnected();
    }

    public static void printLogs(String logs) {
        if (Statics.ENABLE_DEBUG) {
            if (logs == null)
                return;
            int maxLogSize = 1000;
            if (logs.length() > maxLogSize) {
                for (int i = 0; i <= logs.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i + 1) * maxLogSize;
                    end = end > logs.length() ? logs.length() : end;
                    Log.d(Statics.TAG, logs.substring(start, end));
                }
            } else {
                Log.d(Statics.TAG, logs);
            }
        }
    }

    public static String getString(int stringID) {
        return CrewBoardApplication.getInstance().getApplicationContext().getResources().getString(stringID);
    }

    public static Resources getResources() {
        return CrewBoardApplication.getInstance().getApplicationContext().getResources();
    }

    public static void hideKeyboard(Context context) {
        try {
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkStringValue(String... params) {
        for (String param : params) {
            if (param != null) {
                if (TextUtils.isEmpty(param.trim())) {
                    return false;
                }

                if (param.contains("\n") && TextUtils.isEmpty(param.replace("\n", ""))) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }

    public static long getTimeOffsetInMinute() {
        return TimeUnit.MINUTES.convert(getTimeOffsetInMilis(), TimeUnit.MILLISECONDS);
    }

    public static long getTimeOffsetInMilis() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();

        return mTimeZone.getRawOffset();
    }

    public static String getPhoneLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf("/") + 1);
        } else {
            return "";
        }
    }

    public static int getTypeFile(String typeFile) {
        int type = 0;
        switch (typeFile) {
            case Statics.IMAGE_GIF:
            case Statics.IMAGE_JPEG:
            case Statics.IMAGE_JPG:
            case Statics.IMAGE_PNG:
                type = 1;
                break;
            case Statics.VIDEO_MP4:
            case Statics.VIDEO_MOV:
                type = 2;
                break;
            case Statics.AUDIO_MP3:
            case Statics.AUDIO_AMR:
            case Statics.AUDIO_WMA:
                type = 3;
                break;
            case Statics.FILE_DOC:
            case Statics.FILE_DOCX:
                type = 4;
                break;
            case Statics.FILE_XLS:
            case Statics.FILE_XLSX:
                type = 5;
                break;
            case Statics.FILE_PDF:
                type = 6;
                break;
            case Statics.FILE_PPT:
            case Statics.FILE_PPTX:
                type = 7;
                break;
            case Statics.FILE_ZIP:
                type = 8;
                break;
            case Statics.FILE_RAR:
                type = 9;
                break;
            case Statics.FILE_APK:
                type = 10;
                break;
            default:
                type = 11;
                break;
        }
        return type;
    }

    static float x = 0, y = 0;
    static float delta = 30;
    static boolean disableClick = false;

    public static void animateClickButton(View view, final View.OnClickListener onClickListener) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = motionEvent.getX();
                        y = motionEvent.getY();
                        view.animate().setDuration(100).scaleX(0.95f).scaleY(0.95f);
                        return true;
                    case MotionEvent.ACTION_UP:
                        view.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f);
                        if (!disableClick)
                            onClickListener.onClick(view);
                        return false;

                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs(motionEvent.getX() - x) > delta || Math.abs(motionEvent.getY() - y) > delta) {
                            disableClick = true;
                            //  android.util.Log.d("animate view",( motionEvent.getX() - x) + " - " + (motionEvent.getY() - y));
                        } else {
                            disableClick = false;
                        }
                        return true;
                    case MotionEvent.ACTION_CANCEL:
                        view.animate().setDuration(100).scaleX(1.0f).scaleY(1.0f);
                        break;
                }

                return false;
            }
        });
    }

    public static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    public static float getDisplayDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static long getAvailableMemory(Context context) {
        long m = Runtime.getRuntime().freeMemory() / 1024;
        Log.d("TAG", "Available Memory: " + m + " KB");
        return m;
    }


    public static long getFileSize(String realPath) {
        long sizeOfInputStram = 0;
        try {
            InputStream is = new FileInputStream(realPath);
            sizeOfInputStram = is.available();
        } catch (Exception e) {
            sizeOfInputStram = 0;
        }
        return sizeOfInputStram;
    }

    public static String getPathFromURI(Uri contentURI, Context context) {
        String result;
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE);
                result = cursor.getString(idx);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        return result;
    }

    //Get filename from URI
    public static String getFileName(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                assert cursor != null;
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String readableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        return Integer.signum(vals1.length - vals2.length);
    }

    public static int getTimezoneOffsetInMinutes() {
        TimeZone tz = TimeZone.getDefault();
        int offsetMinutes = tz.getRawOffset() / 60000;
        return offsetMinutes;
    }

    public static void showImage(String url, ImageView view) {
        if (url.contains("content") || url.contains("storage")) {
            File f = new File(url);
            if (f.exists()) {
                Picasso.with(view.getContext()).load(f).into(view);
            } else {
                Picasso.with(view.getContext()).load(url).into(view);
            }
        } else {
            String path = CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.DOMAIN, "");
            Picasso.with(view.getContext()).load(path + url).into(view);
        }
    }

    public static String getFullHour(String tv) {
        String hour = "", minute = "";
        int h = getHour(tv);
        int m = getMinute(tv);
        if (h < 10) hour = "0" + h;
        else hour = "" + h;
        if (m < 10) minute = "0" + m;
        else minute = "" + m;
        String text = hour + ":" + minute;
        return text;
    }

    public static int getHour(String tv) {
        int h = 0;
        String str[] = tv.trim().split(" ");
        h = Integer.parseInt(str[1].split(":")[0]);
        if (str[0].equalsIgnoreCase("PM")) h += 12;
        return h;
    }

    public static int getMinute(String tv) {
        return Integer.parseInt(tv.split(" ")[1].split(":")[1]);
    }

    public static String setServerSite(String domain) {
        String[] domains = domain.split("[.]");
        if (domain.contains(".bizsw.co.kr") && !domain.contains("8080")) {
            domain =  domain.replace(".bizsw.co.kr", ".bizsw.co.kr:8080");
        }

        if (domains.length == 1) {
            domain = domains[0] + ".crewcloud.net";
        }

        if(domain.startsWith("http://") || domain.startsWith("https://")){
            CrewBoardApplication.getInstance().getPreferenceUtilities().setString(Constants.DOMAIN, domain);
            String companyName = domain.startsWith("http://") ? domain.replace("http://", ""): domain.startsWith("https://") ? domain.replace("https://", ""): domain;
            CrewBoardApplication.getInstance().getPreferenceUtilities().setString(Constants.COMPANY_NAME, companyName);
            return domain;
        }

        String head = CrewBoardApplication.getInstance().getPreferenceUtilities().getBoolean(Constants.HAS_SSL, false) ? "https://" : "http://";
        String domainCompany = head + domain;
        CrewBoardApplication.getInstance().getPreferenceUtilities().setString(Constants.DOMAIN, domainCompany);
        CrewBoardApplication.getInstance().getPreferenceUtilities().setString(Constants.COMPANY_NAME, domain);
        return domainCompany;
    }
}