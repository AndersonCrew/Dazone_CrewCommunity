package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by dazone on 3/9/2017.
 */

public class UserViewCommunity implements Serializable {
    @SerializedName("LogNo")
    private int logNo;

    @SerializedName("BoardNo")
    private int boardNo;

    @SerializedName("ContentNo")
    private int contentNo;

    @SerializedName("UserNo")
    private int userNo;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("PositionNo")
    private int positionNo;

    @SerializedName("PositionName")
    private String positionName;

    @SerializedName("DepartNo")
    private int departNo;

    @SerializedName("DepartName")
    private String departName;

    @SerializedName("ViewedDate")
    private String viewedDate;

    @SerializedName("ClientIP")
    private String clientIP;

    @SerializedName("MailAddress")
    private String mailAddress;

    @SerializedName("Avatar")
    private String avatar;

    @SerializedName("ViewedDateToString")
    private String viewedDateToString;

    private boolean mIsSelected;
    private int TypeColor = 2;

    public UserViewCommunity() {
    }

    public int getLogNo() {
        return logNo;
    }

    public void setLogNo(int logNo) {
        this.logNo = logNo;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public int getContentNo() {
        return contentNo;
    }

    public void setContentNo(int contentNo) {
        this.contentNo = contentNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPositionNo() {
        return positionNo;
    }

    public void setPositionNo(int positionNo) {
        this.positionNo = positionNo;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public int getDepartNo() {
        return departNo;
    }

    public void setDepartNo(int departNo) {
        this.departNo = departNo;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getViewedDate() {
        return viewedDate;
    }

    public void setViewedDate(String viewedDate) {
        this.viewedDate = viewedDate;
    }

    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getViewedDateToString() {
        return viewedDateToString;
    }

    public void setViewedDateToString(String viewedDateToString) {
        this.viewedDateToString = viewedDateToString;
    }

    public boolean ismIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public int getTypeColor() {
        return TypeColor;
    }

    public void setTypeColor(int typeColor) {
        TypeColor = typeColor;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
