package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 5/12/2017.
 */

public class ProfileRequest {
    private String sessionId;
    private String languageCode;

    public ProfileRequest(String sessionId, String languageCode) {
        this.sessionId = sessionId;
        this.languageCode = languageCode;
    }
}
