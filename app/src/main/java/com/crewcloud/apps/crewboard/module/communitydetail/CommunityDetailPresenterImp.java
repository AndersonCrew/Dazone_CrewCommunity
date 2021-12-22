package com.crewcloud.apps.crewboard.module.communitydetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BasePresenter;
import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.base.ResponseListener;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.Comment;
import com.crewcloud.apps.crewboard.dtos.request.CommentRequest;
import com.crewcloud.apps.crewboard.dtos.CommunityDetail;
import com.crewcloud.apps.crewboard.dtos.ErrorDto;
import com.crewcloud.apps.crewboard.dtos.request.DeleteCommentRequest;
import com.crewcloud.apps.crewboard.dtos.request.DeleteCommunityRequest;
import com.crewcloud.apps.crewboard.dtos.request.EditRequest;
import com.crewcloud.apps.crewboard.dtos.request.SentCommentRequest;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dazone on 2/23/2017.
 */

public class CommunityDetailPresenterImp extends BasePresenter<CommunityDetailPresenter.view> implements CommunityDetailPresenter.presenter {


    public CommunityDetailPresenterImp(BaseActivity activity) {
        super(activity);
    }

    @Override
    public void getCommunityDetail(int contentNo) {

        activity.showProgressDialog();

        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String language = Util.getPhoneLanguage();

        BodyRequest bodyRequest = new BodyRequest(sessionId);
        bodyRequest.setLanguageCode(language);
        bodyRequest.setContentNo(contentNo);

        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().getCommunityDetail(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                    activity.dismissProgressDialog();
                    CommunityDetail data = new Gson().fromJson(result.getData().getList(), CommunityDetail.class);
                    getView().onGetDetailSuccess(data,result.getData().getAvatar(),result.getData().getEmail(),result.getData().getAttachments(), isFromCache());

                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    activity.dismissProgressDialog();
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }

    @Override
    public void getComment(int contentNo) {

        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();
        String language = Util.getPhoneLanguage();

        CommentRequest bodyRequest = new CommentRequest(sessionId);
        bodyRequest.setLanguageCode(language);
        bodyRequest.setContentno(contentNo);
        bodyRequest.setRole("");

        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().getCommentDetail(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<String>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<String>> result) {
                    Comment[] data = new Gson().fromJson(result.getData().getList(), Comment[].class);
                    List<Comment> comments = new ArrayList<>();
                    for (int index = 0; index < data.length; index++) {
                        comments.add(data[index]);
                    }
                    getView().onGetCommentDetailSuccess(comments, isFromCache());
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }

    @Override
    public void sentComment(int contentNo, int groupNo, int orderNo, int depth, String comment) {

        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();

        SentCommentRequest sentCommentRequest = new SentCommentRequest(sessionId);
        sentCommentRequest.setContentno(contentNo);
        sentCommentRequest.setComment(comment);
        sentCommentRequest.setDepth(depth);
        sentCommentRequest.setOrderNo(orderNo);
        sentCommentRequest.setGroupno(groupNo);

        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().sentComment(sentCommentRequest), new ResponseListener<BaseResponse<MenuResponse<Integer>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<Integer>> result) {
                    Log.d("SUCCESS", result.getData().getErrorDto().getMessage());
                    getView().onSentCommentSuccess(result.getData().getList());
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }

    @Override
    public void deleteComment(int contentno, int replyNo) {
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();

        DeleteCommentRequest sentCommentRequest = new DeleteCommentRequest(sessionId);
        sentCommentRequest.setContentno(contentno);
        sentCommentRequest.setReplyNo(replyNo);

        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().deleteComment(sentCommentRequest), new ResponseListener<BaseResponse<MenuResponse<Boolean>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<Boolean>> result) {
                    Log.d("SUCCESS", result.getData().getErrorDto().getMessage());
                    getView().onDeleteCommentSuccess();
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }

    @Override
    public void editComment(String newComment, int replyNo) {
        String sessionId = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId();

        EditRequest commentRequest = new EditRequest(sessionId);
        commentRequest.setReplyNo(replyNo);
        commentRequest.setComment(newComment);
        commentRequest.setLanguageCode(Util.getPhoneLanguage());
        commentRequest.setRole("");


        if (isViewAttached()) {
            activity.requestAPI(activity.getApi().editComment(commentRequest), new ResponseListener<BaseResponse<MenuResponse<Boolean>>>() {
                @Override
                public void onSuccess(BaseResponse<MenuResponse<Boolean>> result) {
                    Log.d("SUCCESS", result.getData().getErrorDto().getMessage());
                    getView().onDeleteCommentSuccess();
                }

                @Override
                public void onError(@NonNull ErrorDto messageResponse) {
                    getView().onError(messageResponse.getMessage());

                }
            });
        }
    }

    @Override
    public void deleteCommunity(int contentNo) {
        String sessionId = new CrewBoardApplication().getPreferenceUtilities().getCurrentMobileSessionId();
        DeleteCommunityRequest bodyRequest = new DeleteCommunityRequest(sessionId);
        bodyRequest.setContentno(contentNo);
        activity.requestAPI(activity.getApi().deleteCotent(bodyRequest), new ResponseListener<BaseResponse<MenuResponse<Boolean>>>() {
            @Override
            public void onSuccess(BaseResponse<MenuResponse<Boolean>> result) {
                Log.d("DELETE COMMUNITY","SUCCESS");
                getView().onDeleteCommunitySuccess();
            }

            @Override
            public void onError(@NonNull ErrorDto messageResponse) {
                Log.d("DELETE COMMUNITY","ERROR");
                getView().onError(messageResponse.getMessage());
            }
        });
    }
}
