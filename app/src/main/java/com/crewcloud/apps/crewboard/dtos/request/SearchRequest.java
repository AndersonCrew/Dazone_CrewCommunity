package com.crewcloud.apps.crewboard.dtos.request;

/**
 * Created by dazone on 2/22/2017.
 */

public class SearchRequest {
    private String languageSign;
    private String sessionId;

    private int countPerPage;
    private int sortColumn;
    private boolean isAscending;
    private int currentPageIndex;
    private String searchText;



    public SearchRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setCountPerPage(int countPerPage) {
        this.countPerPage = countPerPage;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public void setAscending(boolean ascending) {
        isAscending = ascending;
    }

    public void setLanguageSign(String languageSign) {
        this.languageSign = languageSign;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSortColumn(int sortColumn) {
        this.sortColumn = sortColumn;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
