package com.crewcloud.apps.crewboard.interfaces;


import com.crewcloud.apps.crewboard.dtos.ErrorDto;

public interface BaseHTTPCallBack {
    void onHTTPSuccess();
    void onHTTPFail(ErrorDto errorDto);
}
