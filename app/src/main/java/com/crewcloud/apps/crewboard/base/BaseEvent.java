package com.crewcloud.apps.crewboard.base;


import com.crewcloud.apps.crewboard.event.MenuEvent;

/**
 * Created by mb on 3/26/16
 */
public class BaseEvent {
    private boolean isLock;
    public BaseEvent() {

    }

    public abstract class EventType {
        public static final int MENU = 1;
        public static final int SETTING = 2;
        public static final int COMMUNITY_DETAIL = 3;
        public static final int LIST_USER_VIEW = 4;
        public static final int LOCK= 7;
        public static final int ADD_COMMUNITY = 8;
        public static final int EDIT_COMMUNITY = 5;
        public static final int LOGOUT = 9;
        public static final int CHANGE_PASS = 10;
    }

    private int type;
    private MenuEvent menuEvent;

    public BaseEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public MenuEvent getMenuEvent() {
        return menuEvent;
    }

    public void setMenuEvent(MenuEvent menuEvent) {
        this.menuEvent = menuEvent;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }
}
