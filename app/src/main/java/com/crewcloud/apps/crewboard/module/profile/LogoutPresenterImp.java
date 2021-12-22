package com.crewcloud.apps.crewboard.module.profile;

import android.support.annotation.NonNull;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.request.ProfileRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;

/**
 * Created by dazone on 2/28/2017.
 */

public class LogoutPresenterImp extends BasePresenter<LogoutPresenter.view> implements LogoutPresenter.presenter {

    public LogoutPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void logout(String sessionId) {
        BodyRequest request = new BodyRequest(sessionId);
        activity.showProgressDialog();
        activity.requestAPI(activity.getApi().logout(request), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                activity.dismissProgressDialog();
                getView().onLogoutSuccess();

            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                activity.dismissProgressDialog();
                getView().onError(messageResponse.getMessage());
            }
        });
    }

    @Override
    public void getProfile() {
        String sessionId = CrewBoardApplication.getInstance().getmPrefs().getaccesstoken();
        String languageCode = Util.getPhoneLanguage();
        ProfileRequest request = new ProfileRequest(sessionId, languageCode);
        activity.showProgressDialog();
        activity.requestAPI(activity.getApi().getUser(request), new ResponseListener<BaseResponse<MenuResponse<Profile>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<Profile>> result) {
                activity.dismissProgressDialog();
                Profile profile = result.getData().getList();
                if (profile != null) {
                    getView().onGetProfileSuccess(profile);
                }

            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                activity.dismissProgressDialog();
                getView().onError(messageResponse.getMessage());
            }
        });
    }
}
