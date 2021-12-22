package com.crewcloud.apps.crewboard.module.userview;

import android.support.annotation.NonNull;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.Comment;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.UserViewCommunity;
import com.crewcloud.apps.crewboard.dtos.request.CommentRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dazone on 3/13/2017.
 */

public class UserViewPresenterImp extends BasePresenter<UserViewPresenter.view> implements UserViewPresenter.presenter {

    public UserViewPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void getUserView(int contentno) {
        activity.showProgressDialog();
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String language = Util.getPhoneLanguage();

        CommentRequest bodyRequest = new CommentRequest(sessionId);
        bodyRequest.setLanguageCode(language);
        bodyRequest.setContentno(contentno);
        bodyRequest.setRole("");

        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().getUserViewCommunity(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                    activity.dismissProgressDialog();
                    UserViewCommunity[] data = new Gson().fromJson(result.getData().getList(), UserViewCommunity[].class);
                    List<UserViewCommunity> lstUserViewCommunities = new ArrayList<>();
                    for (int index = 0; index < data.length; index++) {
                        lstUserViewCommunities.add(data[index]);
                    }
                    getView().onGetUserViewSuccess(lstUserViewCommunities);
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    activity.dismissProgressDialog();
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }
}
