package com.crewcloud.apps.crewboard.module.community;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.Community;

import java.util.List;

/**
 * Created by dazone on 2/22/2017.
 */

public interface CommunityPresenter {
    interface view extends BaseView{
        void onSuccess(List<Community> lstCommunity);

        void onError(String message);
    }

    interface presenter {
        void getCommunity(int curentPage);

        void searchCommunity(String textSearch,int curentPage);

        void getCommunityById(int id,int curentPage);
    }
}
