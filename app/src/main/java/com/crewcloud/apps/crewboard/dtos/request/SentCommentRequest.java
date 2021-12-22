package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 2/24/2017.
 */

public class SentCommentRequest {
    private String sessionId;
    private int contentno;
    private int groupno;
    private int orderNo;
    private int depth;
    private String comment;


    public SentCommentRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setContentno(int contentno) {
        this.contentno = contentno;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setGroupno(int groupno) {
        this.groupno = groupno;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
