package com.crewcloud.apps.crewboard.module.profile.changepass;

import android.text.TextUtils;

import com.android.volley.Request;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.request.ChangePassRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.Urls;
import com.crewcloud.apps.crewboard.util.Util;
import com.crewcloud.apps.crewboard.util.WebServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dazone on 5/11/2017.
 */

public class ChangePassPresenterImp extends BasePresenter<ChangePassPresenter.view> implements ChangePassPresenter.presenter {

    public ChangePassPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void ChangePass(String oldPass, final String newPass) {
        final String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        long timeZoneOffset = Util.getTimeOffsetInMinute();
        String languageCode = Util.getPhoneLanguage();

        ChangePassRequest request = new ChangePassRequest(sessionId);
        request.setLanguageCode(languageCode);
        request.setNewPassword(newPass);
        request.setOriginalPassword(oldPass);
        request.setTimeZoneOffset(String.valueOf(timeZoneOffset));

        final String url = Urls.URL_CHANGE_PASS;
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("sessionId", sessionId);
        params.put("timeZoneOffset", "" + timeZoneOffset);
        params.put("originalPassword", oldPass);
        params.put("newPassword", newPass);
        WebServiceManager webServiceManager = new WebServiceManager();
        webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String newSessionId = jsonObject.getString("newSessionID");
                    Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();
                    PreferenceUtilities preference = CrewBoardApplication.getInstance().getPreferenceUtilities();
                    preference.setCurrentMobileSessionId(newSessionId);
                    preference.setPass(newPass);
                    prefs.putPass(newPass);
                    prefs.putaccesstoken(newSessionId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                getView().ChangePassSuccess();

            }

            @Override
            public void onFailure(ErrorDto error) {
                String messageDto = error.getMessage();
                getView().ChangePassError(messageDto);
            }
        });

    }

    @Override
    public void CheckPass(String oldPass, String newPass, String confirmPass) {
        if (TextUtils.isEmpty(oldPass)) {
            getView().ChangePassError("You can't leave this empty current password");
        } else if (TextUtils.isEmpty(newPass)) {
            getView().ChangePassError("You can't leave this empty new password");
        } else if (TextUtils.isEmpty(confirmPass)) {
            getView().ChangePassError("These passwords don't match. Try again?");
        } else if (!newPass.equals(confirmPass)) {
            getView().ChangePassError("These passwords don't match. Try again?");
        } else {
            ChangePass(oldPass, newPass);
        }
    }
}
