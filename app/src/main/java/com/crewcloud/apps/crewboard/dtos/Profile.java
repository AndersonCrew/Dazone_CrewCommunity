package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dazone on 5/9/2017.
 */

public class Profile {
    @SerializedName("UserNo")
    public String UserNo;
    @SerializedName("UserID")
    public String UserId = "";
    @SerializedName("Name_Default")
    public String NameDefault = "";
    @SerializedName("Name_EN")
    public String NameEn = "";
    @SerializedName("Name")
    public String name = "";
    @SerializedName("MailAddress")
    public String mailAddress = "";
    @SerializedName("CellPhone")
    public String cellPhone = "";
    @SerializedName("CompanyPhone")
    private String CompanyPhone;
    @SerializedName("ExtensionNumber")
    private String ExtensionNumber;
    @SerializedName("EntranceDate")
    private String EntranceDate;
    @SerializedName("BirthDate")
    private String BirthDate;
    @SerializedName("AvatarUrl")
    public String avatar = "";

    @SerializedName("CompanyId")
    public String CompanyId = "";

//    @SerializedName("Belongs")
//    public List<Belong> belongs;

    @SerializedName("DepartName")
    public String departName = "";
    @SerializedName("PositionName")
    public String positionName = "";
    @SerializedName("Password")
    public String password = "";
    @SerializedName("Belongs")
    public ArrayList<BelongDepartmentDTO> Belongs = new ArrayList<>();

    public String positionDepartName = "";

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getNameDefault() {
        return NameDefault;
    }

    public void setNameDefault(String nameDefault) {
        NameDefault = nameDefault;
    }

    public String getNameEn() {
        return NameEn;
    }

    public void setNameEn(String nameEn) {
        NameEn = nameEn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public List<BelongDepartmentDTO> getBelongs() {
        return Belongs;
    }

    public void setBelongs(ArrayList<BelongDepartmentDTO> belongs) {
        this.Belongs = belongs;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPositionDepartName() {
        return positionDepartName;
    }

    public void setPositionDepartName(String positionDepartName) {
        this.positionDepartName = positionDepartName;
    }

    public String getCompanyPhone() {
        return CompanyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        CompanyPhone = companyPhone;
    }

    public String getExtensionNumber() {
        return ExtensionNumber;
    }

    public void setExtensionNumber(String extensionNumber) {
        ExtensionNumber = extensionNumber;
    }

    public void setEntranceDate(String entranceDate) {
        EntranceDate = entranceDate;
    }

    public String getEntranceDate() {
        return EntranceDate;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }
}
