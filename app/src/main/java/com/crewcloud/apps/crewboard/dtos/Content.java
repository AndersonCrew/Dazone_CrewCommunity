package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dazone on 2/22/2017.
 */

public class Content {

    @SerializedName("Contents")
    private List<Community> communities;

    @SerializedName("TotalContentCount")
    private int totalContentCount;

    @SerializedName("TotalPageCount")
    private int totalPageCount;

    @SerializedName("CurrentPageIndex")
    private int currentPageIndex;

    public Content() {
    }

    public List<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(List<Community> communities) {
        this.communities = communities;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getTotalContentCount() {
        return totalContentCount;
    }

    public void setTotalContentCount(int totalContentCount) {
        this.totalContentCount = totalContentCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }
}
