package com.crewcloud.apps.crewboard.module.leftmenu;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.LeftMenu;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by dazone on 2/21/2017.
 */

public class LeftMenuPresenterImp extends BasePresenter<LeftMenuPresenter.view> implements LeftMenuPresenter.presenter {

    public LeftMenuPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void getLeftMenu() {
        if (isViewAttached()) {
//            getView().onGetLeftMenuSuccess(DataFactory.getListMenu());
            String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
            String languageCode = Util.getPhoneLanguage();

            BodyRequest bodyRequest = new BodyRequest(sessionId);
            bodyRequest.setCountPerPage(100);
            bodyRequest.setCurentPage(1);
            bodyRequest.setType(-1);

//            final String url = "http://dazone.crewcloud.net/UI/mobileBoard/Handlers/MobileService.asmx/GetHomeBoad";
//            Map<String, String> params = new HashMap<>();
//            params.put("sessionId", sessionId);
//            params.put("type", "-1");
//            params.put("countPerPage", "0");
//            params.put("CurentPage", "0");
//            WebServiceManager webServiceManager = new WebServiceManager();
//            webServiceManager.doJsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new WebServiceManager.RequestListener<String>() {
//                @Override
//                public void onSuccess(String response) {
//                    Util.printLogs("User info =" + response);
//
//                }
//
//                @Override
//                public void onFailure(ErrorDto error) {
//                    Util.printLogs("User info =" + error.getMessage());
//
//                }
//            });

            activity.requestAPI(activity.getApi().getLeftMenu(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                    Log.d("SUCCESS", result.toString());
                    ChildFolders[] data = new Gson().fromJson(result.getData().getList(), ChildFolders[].class);
                    List<ChildFolders> leftMenus = new ArrayList<>();
                    for (int index = 0; index < data.length; index++) {
                        leftMenus.add(data[index]);
                    }
                    getView().onGetLeftMenuSuccess(leftMenus, isFromCache());
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
}
