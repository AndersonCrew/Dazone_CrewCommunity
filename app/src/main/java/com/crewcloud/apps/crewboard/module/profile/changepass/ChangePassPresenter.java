package com.crewcloud.apps.crewboard.module.profile.changepass;

import com.crewcloud.apps.crewboard.base.BaseView;

/**
 * Created by dazone on 5/11/2017.
 */

public interface ChangePassPresenter {
    interface view extends BaseView {

        void ChangePassSuccess();

        void ChangePassError(String message);
    }

    interface presenter {
        void ChangePass(String oldPass, String newPass);

        void CheckPass(String oldPass, String newPass, String confirmPass);
    }
}
