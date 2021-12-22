package com.crewcloud.apps.crewboard.interfaces;


import com.crewcloud.apps.crewboard.dtos.ErrorDto;

/**
 * Created by Dat on 7/27/2016.
 */
public interface OnAutoLoginCallBack {
    void OnAutoLoginSuccess(String response);
    void OnAutoLoginFail(ErrorDto dto);
}
