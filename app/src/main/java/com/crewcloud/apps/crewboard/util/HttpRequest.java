package com.crewcloud.apps.crewboard.util;

import android.util.Log;

import com.android.volley.Request;
import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard._Application;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.interfaces.GetUserCallBack;
import com.crewcloud.apps.crewboard.interfaces.ICheckSSL;
import com.crewcloud.apps.crewboard.interfaces.OnChangePasswordCallBack;
import com.crewcloud.apps.crewboard.interfaces.OnHasAppCallBack;
import com.crewcloud.apps.crewboard.dtos.MessageDto;
import com.crewcloud.apps.crewboard.dtos.UserDto;
import com.crewcloud.apps.crewboard.interfaces.BaseHTTPCallBack;
import com.crewcloud.apps.crewboard.interfaces.BaseHTTPCallBackWithString;
import com.crewcloud.apps.crewboard.interfaces.OnAutoLoginCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HttpRequest {
    public final static String URL_INSERT_ANDROID_DEVICE = "/UI/MobileBoard/BoardService.asmx/InsertAndroidDevice";
    public final static String URL_UPDATE_ANDROID_DEVICE = "/UI/MobileBoard/BoardService.asmx/UpdateAndroidDevice_NotificationOptions";
    public final static String URL_DELETE_ANDROID_DEVICE = "/UI/MobileBoard/BoardService.asmx/DeleteAndroidDevice";
    public final static String URL_GET_USER = "/UI/WebService/WebServiceCenter.asmx/GetUser";
    private static HttpRequest mInstance;
    private static String root_link;

    public static HttpRequest getInstance() {
        if (null == mInstance) {
            mInstance = new HttpRequest();
        }

        root_link = CrewBoardApplication.getInstance().getmPrefs().getServerSite();
        return mInstance;
    }

    public void login(final BaseHTTPCallBack baseHTTPCallBack, final String userID, final String password) {
        final String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.DOMAIN, "") + Urls.URL_GET_LOGIN;
            Map<String, String> params = new HashMap<>();
            params.put("languageCode", Util.getPhoneLanguage());
            params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
            params.put("companyDomain", CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.COMPANY_NAME, ""));
            params.put("password", password);
            params.put("userID", userID);
            params.put("mobileOSVersion", "Android " + android.os.Build.VERSION.RELEASE);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Util.printLogs("User info =" + response);
                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                userDto.prefs.putaccesstoken(userDto.session);
                userDto.prefs.putisadmin(userDto.PermissionType);
                userDto.prefs.putCompanyNo(userDto.CompanyNo);
                userDto.prefs.putCompanyName(userDto.NameCompany);
                userDto.prefs.putUserNo(userDto.Id);
                userDto.prefs.putUserName(userDto.userID);
                userDto.prefs.putAvatar(userDto.avatar);
                userDto.prefs.putMailAddress(userDto.MailAddress);
                userDto.prefs.putPass(password);
                userDto.prefs.putUserid(userID);
                CrewBoardApplication.getInstance().getmPrefs().putUserName(userDto.userID);

                PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
                preferenceUtilities.setCurrentCompanyNo(userDto.CompanyNo);
                preferenceUtilities.setCurrentMobileSessionId(userDto.session);
                preferenceUtilities.setCurrentUserID(userDto.userID);
                preferenceUtilities.setPass(password);
                preferenceUtilities.setUserAvatar(userDto.avatar);
                preferenceUtilities.setEmail(userDto.MailAddress);
                preferenceUtilities.setCurrentUserNo(userDto.Id);
                preferenceUtilities.setCompanyName(userDto.NameCompany);
                preferenceUtilities.setFullName(userDto.Name);
                baseHTTPCallBack.onHTTPSuccess();
            }

            @Override
            public void onFailure(ErrorDto error) {
                baseHTTPCallBack.onHTTPFail(error);
            }
        });
    }

    public void signUp(final BaseHTTPCallBackWithString baseHTTPCallBack, final String email) {
        final String url = Urls.URL_SIGN_UP;
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("mailAddress", "" + email);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                MessageDto messageDto = gson.fromJson(response, MessageDto.class);

                if (baseHTTPCallBack != null && messageDto != null) {
                    String message = messageDto.getMessage();
                    baseHTTPCallBack.onHTTPSuccess(message);
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (baseHTTPCallBack != null) {
                    baseHTTPCallBack.onHTTPFail(error);
                }
            }
        });
    }

    public void checkLogin(final BaseHTTPCallBack baseHTTPCallBack) {
        final String url = root_link + Urls.URL_CHECK_SESSION;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + CrewBoardApplication.getInstance().getmPrefs().getaccesstoken());
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        Log.d(">>>checkLogin", params.toString());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                userDto.prefs.putaccesstoken(userDto.session);
                userDto.prefs.putisadmin(userDto.PermissionType);
                userDto.prefs.putCompanyNo(userDto.CompanyNo);
                userDto.prefs.putUserNo(userDto.Id);
                userDto.prefs.putUserName(userDto.userID);
                CrewBoardApplication.getInstance().getmPrefs().putUserName(userDto.userID);
                if (baseHTTPCallBack != null) {
                    baseHTTPCallBack.onHTTPSuccess();
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                if (baseHTTPCallBack != null) {
                    baseHTTPCallBack.onHTTPFail(error);
                }
            }
        });
    }

    public void checkApplication(final OnHasAppCallBack callBack) {
        final String url = root_link + Urls.URL_HAS_APPLICATION;
//        final String url = "http://mobileupdate.crewcloud.net/WebServiceMobile.asmx/Mobile_Version";
        Map<String, String> params = new HashMap<>();
        String projectCode = "WorkingTime";

        params.put("projectCode", projectCode);
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    if (callBack != null) {
                        if (json.getBoolean("HasApplication")) {
                            callBack.hasApp("");
                        } else {
                            ErrorDto errorDto = new ErrorDto();
                            errorDto.message = json.getString("Message");
                            callBack.noHas(errorDto);
                        }
                    }
                } catch (Exception e) {
                    callBack.noHas(new ErrorDto());
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.noHas(error);
            }
        });
    }

    public void checkApplicationUpdate(final OnHasAppCallBack callBack) {
        final String url = "http://mobileupdate.crewcloud.net/WebServiceMobile.asmx/Mobile_Version";
        Map<String, String> params = new HashMap<>();

        params.put("MobileType", "Android");
        params.put("Domain", CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.COMPANY_NAME, ""));
        params.put("Applications", "CrewBoard");

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    String appVersion = BuildConfig.VERSION_NAME;
                    JSONObject json = new JSONObject(response);
                    String version = json.getString("version");
                    if(version.isEmpty()) {
                        callBack.noHas(new ErrorDto());
                    }else {
                        if (callBack != null) {
                            if (Util.versionCompare(version, appVersion) > 0) {
                                String url = json.getString("packageUrl");
                                callBack.hasApp(url);
                            } else {
                                ErrorDto errorDto = new ErrorDto();
                                errorDto.message = "";
                                callBack.noHas(errorDto);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.noHas(new ErrorDto());
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.noHas(error);
            }
        });
    }

    public void AutoLogin(final String userID, final OnAutoLoginCallBack callBack) {
        final String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.DOMAIN, "") + Urls.URL_AUTO_LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        params.put("companyDomain", CrewBoardApplication.getInstance().getPreferenceUtilities().getString(Constants.COMPANY_NAME, ""));
        params.put("userID", userID);
        params.put("mobileOSVersion", "Android " + android.os.Build.VERSION.RELEASE);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Gson gson = new Gson();
                UserDto userDto = gson.fromJson(response, UserDto.class);
                userDto.prefs.putaccesstoken(userDto.session);
                userDto.prefs.putisadmin(userDto.PermissionType);
                userDto.prefs.putCompanyNo(userDto.CompanyNo);
                userDto.prefs.putUserNo(userDto.Id);
                userDto.prefs.putUserName(userDto.userID);
                userDto.prefs.putAvatar(userDto.avatar);
                userDto.prefs.putMailAddress(userDto.MailAddress);
                userDto.prefs.putUserid(userID);

                PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
                preferenceUtilities.setCurrentCompanyNo(userDto.CompanyNo);
                preferenceUtilities.setCurrentMobileSessionId(userDto.session);
                preferenceUtilities.setCurrentUserID(userDto.userID);
                preferenceUtilities.setUserAvatar(userDto.avatar);
                preferenceUtilities.setEmail(userDto.MailAddress);
                preferenceUtilities.setCurrentUserNo(userDto.Id);
                preferenceUtilities.setCompanyName(userDto.NameCompany);

                preferenceUtilities.setFullName(userDto.Name);
                callBack.OnAutoLoginSuccess(response);
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.OnAutoLoginFail(error);
            }
        });
    }
    public static void insertAndroidDevice(final BaseHTTPCallBack callBack, String regid, String json) {
        final String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + URL_INSERT_ANDROID_DEVICE;

        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
        params.put("timeZoneOffset", "" + Util.getTimezoneOffsetInMinutes());
        params.put("deviceID", regid);
        params.put("osVersion", "Android " + android.os.Build.VERSION.RELEASE);
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("notificationOptions", json);
        Log.d(">>>>",url);
        Log.d(">>>>",params.toString());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                callBack.onHTTPSuccess();
                Log.d(">>>>","response:"+response);
            }

            @Override
            public void onFailure(ErrorDto error) {
                callBack.onHTTPFail(error);
            }
        });
    }

    public void updateAndroidDevice(String regid, String json) {
        final String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + URL_UPDATE_ANDROID_DEVICE;

        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
        params.put("timeZoneOffset", "" + Util.getTimezoneOffsetInMinutes());
        params.put("deviceID", regid);
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("notificationOptions", json);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {

                Log.e("Update API", "Update:" + response);
            }

            @Override
            public void onFailure(ErrorDto error) {
            }
        });
    }

    public static void deleteAndroidDevice(final BaseHTTPCallBack callBack) {
        final String url = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + URL_DELETE_ANDROID_DEVICE;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
        params.put("timeZoneOffset", "" + Util.getTimezoneOffsetInMinutes());
        params.put("languageCode", Util.getPhoneLanguage());
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d("Delete", " :" + response);
                callBack.onHTTPSuccess();
            }

            @Override
            public void onFailure(ErrorDto error) {
                Log.d("Delete", " fail");
                callBack.onHTTPFail(error);
            }
        });
    }
    /**
     * CHANGE PASSWORD
     */
    public void ChangePassword(String originalPassword, final String newPassword, final OnChangePasswordCallBack callBack) {
        String url = root_link + Urls.URL_CHANGE_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("sessionId", "" + CrewBoardApplication.getInstance().getmPrefs().getaccesstoken());
        params.put("languageCode", Locale.getDefault().getLanguage().toUpperCase());
        params.put("timeZoneOffset", "" +TimeUtils.getTimezoneOffsetInMinutes());
        params.put("originalPassword", originalPassword);
        params.put("newPassword", newPassword);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String newSessionID = json.getString("newSessionID");
                    CrewBoardApplication.getInstance().getmPrefs().putaccesstoken(newSessionID);
                    if (callBack != null) {
                        callBack.onSuccess(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ErrorDto errorDto = new ErrorDto();
                    errorDto.message = e.toString();
                    callBack.onFail(errorDto);
                }

            }

            @Override
            public void onFailure(ErrorDto error) {
                if (callBack != null) {
                    callBack.onFail(error);
                }
            }
        });
    }

