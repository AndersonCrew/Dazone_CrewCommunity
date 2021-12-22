package com.crewcloud.apps.crewboard.module.communitydetail;

import com.crewcloud.apps.crewboard.base.BaseView;
import com.crewcloud.apps.crewboard.dtos.Comment;
import com.crewcloud.apps.crewboard.dtos.CommunityDetail;

import java.util.List;

/**
 * Created by dazone on 2/23/2017.
 */

public interface CommunityDetailPresenter {
    interface view extends BaseView {
        void onGetDetailSuccess(CommunityDetail communityDetail,String avatar, String email,String attachment, boolean isFromCached);

        void onGetCommentDetailSuccess(List<Comment> comment, boolean isFromCached);

        void onSentCommentSuccess(int commentNo);

        void onDeleteCommentSuccess();

        void onDeleteCommunitySuccess();

        void onError(String message);
    }

    interface presenter {

        void getCommunityDetail(int contentNo);

        void getComment(int contentNo);

        void sentComment(int contentNo, int groupNo, int orderNo, int depth, String comment);

        void deleteComment(int contentno, int replyNo);

        void editComment(String newComment,int replyNo);

        void deleteCommunity(int contentNo);

    }

}
