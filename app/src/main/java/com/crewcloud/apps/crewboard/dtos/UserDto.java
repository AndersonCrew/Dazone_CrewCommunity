package com.crewcloud.apps.crewboard.dtos;

import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Prefs;
import java.util.ArrayList;

public class UserDto {

    public int Id;

    public int CompanyNo;

    public int PermissionType;//0 normal, 1 admin

    public String userID;

    public String FullName = "";

    public String MailAddress = "";

    public String session;

    public String avatar;

    public String NameCompany = "";

    public String Name = "";

    public ArrayList<CompanyDto> informationcompany;

    public Prefs prefs = CrewBoardApplication.getInstance().getmPrefs();

    public UserDto() {
        prefs = CrewBoardApplication.getInstance().getmPrefs();
    }
}
