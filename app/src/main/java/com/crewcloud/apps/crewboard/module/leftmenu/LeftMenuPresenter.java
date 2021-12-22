package com.crewcloud.apps.crewboard.module.leftmenu;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.dtos.LeftMenu;

import java.util.List;

/**
 * Created by dazone on 2/21/2017.
 */

public interface LeftMenuPresenter {
    interface view extends BaseView {
        void onGetLeftMenuSuccess(List<ChildFolders> leftMenus, boolean isCached);

        void onError(String message);
    }

    interface presenter {
        void getLeftMenu();
    }
}
