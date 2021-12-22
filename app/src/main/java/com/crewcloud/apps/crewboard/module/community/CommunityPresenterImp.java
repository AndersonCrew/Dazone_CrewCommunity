package com.crewcloud.apps.crewboard.module.community;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.Content;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.request.SearchRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.gson.Gson;

/**
 * Created by dazone on 2/22/2017.
 */

public class CommunityPresenterImp extends BasePresenter<CommunityPresenter.view> implements CommunityPresenter.presenter {

    public CommunityPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void getCommunity(int curentPage) {
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String languageCode = Util.getPhoneLanguage();
        BodyRequest bodyRequest = new BodyRequest(sessionId);
        bodyRequest.setCurentPage(curentPage);
        bodyRequest.setCountPerPage(20);
        bodyRequest.setLanguageCode(languageCode);
        activity.requestAPI(activity.getApi().getCommunity(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                Log.d("TAG", result.getData().getList());
                Content data = new Gson().fromJson(result.getData().getList(), Content.class);
                if (data != null) {
                    getView().onSuccess(data.getCommunities());
                }
            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                getView().onError(messageResponse.getMessage());
            }
        });
    }

    @Override
    public void searchCommunity(String textSearch, int curentPage) {
        activity.showProgressDialog();
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String language = Util.getPhoneLanguage();

        SearchRequest bodyRequest = new SearchRequest(sessionId);
        bodyRequest.setCurrentPageIndex(curentPage);
        bodyRequest.setCountPerPage(20);
        bodyRequest.setLanguageSign(language);
        bodyRequest.setAscending(true);
        bodyRequest.setSortColumn(1);
        bodyRequest.setSearchText(textSearch);

        activity.requestAPI(activity.getApi().searchCommunity(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                activity.dismissProgressDialog();
                Content data = new Gson().fromJson(result.getData().getList(), Content.class);
                if (data != null) {
                    getView().onSuccess(data.getCommunities());
                }

            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                activity.dismissProgressDialog();
                getView().onError(messageResponse.getMessage());
            }
        });
    }

    @Override
    public void getCommunityById(int id, int curentPage) {
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String language = Util.getPhoneLanguage();

        BodyRequest bodyRequest = new BodyRequest(sessionId);
        bodyRequest.setCurentPage(curentPage);
        bodyRequest.setCountPerPage(20);
        bodyRequest.setLanguageCode(language);
        bodyRequest.setBoardno(id);


        activity.requestAPI(activity.getApi().getCommunityById(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                Content data = new Gson().fromJson(result.getData().getList(), Content.class);
                getView().onSuccess(data.getCommunities());
            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                getView().onError(messageResponse.getMessage());
            }
        });

    }
}
