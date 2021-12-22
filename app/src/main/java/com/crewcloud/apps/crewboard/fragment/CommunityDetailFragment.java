package com.crewcloud.apps.crewboard.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.adapter.CommentAdapter;
import com.crewcloud.apps.crewboard.adapter.PhotoAdapter;
import com.crewcloud.apps.crewboard.base.BaseDialog;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dialog.EditCommentDialog;
import com.crewcloud.apps.crewboard.dialog.InfoCommentDialog;
import com.crewcloud.apps.crewboard.dialog.TitleEffectDialog;
import com.crewcloud.apps.crewboard.dtos.Attachments;
import com.crewcloud.apps.crewboard.dtos.Comment;
import com.crewcloud.apps.crewboard.dtos.CommunityDetail;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.module.communitydetail.CommunityDetailPresenter;
import com.crewcloud.apps.crewboard.module.communitydetail.CommunityDetailPresenterImp;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.crewcloud.apps.crewboard.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * Created by dazone on 2/21/2017.
 */

public class CommunityDetailFragment extends BaseFragment implements CommunityDetailPresenter.view {


    @Bind(R.id.fragment_community_detail_et_comment)
    EditText etComment;

    @Bind(R.id.fragment_community_detail_iv_send)
    ImageView ivSend;

    @Bind(R.id.fragment_community_detail_lv_attach)
    RecyclerView lvAttach;

    @Bind(R.id.fragment_community_detail_tv_author)
    TextView tvAuthor;

    @Bind(R.id.fragment_community_detail_tv_des)
    WebView tvDes;

    @Bind(R.id.fragment_community_detail_tv_time_upload)
    TextView tvTime;

    @Bind(R.id.fragment_community_detail_tv_title)
    TextView tvTitle;

    @Bind(R.id.fragment_community_iv_avatar)
    ImageView ivAvatar;

    @Bind(R.id.list_comment)
    RecyclerView listComment;

    CommentAdapter commentAdapter;
    AttachmentAdapter attachmentAdapter;

    CommunityDetailPresenterImp communityDetailPresenterImp;
    private int idCommunity;

    CommunityDetail communityDetail;
    PhotoAdapter attachAdapter;
    InfoCommentDialog infoCommentDialog;

