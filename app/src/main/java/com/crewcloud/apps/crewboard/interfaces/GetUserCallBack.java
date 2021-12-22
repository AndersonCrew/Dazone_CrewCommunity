package com.crewcloud.apps.crewboard.interfaces;

import com.crewcloud.apps.crewboard.dtos.Profile;

/**
 * Created by dazone on 5/9/2017.
 */

public interface GetUserCallBack {

    void onGetUserSuccess(Profile profile);
    void onError();
}
