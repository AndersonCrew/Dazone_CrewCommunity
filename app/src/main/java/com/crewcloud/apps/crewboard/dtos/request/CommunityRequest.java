package com.crewcloud.apps.crewboard.dtos.request;

import java.util.List;

/**
 * Created by dazone on 2/28/2017.
 */

public class CommunityRequest {
    private String sessionId;
    private int boardno;

    private String content;
//    private List<String> attactments;
    private String attactment;
    private String title;
    private boolean alarm;

    public CommunityRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public void setAttactments(String attactments) {
        this.attactment  = attactments;
    }

    public void setBoardno(int boardno) {
        this.boardno = boardno;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
