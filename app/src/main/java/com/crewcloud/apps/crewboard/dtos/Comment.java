package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dazone on 2/23/2017.
 */

public class Comment extends RealmObject {
    @SerializedName("ReplyNo")
    private int replyNo;

    @SerializedName("ContentNo")
    private int contentNo;

    @SerializedName("ModUserNo")
    private int modUserNo;
    @SerializedName("ModPositionNo")
    private int modPositionNo;

    @SerializedName("ModUserName")
    private String modUserName;

    @SerializedName("ModPositionName")
    private String modPositionName;
    @SerializedName("ModDepartNo")
    private int modDepartNo;

    @SerializedName("ModDepartName")
    private String modDepartName;

    @SerializedName("RegDate")
    private String regDate;

    @SerializedName("ModDate")
    private String modDate;

    @SerializedName("Content")
    private String Content;

    @SerializedName("GroupNo")
    private int groupNo;

    @SerializedName("Depth")
    private int depth;

    @SerializedName("OrderNo")
    private int orderNo;

    @SerializedName("AvatarUrl")
    private String avatar;


    public Comment() {
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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

    public int getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(int groupNo) {
        this.groupNo = groupNo;
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

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getReplyNo() {
        return replyNo;
    }

    public void setReplyNo(int replyNo) {
        this.replyNo = replyNo;
    }
}
