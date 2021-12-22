package com.crewcloud.apps.crewboard.interfaces;


import com.crewcloud.apps.crewboard.dtos.ErrorDto;

public interface OnHasAppCallBack {
    void hasApp(String url);
    void noHas(ErrorDto dto);
}
