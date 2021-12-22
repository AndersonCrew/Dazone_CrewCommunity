package com.crewcloud.apps.crewboard.interfaces;

import com.crewcloud.apps.crewboard.dtos.ErrorDto;

public interface OnChangePasswordCallBack {
    void onSuccess(String response);
    void onFail(ErrorDto errorDto);
}