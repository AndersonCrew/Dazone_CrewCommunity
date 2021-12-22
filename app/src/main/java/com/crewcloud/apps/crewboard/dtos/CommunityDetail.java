package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by dazone on 2/23/2017.
 */

public class CommunityDetail extends RealmObject implements Serializable {

    @SerializedName("ContentNo")
    private int contentNo;

    @SerializedName("BoardNo")
    private int boardNo;

    @SerializedName("ReadCount")
    private int readCount;

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
    private int modDepartNo;

    @SerializedName("ModDepartName")
    private String modDepartName;

    @SerializedName("RegPositionNo")
    private int regPositionNo;

    @SerializedName("RegPositionName")
    private String regPositionName;

    @SerializedName("RegUserNo")
    private int regUserNo;

    @SerializedName("RegDepartNo")
    private int regDepartNo;

    @SerializedName("RegDepartName")
    private String regDepartName;

    @SerializedName("AvataContent")
    private String avatarContent;

    @SerializedName("HTMLAvataContent")
    private String htmlAvataContent;

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
    private int headNo;

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
    private int recommentCount;

    @SerializedName("ViewedCount")
    private int viewedCount;

    @SerializedName("StartDate")
    private String startDate;

    @SerializedName("EndDate")
    private String endDate;

    @SerializedName("HeadName")
    private String headName;

    @SerializedName("IsRecommended")
    private boolean isRecommended;

    @SerializedName("IsAttach")
    private boolean isAttach;

    @SerializedName("DisplayContentNo")
    private String displayContentNO;

    @SerializedName("DisplayTitle")
    private String disPlaytitle;

    @SerializedName("DisplaytitlePhoto")
    private String displayTitlePhoto;

    @SerializedName("RegDateToString")
    private String regDateToString;

    @SerializedName("ViewedCountToString")
    private String viewCountToString;

    @SerializedName("RecommendedCountToString")
    private String recommendedCountToString;

    @SerializedName("Email")
    private String email;

    private List<Attachments> attachmentses;

    public CommunityDetail() {
    }

    public List<Attachments> getAttachmentses() {
        return attachmentses;
    }

    public void setAttachmentses(List<Attachments> attachmentses) {
        this.attachmentses = attachmentses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarContent() {
        return avatarContent;
    }

    public void setAvatarContent(String avatarContent) {
        this.avatarContent = avatarContent;
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

    public String getDisplayContentNO() {
        return displayContentNO;
    }

    public void setDisplayContentNO(String displayContentNO) {
        this.displayContentNO = displayContentNO;
    }

    public String getDisPlaytitle() {
        return disPlaytitle;
    }

    public void setDisPlaytitle(String disPlaytitle) {
        this.disPlaytitle = disPlaytitle;
    }

    public String getDisplayTitlePhoto() {
        return displayTitlePhoto;
    }

    public void setDisplayTitlePhoto(String displayTitlePhoto) {
        this.displayTitlePhoto = displayTitlePhoto;
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

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public int getHeadNo() {
        return headNo;
    }

    public void setHeadNo(int headNo) {
        this.headNo = headNo;
    }

    public String getHtmlAvataContent() {
        return htmlAvataContent;
    }

    public void setHtmlAvataContent(String htmlAvataContent) {
        this.htmlAvataContent = htmlAvataContent;
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

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
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

    public int getModDepartNo() {
        return modDepartNo;
    }

    public void setModDepartNo(int modDepartNo) {
        this.modDepartNo = modDepartNo;
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

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getRecommendedCountToString() {
        return recommendedCountToString;
    }

    public void setRecommendedCountToString(String recommendedCountToString) {
        this.recommendedCountToString = recommendedCountToString;
    }

    public int getRecommentCount() {
        return recommentCount;
    }

    public void setRecommentCount(int recommentCount) {
        this.recommentCount = recommentCount;
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

    public int getRegPositionNo() {
        return regPositionNo;
    }

    public void setRegPositionNo(int regPositionNo) {
        this.regPositionNo = regPositionNo;
    }

    public int getRegUserNo() {
        return regUserNo;
    }

    public void setRegUserNo(int regUserNo) {
        this.regUserNo = regUserNo;
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

    public String getViewCountToString() {
        return viewCountToString;
    }

    public void setViewCountToString(String viewCountToString) {
        this.viewCountToString = viewCountToString;
    }

    public int getViewedCount() {
        return viewedCount;
    }

    public void setViewedCount(int viewedCount) {
        this.viewedCount = viewedCount;
    }
}
