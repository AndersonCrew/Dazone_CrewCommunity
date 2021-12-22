package com.crewcloud.apps.crewboard.dtos;

/**
 * Created by dazone on 2/27/2017.
 */

public class Jobs {
    private String name;

    public Jobs() {
    }

    public Jobs(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
