package com.crewcloud.apps.crewboard.interfaces;

import android.view.View;

import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.dtos.UserDto;

/**
 * Created by dazone on 5/9/2017.
 */

public interface MyProfileCallBack {
    void onClickCall(View view, String phonenumber);

    void onClickMessage(View view, String phoneNumber);

    void onClickSendEmail(View view, String Email);

    void onClickBack(View view);

    void onClickChangePass(View view, Profile data);
}
