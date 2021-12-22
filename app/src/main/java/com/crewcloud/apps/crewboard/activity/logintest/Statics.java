package com.crewcloud.apps.crewboard.activity.logintest;

import com.crewcloud.apps.crewboard.BuildConfig;

/**
 * Created by Admin on 8/10/2016.
 */
public class Statics {
    public static final boolean ENABLE_DEBUG = BuildConfig.ENABLE_DEBUG;
    public static final String TAG = ">>>CrewBoard";
    public static final boolean WRITE_HTTP_REQUEST = true;
    public static final int REQUEST_TIMEOUT_MS = 15000;
    public static final String ID_COMMUNITY = "id_community";

    public static final String IMAGE_DEAULT = "/Images/Avatar.jpg";

    public static final String KEY_URL = "KEY_URL";
    /**
     * PREFS
     */
    public static final String PREFS_KEY_RELOAD_SETTING = "reload_setting";
    public static final String PREFS_KEY_RELOAD_TIMECARD = "reload_timecard";
    public static final String PREFS_KEY_SESSION_ERROR = "session_error";
    public static final String PREFS_KEY_SORT_STAFF_LIST = "PREFS_KEY_SORT_STAFF_LIST";
    public static final String PREFS_KEY_COMPANY_NAME = "PREFS_KEY_COMPANY_NAME";
    public static final String PREFS_KEY_COMPANY_DOMAIN = "PREFS_KEY_COMPANY_DOMAIN";
    public static final String PREFS_KEY_USER_ID = "PREFS_KEY_USER_ID";

    public static final boolean WRITEHTTPREQUEST = true;
    public static String pathDownload = "/CrewCommunity/";

    public abstract class MENU {

        public static final int COMMUNITY = 1;
        public static final int LEFT_MENU = 2;

    }

    public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd hh:mm aa";
    public static final String DATE_FORMAT_HH_MM_AA = "hh:mm aa";
    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd";

    //file type
    public static final String IMAGE_JPG = ".jpg";
    public static final String IMAGE_JPEG = ".jpeg";
    public static final String IMAGE_PNG = ".png";
    public static final String IMAGE_GIF = ".gif";
    public static final String AUDIO_MP3 = ".mp3";
    public static final String AUDIO_WMA = ".wma";
    public static final String AUDIO_AMR = ".amr";
    public static final String VIDEO_MP4 = ".mp4";
    public static final String VIDEO_MOV = ".mov";
    public static final String FILE_PDF = ".pdf";
    public static final String FILE_DOCX = ".docx";
    public static final String FILE_DOC = ".doc";
    public static final String FILE_XLS = ".xls";
    public static final String FILE_XLSX = ".xlsx";
    public static final String FILE_PPTX = ".pptx";
    public static final String FILE_PPT = ".ppt";
    public static final String FILE_ZIP = ".zip";
    public static final String FILE_RAR = ".rar";
    public static final String FILE_APK = ".apk";
    public static final String MIME_TYPE_IMAGE = "image/*";
    public static final String MIME_TYPE_VIDEO = "video/*";
    public static final String MIME_TYPE_APP = "file/*";
    public static final String MIME_TYPE_TEXT = "text/*";
    public static final String MIME_TYPE_ALL = "*/*";
    public static final String MIME_TYPE_AUDIO = "audio/*";
}
