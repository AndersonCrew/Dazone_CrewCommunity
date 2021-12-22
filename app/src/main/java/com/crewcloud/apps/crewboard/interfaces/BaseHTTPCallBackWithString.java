package com.crewcloud.apps.crewboard.interfaces;


import com.crewcloud.apps.crewboard.dtos.ErrorDto;

public interface BaseHTTPCallBackWithString {
    void onHTTPSuccess(String message);
    void onHTTPFail(ErrorDto errorDto);
}
