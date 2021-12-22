package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 2/24/2017.
 */

public class EditRequest {
    private String sessionId;
    private String comment;
    private int replyNo;
    private String role;
    private String languageCode;

    public EditRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



    public void setReplyNo(int replyNo) {
        this.replyNo = replyNo;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
