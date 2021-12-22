package com.crewcloud.apps.crewboard.dtos;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by tunglam on 12/26/16.
 */

public class Attachments extends RealmObject implements Serializable {
    @SerializedName("FileNo")
    private int fileNo;

    @SerializedName("ContentNo")
    private int contentNo;

    @SerializedName("Name")
    private String name;

    @SerializedName("Size")
    private long size;

    public Attachments() {
    }

    public int getFileNo() {
        return fileNo;
    }

    public void setFileNo(int fileNo) {
        this.fileNo = fileNo;
    }

    public int getContentNo() {
        return contentNo;
    }

    public void setContentNo(int contentNo) {
        this.contentNo = contentNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
