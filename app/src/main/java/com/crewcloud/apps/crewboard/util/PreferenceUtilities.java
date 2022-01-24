package com.crewcloud.apps.crewboard.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtilities {
    private SharedPreferences mPreferences;

    private final String KEY_CURRENT_COMPANY_NO = "currentCompanyNo";
    private final String KEY_CURRENT_MOBILE_SESSION_ID = "currentMobileSessionId";
    private final String KEY_CURRENT_USER_ID = "currentUserID";
    private final String KEY_PASS = "pass";
    private final String AVATAR = "avatar";
    private final String FULL_NAME = "fullname";
    private final String EMAIL = "email";

    private final String COMPANY_NAME = "companyname";

    private final String KEY_CURRENT_USER_NO = "currentUserNo";
    private final String NOTIFI_MAIL = "notifi_newmail";
    private final String NOTIFI_SOUND = "notifi_sound";
    private final String NOTIFI_VIBRATE = "notifi_vibrate";
    private final String NOTIFI_TIME = "notifi_time";
    private final String START_TIME = "starttime";
    private final String END_TIME = "endtime";
    private final String KEY_GCM = "googlecloudmsg";

    public PreferenceUtilities() {
        mPreferences = CrewBoardApplication.getInstance().getApplicationContext().getSharedPreferences("CrewBoard_Prefs", Context.MODE_PRIVATE);
    }

    public String getCurrentServiceDomain() {
        return mPreferences.getString(Constants.COMPANY_NAME, "");
    }

    public String getCompany() {
        return mPreferences.getString(Constants.COMPANY_NAME, "");
    }

    public String getCurrentCompanyDomain() {
        return mPreferences.getString(Constants.COMPANY_NAME, "");
    }

    public void setCurrentCompanyNo(int companyNo) {
        mPreferences.edit().putInt(KEY_CURRENT_COMPANY_NO, companyNo).apply();
    }

    public int getCurrentCompanyNo() {
        return mPreferences.getInt(KEY_CURRENT_COMPANY_NO, 0);
    }

    public void setCurrentMobileSessionId(String sessionId) {
        mPreferences.edit().putString(KEY_CURRENT_MOBILE_SESSION_ID, sessionId).apply();
    }

    public String getCurrentMobileSessionId() {
        return mPreferences.getString(KEY_CURRENT_MOBILE_SESSION_ID, "");
    }

    public void setCurrentUserID(String userID) {
        mPreferences.edit().putString(KEY_CURRENT_USER_ID, userID).apply();
    }

    public void setPass(String pass) {
        mPreferences.edit().putString(KEY_PASS, pass).apply();
    }

    public void setFullName(String fullName) {
        mPreferences.edit().putString(FULL_NAME, fullName).apply();
    }

    public void setUserAvatar(String avatar) {
        mPreferences.edit().putString(AVATAR, avatar).apply();
    }

    public String getUserAvatar() {
        return mPreferences.getString(AVATAR, "");
    }

    public void setEmail(String email) {
        mPreferences.edit().putString(EMAIL, email).apply();
    }

    public String getEmail() {
        return mPreferences.getString(EMAIL, "");
    }

    public String getGCMregistrationid() {
        return mPreferences.getString(KEY_GCM, "");
    }

    public void setEND_TIME(String domain) {
        mPreferences.edit().putString(END_TIME, domain).apply();
    }

    public String getEND_TIME() {
        return mPreferences.getString(END_TIME, "PM 06:00");
    }

    public void setSTART_TIME(String domain) {
        mPreferences.edit().putString(START_TIME, domain).apply();
    }

    public String getSTART_TIME() {
        return mPreferences.getString(START_TIME, "AM 08:00");
    }

    public void setNOTIFI_TIME(boolean b) {
        mPreferences.edit().putBoolean(NOTIFI_TIME, b).apply();
    }

    public boolean getNOTIFI_TIME() {
        return mPreferences.getBoolean(NOTIFI_TIME, false);
    }

    public void setNOTIFI_VIBRATE(boolean b) {
        mPreferences.edit().putBoolean(NOTIFI_VIBRATE, b).apply();
    }

    public boolean getNOTIFI_VIBRATE() {
        return mPreferences.getBoolean(NOTIFI_VIBRATE, true);
    }

    public void setNOTIFI_MAIL(boolean b) {
        mPreferences.edit().putBoolean(NOTIFI_MAIL, b).apply();
    }

    public boolean getNOTIFI_MAIL() {
        return mPreferences.getBoolean(NOTIFI_MAIL, true);
    }

    public void setNOTIFI_SOUND(boolean b) {
        mPreferences.edit().putBoolean(NOTIFI_SOUND, b).apply();
    }

    public boolean getNOTIFI_SOUND() {
        return mPreferences.getBoolean(NOTIFI_SOUND, true);
    }

    public void setCompanyName(String companyName) {
        mPreferences.edit().putString(Constants.COMPANY_NAME, companyName).apply();
    }

    public String getCompanyName() {
        return mPreferences.getString(Constants.COMPANY_NAME, "");
    }

    public int getCurrentUserNo() {
        return mPreferences.getInt(KEY_CURRENT_USER_NO, 0);
    }

    public void setCurrentUserNo(int userNo) {
        mPreferences.edit().putInt(KEY_CURRENT_USER_NO, userNo).apply();
    }

    public void clearNotificationSetting(){
        mPreferences.edit().remove(NOTIFI_MAIL).apply();
        mPreferences.edit().remove(NOTIFI_SOUND).apply();
        mPreferences.edit().remove(NOTIFI_VIBRATE).apply();
        mPreferences.edit().remove(NOTIFI_TIME).apply();
        mPreferences.edit().remove(START_TIME).apply();
        mPreferences.edit().remove(END_TIME).apply();
    }

    /**
     * GET KEY VALUE
     * */
    public void setInt(String key, int value){
        mPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key){
        return mPreferences.getInt(key, -1);
    }

    public void setBoolean(String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public void setString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }
}