package com.crewcloud.apps.crewboard.event;

/**
 * Created by mb on 3/30/16.
 */
public class MenuItem {
    private int id;
    private String name = "";

    private boolean isSelected;

    public MenuItem(int id) {
        this.id = id;
    }

    public MenuItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
