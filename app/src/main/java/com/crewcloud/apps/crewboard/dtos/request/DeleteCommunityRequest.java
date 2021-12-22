package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 3/7/2017.
 */

public class DeleteCommunityRequest {
    private int contentno;
    private String sessionId;

    public DeleteCommunityRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setContentno(int contentno) {
        this.contentno = contentno;
    }
}
