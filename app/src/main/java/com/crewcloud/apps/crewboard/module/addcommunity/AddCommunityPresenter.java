package com.crewcloud.apps.crewboard.module.addcommunity;

import android.net.Uri;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;

import java.util.List;

/**
 * Created by dazone on 2/28/2017.
 */

public interface AddCommunityPresenter {
    interface view extends BaseView {
        void onAddSuccess();

        void onGetSuccess(List<ChildFolders> childFolderses);

        void onValiDateSuccess(String title, String content);

        void onError(String message);
    }

    interface presenter {
        void addCommunity(String title, String content, List<String> attactments, int boardno, boolean alarm);

        void validate(String title, String content, int broadNo);

        void getMenuLeft();

        void uploadFile(Uri uri);

    }
}
