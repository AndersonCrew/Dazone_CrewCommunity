package com.crewcloud.apps.crewboard.net;


import com.crewcloud.apps.crewboard.base.BaseResponse;
import com.crewcloud.apps.crewboard.base.MenuResponse;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.dtos.request.BodyRequest;
import com.crewcloud.apps.crewboard.dtos.request.ChangePassRequest;
import com.crewcloud.apps.crewboard.dtos.request.CommentRequest;
import com.crewcloud.apps.crewboard.dtos.request.CommunityRequest;
import com.crewcloud.apps.crewboard.dtos.request.DeleteCommentRequest;
import com.crewcloud.apps.crewboard.dtos.request.DeleteCommunityRequest;
import com.crewcloud.apps.crewboard.dtos.request.EditRequest;
import com.crewcloud.apps.crewboard.dtos.request.ProfileRequest;
import com.crewcloud.apps.crewboard.dtos.request.SearchRequest;
import com.crewcloud.apps.crewboard.dtos.request.SentCommentRequest;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by mb on 3/30/16.
 */
public interface MyApi {

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetMenu")
    Observable<BaseResponse<MenuResponse<String>>> getLeftMenu(@Body BodyRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetAllArticleList")
    Observable<BaseResponse<MenuResponse<String>>> getCommunity(@Body BodyRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetArticleList")
    Observable<BaseResponse<MenuResponse<String>>> getCommunityById(@Body BodyRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/SearchContents")
    Observable<BaseResponse<MenuResponse<String>>> searchCommunity(@Body SearchRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetContent")
    Observable<BaseResponse<MenuResponse<String>>> getCommunityDetail(@Body BodyRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetComment")
    Observable<BaseResponse<MenuResponse<String>>> getCommentDetail(@Body CommentRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/GetViewLog")
    Observable<BaseResponse<MenuResponse<String>>> getUserViewCommunity(@Body CommentRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/SaveComment")
    Observable<BaseResponse<MenuResponse<Integer>>> sentComment(@Body SentCommentRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/DeleteComment")
    Observable<BaseResponse<MenuResponse<Boolean>>> deleteComment(@Body DeleteCommentRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/EditComment")
    Observable<BaseResponse<MenuResponse<Boolean>>> editComment(@Body EditRequest bodyRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/AddContent")
    Observable<BaseResponse<MenuResponse<String>>> addCommunity(@Body CommunityRequest communityRequest);

    @POST("/UI/mobileBoard/Handlers/MobileService.asmx/DeleteCotent")
    Observable<BaseResponse<MenuResponse<Boolean>>> deleteCotent(@Body DeleteCommunityRequest bodyRequest);

    @POST("/UI/WebService/WebServiceCenter.asmx/Logout_v2")
    Observable<BaseResponse<MenuResponse<String>>> logout(@Body BodyRequest bodyRequest);

    @POST("/UI/mobileBoard/FileUpload.aspx")
    Observable<BaseResponse<MenuResponse<String>>> uploadFile(@Body BodyRequest bodyRequest);

    @POST("/UI/WebService/WebServiceCenter.asmx/UpdatePassword")
    Observable<BaseResponse<MenuResponse<Boolean>>> changePass(@Body ChangePassRequest bodyRequest);

    @POST("/UI/MobileBoard/Handlers/MobileService.asmx/GetProfileUser")
    Observable<BaseResponse<MenuResponse<Profile>>> getUser(@Body ProfileRequest bodyRequest);

    @FormUrlEncoded
    @POST("/UI/WebService/WebServiceCenter.asmx/Login_v2")
    Observable<BaseResponse<MenuResponse<String>>> login(@FieldMap Map<String, String> params);
}