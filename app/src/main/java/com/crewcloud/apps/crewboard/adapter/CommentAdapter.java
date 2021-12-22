package com.crewcloud.apps.crewboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.Comment;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmList;

import static android.content.ContentValues.TAG;


/**
 * Created by dazone on 2/23/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private BaseActivity mActivity;
    private RealmList<Comment> lstComment = new RealmList<>();
    public CommentAdapter(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }
    public void addAll(List<Comment> comments) {
        int curr = getItemCount();
        lstComment.addAll(comments);
        notifyItemRangeInserted(curr, getItemCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CommentAdapter.ViewHolder) holder).bind(position);

    }

    @Override
    public int getItemCount() {
        if (lstComment == null) {
            return 0;
        }
        return lstComment.size();
    }

    public void clear() {
        lstComment.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_comment_civ_avatar)
        ImageView ivAvatar;

        @Bind(R.id.item_comment_iv_info)
        ImageView ivInfo;

        @Bind(R.id.item_comment_tv_author)
        TextView tvCommentAuthor;

        @Bind(R.id.item_comment_tv_time)
        TextView tvCommentTime;

        @Bind(R.id.item_comment_tv_count_comment)
        TextView tvCountComment;

        @Bind(R.id.item_comment_tv_reply)
        TextView tvReply;

        @Bind(R.id.item_comment_tv_content)
        ExpandableTextView tvContent;

        public ViewHolder(View view) {
            super(view);
            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
            tvContent.setAnimationDuration(1000L);

            // set interpolators for both expanding and collapsing animations
            tvContent.setInterpolator(new OvershootInterpolator());

            // or set them separately
            tvContent.setExpandInterpolator(new OvershootInterpolator());
            tvContent.setCollapseInterpolator(new OvershootInterpolator());

        }

        void bind(int position) {
            final Comment comment = lstComment.get(position);
            if (TextUtils.isEmpty(comment.getAvatar())) {
                Picasso.with(mActivity).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite()+ Statics.IMAGE_DEAULT)
                        .placeholder(R.mipmap.photo_placeholder)
                        .error(R.mipmap.avatar_default)
                        .into(ivAvatar);
            } else {
                Picasso.with(mActivity).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + comment.getAvatar())
                        .placeholder(R.mipmap.photo_placeholder)
                        .error(R.mipmap.avatar_default)
                        .into(ivAvatar);
            }

            tvCommentAuthor.setText(comment.getModUserName());
            long time = TimeUtils.getTimeFromString(comment.getRegDate());
            long timeCreated = TimeUtils.getTimeForMail(time);
            if (timeCreated == -2) {
                //today,hh:mm aa
                tvCommentTime.setText(mActivity.getString(R.string.today) + TimeUtils.displayTimeWithoutOffset(comment.getRegDate(), true));
            } else {
                //YYY-MM-DD hh:mm aa
                tvCommentTime.setText(TimeUtils.displayTimeWithoutOffset(comment.getRegDate(), false));
            }
            tvContent.setText(comment.getContent());
//            if (comment.getLstReply() != null && comment.getLstReply().size() > 0) {
////                tvCountComment.setText("View More " + noticeDetail.getCommentList().size() + " Comment");
//                tvCountComment.setText(String.valueOf(comment.getLstReply().size()));
//
//            } else {
//                tvReply.setVisibility(View.GONE);
//                tvCountComment.setVisibility(View.GONE);
//            }

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        listener.onLongClickItem(comment);
                    }
                    return false;
                }
            });

            if (Util.countLines(comment.getContent()) > 3) {
                ivInfo.setVisibility(View.VISIBLE);
            } else {
                ivInfo.setVisibility(View.GONE);
            }
            ivInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tvContent.isExpanded()) {
                        tvContent.collapse();
                        ivInfo.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_chevron_up_grey600_24dp));
                    } else {
                        ivInfo.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_chevron_down_grey600_24dp));
                        tvContent.expand();
                    }
                }
            });
            tvContent.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
                @Override
                public void onExpand(ExpandableTextView view) {
                    Log.d(TAG, "ExpandableTextView expanded");
                }

                @Override
                public void onCollapse(ExpandableTextView view) {
                    Log.d(TAG, "ExpandableTextView collapsed");
                }
            });
        }

    }


    public interface onClickItemListener {
        void onLongClickItem(Comment comment);

        void onClickItem(Comment comment);
    }

    private onClickItemListener listener;

    public void setOnClickLitener(onClickItemListener litener) {
        this.listener = litener;
    }


}
