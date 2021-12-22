package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 2/24/2017.
 */

public class CommentRequest {
    private String languageCode;
    private String sessionId;
    private String role;
    private int contentno;

    public CommentRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContentno(int contentno) {
        this.contentno = contentno;
    }
}