    public static BaseFragment newInstance(Bundle bundle) {
        CommunityDetailFragment fragment = new CommunityDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.detail));
        setActionFloat(true);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            idCommunity = getArguments().getInt(Statics.ID_COMMUNITY);
        }
        attachmentAdapter = new AttachmentAdapter(getBaseActivity());
        commentAdapter = new CommentAdapter(getBaseActivity());
        communityDetailPresenterImp = new CommunityDetailPresenterImp(getBaseActivity());


    }


    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_community_detail, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail_community, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_community_delete:
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity())
                        .setMessage("Are you sure delete this community?")
                        .setPositiveButton(Util.getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                communityDetailPresenterImp.deleteCommunity(communityDetail.getContentNo());
                            }
                        })
                        .setNegativeButton(Util.getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;
            case R.id.menu_community_more:
                TitleEffectDialog dialog = new TitleEffectDialog(getContext());
                dialog.show();
                break;
            case R.id.menu_community_detail_edit:
                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.EDIT_COMMUNITY);
                MenuEvent menuEvent = new MenuEvent();
                Bundle data = new Bundle();
                data.putSerializable("DETAIL", communityDetail);
                menuEvent.setBundle(data);
                baseEvent.setMenuEvent(menuEvent);
                getEventBus().post(baseEvent);

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        communityDetailPresenterImp.attachView(this);
        attachAdapter = new PhotoAdapter(getBaseActivity());
        lvAttach.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listComment.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        communityDetailPresenterImp.getCommunityDetail(idCommunity);

    }

    @OnClick(R.id.fragment_community_detail_iv_send)
    public void onClickSend() {
        if (!TextUtils.isEmpty(etComment.getText().toString())) {
            if (communityDetail != null) {
                communityDetailPresenterImp.sentComment(idCommunity, communityDetail.getGroupNo(), communityDetail.getOrderNo(), communityDetail.getDepth(), etComment.getText().toString());
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onGetDetailSuccess(final CommunityDetail communityDetail, String avatar, String email, String attachment, boolean isFromCached) {

        showData(communityDetail, avatar, email, attachment);
        Prefs preferenceUtilities = CrewBoardApplication.getInstance().getmPrefs();
        if (preferenceUtilities.getUserNo() != communityDetail.getModUserNo()) {
            setHasOptionsMenu(false);
        }

        communityDetailPresenterImp.getComment(communityDetail.getContentNo());

        if (!isFromCached) {
            getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                }
            });
        }
    }

    @Override
    public void onGetCommentDetailSuccess(List<Comment> comments, boolean isFromCached) {
        listComment.setAdapter(commentAdapter);
        commentAdapter.clear();
        commentAdapter.addAll(comments);
        commentAdapter.setOnClickLitener(new CommentAdapter.onClickItemListener() {
            @Override
            public void onLongClickItem(final Comment comment) {
                infoCommentDialog = new InfoCommentDialog(getActivity());
                infoCommentDialog.setRegUserNo(comment.getModUserNo());
                final int replyNo = comment.getReplyNo();
                infoCommentDialog.setOnclickListener(new InfoCommentDialog.onClickInfoListener() {
                    @Override
                    public void deleteListerner() {
                        infoCommentDialog.dismiss();
                        communityDetailPresenterImp.deleteComment(comment.getContentNo(), replyNo);
                    }

                    @Override
                    public void editListener() {
                        infoCommentDialog.dismiss();
                        EditCommentDialog dialog = new EditCommentDialog(getActivity());
                        dialog.setComment(comment.getContent());
                        dialog.setOnCloseDialogListener(new BaseDialog.OnCloseDialog() {
                            @Override
                            public void onPositive(String newComment) {
                                communityDetailPresenterImp.editComment(newComment, replyNo);
                            }

                            @Override
                            public void onNegative() {

                            }

                            @Override
                            public void onClose() {

                            }
                        });
                        dialog.show();
                    }

                    @Override
                    public void copyListener() {
                        ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(comment.getContent());
                        infoCommentDialog.dismiss();
                    }
                });
                infoCommentDialog.show();

            }

            @Override
            public void onClickItem(Comment comment) {

            }
        });

    }

    @Override
    public void onSentCommentSuccess(final int commentNo) {
        Util.hideKeyboard(getBaseActivity());
        etComment.setText("");


        communityDetailPresenterImp.getComment(communityDetail.getContentNo());
    }

    @Override
    public void onDeleteCommentSuccess() {
        communityDetailPresenterImp.getComment(communityDetail.getContentNo());

    }

    @Override
    public void onDeleteCommunitySuccess() {
        getBaseActivity().changeFragment(new CommunityFragment(), false, CommunityFragment.class.getSimpleName());
    }

    private void showData(final CommunityDetail communityDetail, String avatar, String email, String attachment) {

        Type listType = new TypeToken<List<Attachments>>() {
        }.getType();
        List<Attachments> attachmentses = new Gson().fromJson(attachment, listType);
        communityDetail.setAttachmentses(attachmentses);
        this.communityDetail = communityDetail;

        if (!TextUtils.isEmpty(communityDetail.getTitle())) {
            tvTitle.setText(Html.fromHtml(communityDetail.getTitle()));
        } else {
            tvTitle.setText("");
        }
//        Picasso.with(getContext()).load(BuildConfig.BASE_URL + CrewBoardApplication.getInstance().getmPrefs()
//                .getAvatar())
//                .placeholder(R.mipmap.photo_placeholder)
//                .error(R.mipmap.photo_placeholder)
//                .into(ivAvatar);
//
        if (!TextUtils.isEmpty(avatar)) {
            Picasso.with(getContext()).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite()+ avatar)
                    .placeholder(R.mipmap.photo_placeholder)
                    .error(R.mipmap.avatar_default)
                    .into(ivAvatar);

        } else {
            Picasso.with(getContext()).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + Statics.IMAGE_DEAULT)
                    .placeholder(R.mipmap.photo_placeholder)
                    .error(R.mipmap.avatar_default)
                    .into(ivAvatar);
        }


        tvTime.setText(TimeUtils.getTime(communityDetail.getRegDate()));
        attachmentAdapter.clear();
        lvAttach.setAdapter(attachmentAdapter);
        attachmentAdapter.addAll(attachmentses);
        tvAuthor.setText(communityDetail.getModUserName() + " < " + communityDetail.getModDepartName() + "/" + communityDetail.getModPositionName() + " > ");
        tvDes.setWebViewClient(new WebViewClient());
        tvDes.getSettings().setJavaScriptEnabled(true);
        tvDes.getSettings().setBuiltInZoomControls(true);
        tvDes.getSettings().setLoadWithOverviewMode(true);
        tvDes.setPadding(0, 0, 10, 0);

        String htmlString = communityDetail.getContent();
        String summary = "<html><body>" + htmlString + "</body></html>";
        tvDes.loadData(summary, "text/html; charset=utf-8", "utf-8");

    }

    @Override
    public void onError(String message) {
        Toast.makeText(getBaseActivity(), message, Toast.LENGTH_LONG).show();
    }

}
