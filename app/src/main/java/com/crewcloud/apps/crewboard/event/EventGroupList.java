package com.crewcloud.apps.crewboard.event;

import com.crewcloud.apps.crewboard.dtos.ChildBoards;

/**
 * Created by dazone on 3/7/2017.
 */

public class EventGroupList {
    private ChildBoards childBoards;

    public EventGroupList(ChildBoards childBoards) {
        this.childBoards = childBoards;
    }

    public ChildBoards getChildBoards() {
        return childBoards;
    }

    public void setChildBoards(ChildBoards childBoards) {
        this.childBoards = childBoards;
    }
}