//    public void updateAndroidDevice(String regid, String json) {
//        final String url = _Application.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + URL_UPDATE_ANDROID_DEVICE;
//
//        Map<String, String> params = new HashMap<>();
//        params.put("sessionId", "" + _Application.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
//        params.put("timeZoneOffset", "" + Util.getTimezoneOffsetInMinutes());
//        params.put("deviceID", regid);
//        params.put("languageCode", Util.getPhoneLanguage());
//        params.put("notificationOptions", json);
//        WebServiceManager webServiceManager = new WebServiceManager();
//        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
//            @Override
//            public void onSuccess(String response) {
//
//                Log.e("Update API", "Update:" + response);
//            }
//
//            @Override
//            public void onFailure(ErrorDto error) {
//
////                baseHTTPCallBack.onHTTPFail(error);
//            }
//        });
//    }

    public void GetUser(int userNo, final GetUserCallBack callBack) {
        String url = root_link + URL_GET_USER;
        Log.e(">>>> GetUser:", root_link +url);
        Map<String, Object> params = new HashMap<>();
        params.put("sessionId", CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes());
        params.put("userNo", userNo);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(">>> response ", response);
                Type listType = new TypeToken<Profile>() {
                }.getType();
                Profile profile = new Gson().fromJson(response, listType);
                callBack.onGetUserSuccess(profile);
            }

            @Override
            public void onFailure(ErrorDto error) {
                Log.d(">>>", "onFailure");
            }
        });
    }

    public void checkSSL(final ICheckSSL checkSSL) {
        final String url = Config.URL_CHECK_SSL;
        Map<String, String> params = new HashMap<>();
        params.put("Domain", CrewBoardApplication.getInstance().getPreferenceUtilities().getCompanyName());
        params.put("Applications", "CrewApproval");

        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean hasSSL = jsonObject.getBoolean("SSL");
                    CrewBoardApplication.getInstance().getPreferenceUtilities().setBoolean(Constants.HAS_SSL, hasSSL);
                    checkSSL.hasSSL(hasSSL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ErrorDto error) {
                checkSSL.checkSSLError(error);
            }
        });
    }

}