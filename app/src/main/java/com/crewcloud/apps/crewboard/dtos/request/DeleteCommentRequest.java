package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 2/24/2017.
 */

public class DeleteCommentRequest {
    private String sessionId;

    private int replyNo;
    private int contentno;





    public DeleteCommentRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setContentno(int contentno) {
        this.contentno = contentno;
    }

    public void setReplyNo(int replyNo) {
        this.replyNo = replyNo;
    }
}
