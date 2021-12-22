package com.crewcloud.apps.crewboard.module.profile;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.Profile;

/**
 * Created by dazone on 2/28/2017.
 */

public interface LogoutPresenter {
    interface view extends BaseView {
        void onLogoutSuccess();

        void onGetProfileSuccess(Profile profile);

        void onError(String message);
    }

    interface presenter {
        void logout(String sessionId);

        void getProfile();
    }
}
