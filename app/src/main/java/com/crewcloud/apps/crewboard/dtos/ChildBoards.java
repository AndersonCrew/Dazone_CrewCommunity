package com.crewcloud.apps.crewboard.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import io.realm.RealmObject;

/**
 * Created by dazone on 3/3/2017.
 */

public class ChildBoards implements Parcelable {
    @SerializedName("BoardNo")
    private int boardNo;

    @SerializedName("ModUserNo")
    private int modUserNo;

    @SerializedName("ModDate")
    private String modDate;

    @SerializedName("Name")
    private String name;

    @SerializedName("Description")
    private String description;

    @SerializedName("FolderNo")
    private int folderNo;

    @SerializedName("DisplayTypeNo")
    private int displayTypeNo;

    @SerializedName("SortNo")
    private int dortNo;

    @SerializedName("IsReply")
    private boolean isReply;

    @SerializedName("IsHead")
    private boolean isHead;

    @SerializedName("IsNotice")
    private boolean isNotice;

    @SerializedName("IsRecommend")
    private boolean isRecommend;

    @SerializedName("RecommendedDisplayCount")
    private int recommendedDisplayCount;

    @SerializedName("Enabled")
    private boolean enabled;

    @SerializedName("CountContent")
    private int countContent;

    @SerializedName("ViewMode")
    private int viewMode;

    @SerializedName("group")
    private int group = 1;

    private String nameGroup;

    protected ChildBoards(Parcel in) {
        boardNo = in.readInt();
        modUserNo = in.readInt();
        modDate = in.readString();
        name = in.readString();
        description = in.readString();
        folderNo = in.readInt();
        displayTypeNo = in.readInt();
        dortNo = in.readInt();
        isReply = in.readByte() != 0;
        isHead = in.readByte() != 0;
        isNotice = in.readByte() != 0;
        isRecommend = in.readByte() != 0;
        recommendedDisplayCount = in.readInt();
        enabled = in.readByte() != 0;
        countContent = in.readInt();
        viewMode = in.readInt();
        group = in.readInt();
        nameGroup = in.readString();
    }

    public static final Creator<ChildBoards> CREATOR = new Creator<ChildBoards>() {
        @Override
        public ChildBoards createFromParcel(Parcel in) {
            return new ChildBoards(in);
        }

        @Override
        public ChildBoards[] newArray(int size) {
            return new ChildBoards[size];
        }
    };

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public int getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }

    public int getModUserNo() {
        return modUserNo;
    }

    public void setModUserNo(int modUserNo) {
        this.modUserNo = modUserNo;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(int folderNo) {
        this.folderNo = folderNo;
    }

    public int getDisplayTypeNo() {
        return displayTypeNo;
    }

    public void setDisplayTypeNo(int displayTypeNo) {
        this.displayTypeNo = displayTypeNo;
    }

    public int getDortNo() {
        return dortNo;
    }

    public void setDortNo(int dortNo) {
        this.dortNo = dortNo;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
    }

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public int getRecommendedDisplayCount() {
        return recommendedDisplayCount;
    }

    public void setRecommendedDisplayCount(int recommendedDisplayCount) {
        this.recommendedDisplayCount = recommendedDisplayCount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getCountContent() {
        return countContent;
    }

    public void setCountContent(int countContent) {
        this.countContent = countContent;
    }

    public int getViewMode() {
        return viewMode;
    }

    public void setViewMode(int viewMode) {
        this.viewMode = viewMode;
    }

    public String getName() {
        return name;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(boardNo);
        dest.writeInt(modUserNo);
        dest.writeString(modDate);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(folderNo);
        dest.writeInt(displayTypeNo);
        dest.writeInt(dortNo);
        dest.writeByte((byte) (isReply ? 1 : 0));
        dest.writeByte((byte) (isHead ? 1 : 0));
        dest.writeByte((byte) (isNotice ? 1 : 0));
        dest.writeByte((byte) (isRecommend ? 1 : 0));
        dest.writeInt(recommendedDisplayCount);
        dest.writeByte((byte) (enabled ? 1 : 0));
        dest.writeInt(countContent);
        dest.writeInt(viewMode);
        dest.writeInt(group);
        dest.writeString(nameGroup);
    }
}
