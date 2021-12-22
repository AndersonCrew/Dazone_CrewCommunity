package com.crewcloud.apps.crewboard.base;

import com.google.gson.annotations.SerializedName;


/**
 * Created by dazone on 2/21/2017.
 */
public class BaseResponse<D> {

    @SerializedName("d")
    private D d;

    public BaseResponse() {
    }

    public D getData() {
        return d;
    }

    public void setData(D d) {
        this.d = d;
    }
}
