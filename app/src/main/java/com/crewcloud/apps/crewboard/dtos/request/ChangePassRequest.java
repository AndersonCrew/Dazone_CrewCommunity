package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 5/11/2017.
 */

public class ChangePassRequest {
    private String languageCode;
    private String sessionId;
    private String timeZoneOffset;
    private String originalPassword;
    private String newPassword;

    public ChangePassRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimeZoneOffset(String timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public void setOriginalPassword(String originalPassword) {
        this.originalPassword = originalPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
