package com.crewcloud.apps.crewboard.module.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dazone on 3/8/2017.
 */

public class LoginPresenterImp extends BasePresenter<LoginPresenter.view> implements LoginPresenter.presenter {

    public LoginPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void validateData(String server, String personal, String pass) {
        boolean isOk = true;
        if (TextUtils.isEmpty(server)) {
            isOk = false;
            getView().onError(activity.getString(R.string.login_empty_input) + activity.getString(R.string.string_server_site));
        } else if (TextUtils.isEmpty(personal)) {
            isOk = false;
            getView().onError(activity.getString(R.string.login_empty_input) + activity.getString(R.string.login_username));
        } else if (TextUtils.isEmpty(pass)) {
            isOk = false;
            getView().onError(activity.getString(R.string.login_empty_input) + activity.getString(R.string.login_password));
        }

        if (isOk) {
            login(server, personal, pass);
        }

    }

    @Override
    public void login(String server, String personal, String pass) {
        activity.showProgressDialog();
        Map<String, String> params = new HashMap<>();
        params.put("languageCode", Util.getPhoneLanguage());
        params.put("timeZoneOffset", "" + Util.getTimeOffsetInMinute());
        params.put("companyDomain", server);
        params.put("password", pass);
        params.put("userID", personal);
        params.put("mobileOSVersion", "Android " + android.os.Build.VERSION.RELEASE);
        activity.requestAPI(activity.getApi().login(params), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                activity.dismissProgressDialog();
                if (isViewAttached()){
                    getView().onLoginSuccess();
                }
            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                activity.dismissProgressDialog();
            }
        });

    }
}
