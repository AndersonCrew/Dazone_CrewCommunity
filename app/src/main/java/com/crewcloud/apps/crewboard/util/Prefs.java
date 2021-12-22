package com.crewcloud.apps.crewboard.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.crewcloud.apps.crewboard.activity.logintest.Statics;

public class Prefs {

    private SharedPreferences prefs;

    private static final String SHAREDPREFERENCES_NAME = "CrewBoard_Prefs";
    private static final String ACCESSTOKEN = "accesstoken";
    private static final String ISADMIN = "isadmin";
    private static final String AESORTTYPE = "aesorttype";
    private static final String INTRO_COUNT = "introcount";

    private static final String USER_NAME = "username";
    private static final String COMPANYNO = "companyno";
    private static final String USERNO = "user_no";
    private static final String USERID = "user_id";
    private static final String PASS = "pass";
    private static final String AVATAR = "avatar";
    private static final String MAIL = "mail";
    private static final String COMPANY_NAME = "company_name";

    private final String KEY_GCM = "googlecloudmsg";

    public Prefs() {
        prefs = CrewBoardApplication.getInstance().getApplicationContext().getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Prefs mInstance;

    public static Prefs getInstance() {
        if (null == mInstance) {
            mInstance = new Prefs();
        }
        return mInstance;
    }

    public void putCompanyNo(int aesorttype) {
        putIntValue(COMPANYNO, aesorttype);
    }

    public void putUserNo(int userNo) {
        putIntValue(USERNO, userNo);
    }

    public int getUserNo() {
        return getIntValue(USERNO, -1);
    }

    public void putaesorttype(int aesorttype) {
        putIntValue(AESORTTYPE, aesorttype);
    }

    public String getPass() {
        return getStringValue(PASS, "");
    }

    public void putPass(String pass) {
        putStringValue(PASS, pass);
    }

    public String getUserid() {
        return getStringValue(USERID, "");
    }

    public void putUserid(String userId) {
        putStringValue(USERID, userId);
    }

    public void putCompanyName(String companyName) {
        putStringValue(COMPANY_NAME, companyName);
    }

    public String getCompanyName() {
        return getStringValue(COMPANY_NAME, "");
    }

    public int getintrocount() {
        return getIntValue(INTRO_COUNT, 0);
    }

    public void putisadmin(int isadmin) {
        putIntValue(ISADMIN, isadmin);
    }

    public String getServerSite() {
        return getStringValue(Constants.DOMAIN, "");
    }

    public String getServer() {
        return getStringValue(Constants.COMPANY_NAME, "");
    }

    public void putUserName(String username) {
        putStringValue(USER_NAME, username);
    }

    public String getUserName() {
        return getStringValue(USER_NAME, "");
    }

    public void putAvatar(String avatar) {
        putStringValue(AVATAR, avatar);
    }

    public String getAvatar() {
        return getStringValue(AVATAR, "");
    }


    public void putMailAddress(String mailAddress) {
        putStringValue(MAIL, mailAddress);
    }

    public String getMailAddress() {
        return getStringValue(MAIL, "");
    }

    public void putaccesstoken(String accesstoken) {
        putStringValue(ACCESSTOKEN, accesstoken);
    }

    public String getaccesstoken() {
        return getStringValue(ACCESSTOKEN, "");
    }

    public void putBooleanValue(String KEY, boolean value) {
        prefs.edit().putBoolean(KEY, value).apply();
    }

    public boolean getBooleanValue(String KEY, boolean defvalue) {
        return prefs.getBoolean(KEY, defvalue);
    }

    public void putStringValue(String KEY, String value) {
        prefs.edit().putString(KEY, value).apply();
    }

    public String getStringValue(String KEY, String defvalue) {
        return prefs.getString(KEY, defvalue);
    }

    public void putIntValue(String KEY, int value) {
        prefs.edit().putInt(KEY, value).apply();
    }

    public int getIntValue(String KEY, int defvalue) {
        return prefs.getInt(KEY, defvalue);
    }

    public void putLongValue(String KEY, long value) {
        prefs.edit().putLong(KEY, value).apply();
    }

    public long getLongValue(String KEY, long defvalue) {
        return prefs.getLong(KEY, defvalue);
    }

    public void setGCMregistrationid(String domain) {
        prefs.edit().putString(KEY_GCM, domain).apply();
    }
}