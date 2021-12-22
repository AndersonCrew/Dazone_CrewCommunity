package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by tunglam on 12/23/16.
 */
public class BodyRequest {
    private String LanguageCode;
    private String sessionId;

    private int type;
    private int countPerPage;
    private int CurentPage;

    private int boardno;
    private int contentNo;

    public BodyRequest(String LanguageCode, String sessionId) {
        this.LanguageCode = LanguageCode;
        this.sessionId = sessionId;
    }

    public BodyRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    public void setCurentPage(int curentPage) {
        this.CurentPage = curentPage;
    }

    public String getLanguageCode() {
        return LanguageCode;
    }

    public void setLanguageCode(String languageCode) {
            LanguageCode = languageCode;
    }

    public void setBoardno(int boardno) {
        this.boardno = boardno;
    }

    public void setContentNo(int contentNo) {
        this.contentNo = contentNo;
    }
}
