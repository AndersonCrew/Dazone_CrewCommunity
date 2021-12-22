package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dazone on 2/22/2017.
 */

public class Community implements Serializable {
    @SerializedName("ContentNo")
    private int contentNo;

    @SerializedName("BoardNo")
    private int boardNo;

    @SerializedName("ReadNo")
    private int readNo;

    @SerializedName("BoardName")
    private String boardName;

    @SerializedName("ModUserNo")
    private int modUserNo;

    @SerializedName("ModUserName")
    private String modUserName;

    @SerializedName("ModPositionNo")
    private int modPositionNo;

    @SerializedName("ModPositionName")
    private String modPositionName;

    @SerializedName("ModDepartNo")
    private int mobDepartNo;

    @SerializedName("ModDepartName")
    private String modDepartName;

    @SerializedName("RegUserNo")
    private int redUserNo;

    @SerializedName("RegUserName")
    private String regUserName;

    @SerializedName("RegPositionNo")
    private String regPositionName;

    @SerializedName("RegDepartNo")
    private int regDepartNo;

    @SerializedName("RegDepartName")
    private String regDepartName;

    @SerializedName("AvataContent")
    private String avataContent;

    @SerializedName("RegDate")
    private String regDate;

    @SerializedName("ModDate")
    private String modDate;

    @SerializedName("Title")
    private String title;

    @SerializedName("TitleEffect")
    private int titleEffect;

    @SerializedName("GroupNo")
    private int groupNo;

    @SerializedName("Depth")
    private int depth;

    @SerializedName("OrderNo")
    private int orderNo;

    @SerializedName("HeadNo")
    private  int headNo;

    @SerializedName("IsNotice")
    private boolean isNotice;

    @SerializedName("IsAlarm")
    private boolean isAlarm;

    @SerializedName("Content")
    private String content;

    @SerializedName("IsFile")
    private boolean isFile;

    @SerializedName("FileCount")
    private int fileCount;

    @SerializedName("ReplyCount")
    private int replyCount;

    @SerializedName("RecommendedCount")
    private int recommendedCount;

    @SerializedName("ViewedCount")
    private int viewCount;

    @SerializedName("StartDate")
    private String startDate;

    @SerializedName("EndDate")
    private  String endDate;

    @SerializedName("IsRecommended")
    private boolean isRecommended;

    @SerializedName("IsAttach")
    private boolean isAttach;

    @SerializedName("DisplayContentNo")
    private String disPlayContentNo;

    @SerializedName("DisplayTitle")
    private String disPlaytitle;

    @SerializedName("DisplayTitlephoto")
    private String isplayTitlephoto;

    @SerializedName("RegDateToString")
    private String regDateToString;

    @SerializedName("ViewedCountToString")
    private String viewedCountToString;

    @SerializedName("RecommendedCountToString")
    private String recommendedCountToString;

    public Community() {
    }

    public String getAvataContent() {
        return avataContent;
    }

    public void setAvataContent(String avataContent) {
        this.avataContent = avataContent;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getContentNo() {
        return contentNo;
    }

    public void setContentNo(int contentNo) {
        this.contentNo = contentNo;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getDisPlayContentNo() {
        return disPlayContentNo;
    }

    public void setDisPlayContentNo(String disPlayContentNo) {
        this.disPlayContentNo = disPlayContentNo;
    }

    public String getDisPlaytitle() {
        return disPlaytitle;
    }

    public void setDisPlaytitle(String disPlaytitle) {
        this.disPlaytitle = disPlaytitle;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
    }

    public int getHeadNo() {
        return headNo;
    }

    public void setHeadNo(int headNo) {
        this.headNo = headNo;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public boolean isAttach() {
        return isAttach;
    }

    public void setAttach(boolean attach) {
        isAttach = attach;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
    }

    public String getIsplayTitlephoto() {
        return isplayTitlephoto;
    }

    public void setIsplayTitlephoto(String isplayTitlephoto) {
        this.isplayTitlephoto = isplayTitlephoto;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public int getMobDepartNo() {
        return mobDepartNo;
    }

    public void setMobDepartNo(int mobDepartNo) {
        this.mobDepartNo = mobDepartNo;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getModDepartName() {
        return modDepartName;
    }

    public void setModDepartName(String modDepartName) {
        this.modDepartName = modDepartName;
    }

    public String getModPositionName() {
        return modPositionName;
    }

    public void setModPositionName(String modPositionName) {
        this.modPositionName = modPositionName;
    }

    public int getModPositionNo() {
        return modPositionNo;
    }

    public void setModPositionNo(int modPositionNo) {
        this.modPositionNo = modPositionNo;
    }

    public String getModUserName() {
        return modUserName;
    }

    public void setModUserName(String modUserName) {
        this.modUserName = modUserName;
    }

    public int getModUserNo() {
        return modUserNo;
    }

    public void setModUserNo(int modUserNo) {
        this.modUserNo = modUserNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getReadNo() {
        return readNo;
    }

    public void setReadNo(int readNo) {
        this.readNo = readNo;
    }

    public int getRecommendedCount() {
        return recommendedCount;
    }

    public void setRecommendedCount(int recommendedCount) {
        this.recommendedCount = recommendedCount;
    }

    public String getRecommendedCountToString() {
        return recommendedCountToString;
    }

    public void setRecommendedCountToString(String recommendedCountToString) {
        this.recommendedCountToString = recommendedCountToString;
    }

    public int getRedUserNo() {
        return redUserNo;
    }

    public void setRedUserNo(int redUserNo) {
        this.redUserNo = redUserNo;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegDateToString() {
        return regDateToString;
    }

    public void setRegDateToString(String regDateToString) {
        this.regDateToString = regDateToString;
    }

    public String getRegDepartName() {
        return regDepartName;
    }

    public void setRegDepartName(String regDepartName) {
        this.regDepartName = regDepartName;
    }

    public int getRegDepartNo() {
        return regDepartNo;
    }

    public void setRegDepartNo(int regDepartNo) {
        this.regDepartNo = regDepartNo;
    }

    public String getRegPositionName() {
        return regPositionName;
    }

    public void setRegPositionName(String regPositionName) {
        this.regPositionName = regPositionName;
    }

    public String getRegUserName() {
        return regUserName;
    }

    public void setRegUserName(String regUserName) {
        this.regUserName = regUserName;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleEffect() {
        return titleEffect;
    }

    public void setTitleEffect(int titleEffect) {
        this.titleEffect = titleEffect;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getViewedCountToString() {
        return viewedCountToString;
    }

    public void setViewedCountToString(String viewedCountToString) {
        this.viewedCountToString = viewedCountToString;
    }
}