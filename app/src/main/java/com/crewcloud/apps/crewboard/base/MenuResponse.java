package com.crewcloud.apps.crewboard.base;

import com.crewcloud.apps.crewboard.dtos.Attachments;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tunglam on 12/23/16.
 */

public class MenuResponse<D> {

    @SerializedName("success")
    private int success;

    @SerializedName("data")
    private D list;

    @SerializedName("Email")
    private String email;

    @SerializedName("Avatar")
    private String avatar;


    @SerializedName("error")
    private ErrorDto errorDto;

    @SerializedName("Attactment")
    private String attachments;

    public MenuResponse() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public D getList() {
        return list;
    }

    public void setList(D list) {
        this.list = list;
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }

    public void setErrorDto(ErrorDto errorDto) {
        this.errorDto = errorDto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
