package com.crewcloud.apps.crewboard.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by dazone on 3/3/2017.
 */

public class ChildFolders implements Parcelable {

    @SerializedName("FolderNo")
    private int folderNo;

    @SerializedName("ModUserNo")
    private int modUserNo;

    @SerializedName("ModDate")
    private String modDate;

    @SerializedName("Name")
    private String name;

    @SerializedName("ParentNo")
    private int ParentNo;

    @SerializedName("SortNo")
    private int sortNo;

    @SerializedName("Enabled")
    private boolean Enabled;

    @SerializedName("ChildFolders")
    private List<ChildFolders> lstChildFolders;

    @SerializedName("ChildBoards")
    private List<ChildBoards> lstChildBoards;

    public ChildFolders() {
    }


    protected ChildFolders(Parcel in) {
        folderNo = in.readInt();
        modUserNo = in.readInt();
        modDate = in.readString();
        name = in.readString();
        ParentNo = in.readInt();
        sortNo = in.readInt();
        Enabled = in.readByte() != 0;
        lstChildFolders = in.createTypedArrayList(ChildFolders.CREATOR);
        lstChildBoards = in.createTypedArrayList(ChildBoards.CREATOR);
    }

    public static final Creator<ChildFolders> CREATOR = new Creator<ChildFolders>() {
        @Override
        public ChildFolders createFromParcel(Parcel in) {
            return new ChildFolders(in);
        }

        @Override
        public ChildFolders[] newArray(int size) {
            return new ChildFolders[size];
        }
    };

    public List<ChildFolders> getLstChildFolders() {
        return lstChildFolders;
    }

    public void setLstChildFolders(List<ChildFolders> lstChildFolders) {
        this.lstChildFolders = lstChildFolders;
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    public int getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(int folderNo) {
        this.folderNo = folderNo;
    }

    public List<ChildBoards> getLstChildBoards() {
        return lstChildBoards;
    }

    public void setLstChildBoards(List<ChildBoards> lstChildBoards) {
        this.lstChildBoards = lstChildBoards;
    }


    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public int getModUserNo() {
        return modUserNo;
    }

    public void setModUserNo(int modUserNo) {
        this.modUserNo = modUserNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentNo() {
        return ParentNo;
    }

    public void setParentNo(int parentNo) {
        ParentNo = parentNo;
    }

    public int getSortNo() {
        return sortNo;
    }

    public void setSortNo(int sortNo) {
        this.sortNo = sortNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(folderNo);
        dest.writeInt(modUserNo);
        dest.writeString(modDate);
        dest.writeString(name);
        dest.writeInt(ParentNo);
        dest.writeInt(sortNo);
        dest.writeByte((byte) (Enabled ? 1 : 0));
        dest.writeTypedList(lstChildFolders);
        dest.writeTypedList(lstChildBoards);
    }
}
