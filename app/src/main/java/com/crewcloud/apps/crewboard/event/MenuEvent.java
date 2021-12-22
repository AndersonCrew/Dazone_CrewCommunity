package com.crewcloud.apps.crewboard.event;

import android.os.Bundle;

import com.crewcloud.apps.crewboard.base.BaseEvent;


/**
 * Created by tunglam on 12/15/16.
 */

public class MenuEvent extends BaseEvent {
    private MenuItem menuItem;

    private Bundle bundle;


    public MenuEvent() {
        super();

    }

    public MenuEvent(int type) {
        super(type);
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}

