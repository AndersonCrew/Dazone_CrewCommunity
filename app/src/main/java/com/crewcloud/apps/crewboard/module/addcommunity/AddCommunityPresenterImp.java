package com.crewcloud.apps.crewboard.module.addcommunity;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.request.CommunityRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by dazone on 2/28/2017.
 */

public class AddCommunityPresenterImp extends BasePresenter<AddCommunityPresenter.view> implements AddCommunityPresenter.presenter {

    public AddCommunityPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void addCommunity(String title, String content, List<String> attactments, int boardno, boolean alarm) {
        activity.showProgressDialog();
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        CommunityRequest request = new CommunityRequest(sessionId);
        request.setAlarm(alarm);
        request.setAttactments("");
        request.setBoardno(boardno);
        request.setTitle(title);
        request.setContent(content);

        activity.requestAPI(activity.getApi().addCommunity(request), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                Log.d("SUCCESS", "Add Community Success");
                activity.dismissProgressDialog();
                getView().onAddSuccess();
            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                Log.d("ERROR", "Add Community Error");
                activity.dismissProgressDialog();
                getView().onError(messageResponse.getMessage());
            }
        });

    }

    @Override
    public void validate(String title, String content, int broadNo) {
        if (TextUtils.isEmpty(title)) {
            getView().onError(activity.getString(R.string.can_not_blank_title));
        } else if (broadNo <= 0) {
            getView().onError(activity.getString(R.string.please_select_a_board));
        } else if (TextUtils.isEmpty(content)) {
            getView().onError("Can not blank Content");
        } else {
            getView().onValiDateSuccess(title, content);
        }
    }

    @Override
    public void getMenuLeft() {
        if (isViewAttached()) {
//            getView().onGetLeftMenuSuccess(DataFactory.getListMenu());
            String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();

            BodyRequest bodyRequest = new BodyRequest(sessionId);
            bodyRequest.setCountPerPage(100);
            bodyRequest.setCurentPage(1);
            bodyRequest.setType(-1);

            activity.requestAPI(activity.getApi().getLeftMenu(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                    Log.d("SUCCESS", result.toString());
                    ChildFolders[] data = new Gson().fromJson(result.getData().getList(), ChildFolders[].class);
                    List<ChildFolders> leftMenus = new ArrayList<>();
                    for (int index = 0; index < data.length; index++) {
                        leftMenus.add(data[index]);
                    }
                    getView().onGetSuccess(leftMenus);
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    getView().onError(messageResponse.getMessage());
                }

                @Override
                public BaseResponse<MenuResponse<String>> onFetchDataFromCacheBG(Realm realm) {
                    return super.onFetchDataFromCacheBG(realm);
                }
            });
        }
    }

    @Override
    public void uploadFile(Uri fileUri) {
    }
}
