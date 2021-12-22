package com.crewcloud.apps.crewboard.module.userview;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.UserViewCommunity;

import java.util.List;

/**
 * Created by dazone on 3/13/2017.
 */

public interface UserViewPresenter {
    interface view extends BaseView {
        void onGetUserViewSuccess(List<UserViewCommunity> list);

        void onError(String message);
    }

    interface presenter {
        void getUserView(int contentno);
    }
}
