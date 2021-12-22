package com.crewcloud.apps.crewboard.module.login;

import com.crewcloud.apps.crewboard.base.BaseView;

/**
 * Created by dazone on 3/8/2017.
 */

public interface LoginPresenter {
    interface view extends BaseView {
        void onLoginSuccess();


        void onError(String message);
    }

    interface  presenter {
        void validateData(String server, String personal,String pass);

        void login(String server, String personal, String pass);
    }
}
