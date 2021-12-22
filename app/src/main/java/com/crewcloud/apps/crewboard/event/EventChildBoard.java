package com.crewcloud.apps.crewboard.event;

import com.crewcloud.apps.crewboard.dtos.ChildBoards;

/**
 * Created by dazone on 3/3/2017.
 */

public class EventChildBoard {
    private ChildBoards childBoards;

    public EventChildBoard(ChildBoards childBoards) {
        this.childBoards = childBoards;
    }

    public ChildBoards getChildBoards() {
        return childBoards;
    }

    public void setChildBoards(ChildBoards childBoards) {
        this.childBoards = childBoards;
    }
}
