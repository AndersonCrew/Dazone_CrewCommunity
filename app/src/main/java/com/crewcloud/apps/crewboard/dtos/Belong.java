package com.crewcloud.apps.crewboard.dtos;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dazone on 5/9/2017.
 */

public class Belong {
    @SerializedName("IsDefault")
    public boolean isDefault;

    @SerializedName("DepartName")
    public String departName;

    @SerializedName("PositionName")
    public String positionName;

    @SerializedName("DutyName")
    public String dutyName;
}
