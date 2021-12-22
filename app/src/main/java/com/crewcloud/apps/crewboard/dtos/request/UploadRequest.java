package com.crewcloud.apps.crewboard.dtos.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dazone on 3/6/2017.
 */

public class UploadRequest {
    @SerializedName("sessionId")
    private String sessionId;

    @SerializedName("languageCode")
    private String languageCode;

    @SerializedName("timeZoneOffset")
    private String timeZoneOffset;
}
