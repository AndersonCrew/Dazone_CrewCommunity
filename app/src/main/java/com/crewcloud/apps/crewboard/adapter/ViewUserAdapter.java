package com.crewcloud.apps.crewboard.adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.UserViewCommunity;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.crewcloud.apps.crewboard.widget.AutoSizeTextView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dazone on 1/9/2017.
 */

public class ViewUserAdapter extends BaseAdapter<UserViewCommunity, ViewUserAdapter.ViewHolder> {


    public ViewUserAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_view, parent, false);
        return new ViewUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_view_user_iv_avatar)
        CircleImageView ivAvatar;

        @Bind(R.id.item_view_user_rl_check)
        RelativeLayout rlCheck;

        @Bind(R.id.item_view_user_tv_name)
        AutoSizeTextView tvName;

        @Bind(R.id.item_view_user_tv_position)
        AutoSizeTextView tvPosition;

        @Bind(R.id.item_view_user_tv_time)
        TextView tvTime;

        @Bind(R.id.ln_content)
        RelativeLayout lnContent;


        boolean isCheck = false;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int position) {
            final UserViewCommunity userViewCommunity = getItem(position);
            tvName.setText(userViewCommunity.getUserName());
            long time = TimeUtils.getTimeFromString(userViewCommunity.getViewedDate());
            long today = TimeUtils.getTimeForMail(time);

            itemView.setActivated(userViewCommunity.ismIsSelected());

            if (today == -2) {
                //today,hh:mm aa
                tvTime.setText(mActivity.getString(R.string.today) + TimeUtils.displayTimeWithoutOffset(userViewCommunity.getViewedDate(), true));
            } else {
                //YYY-MM-DD hh:mm aa
                tvTime.setText(TimeUtils.displayTimeWithoutOffset(userViewCommunity.getViewedDate(), false));
            }
            tvPosition.setText(userViewCommunity.getPositionName());

            if (!TextUtils.isEmpty(userViewCommunity.getAvatar())) {
                Picasso.with(mActivity)
                        .load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + userViewCommunity.getAvatar())
                        .error(R.drawable.avatar_l)
                        .placeholder(R.drawable.avatar_l)
                        .into(ivAvatar);
            } else {
                Picasso.with(mActivity).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + Statics.IMAGE_DEAULT)
                        .error(R.drawable.avatar_l)
                        .placeholder(R.drawable.avatar_l)
                        .into(ivAvatar);
            }

            // set selected state for wrapper
            itemView.setActivated(userViewCommunity.ismIsSelected());
            if (userViewCommunity.ismIsSelected()) {
                rlCheck.setAlpha(1.0f);
                rlCheck.setRotationY(0);
            } else {
                rlCheck.setAlpha(0.0f);
            }
//            runAnimationState(userViewNotice);
            itemView.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.btn_press_gray));
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (!isCheck) {
//                        isCheck = true;
//                        runAnimationState(userViewNotice);
//                        if (onItemSelectListener != null) {
//
//                            onItemSelectListener.onItemSelect(userViewNotice);
//                        }
//
//                    }
//                    return true;
//                }
//            });
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (isCheck) {
//                        isCheck = false;
//                        showHideCheckAnimation(false);
//                        userViewNotice.setmIsSelected(false);
//                        if (onItemSelectListener != null) {
//                            onItemSelectListener.onItemSelect(userViewNotice);
//                        }
//                    }
//
//                }
//            });

        }

        public void runAnimationState(UserViewCommunity userDto) {
            if (!userDto.ismIsSelected()) {
                showHideCheckAnimation(true);
                userDto.setmIsSelected(true);
            } else {
                showHideCheckAnimation(false);
                userDto.setmIsSelected(false);
            }
            itemView.setActivated(userDto.ismIsSelected());

            AddOrRemoveMail(userDto);

        }

        private void AddOrRemoveMail(UserViewCommunity userViewNotice) {
            if (getItems().contains(userViewNotice)) {
                // remove if already add selected list
                getItems().remove(userViewNotice);
            } else {
                getItems().add(userViewNotice);
            }
        }

        public void showHideCheckAnimation(boolean isShowCheck) {
            if (isShowCheck) {
                final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(mActivity,
                        R.animator.flip_right);

                final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(mActivity,
                        R.animator.flip_left_in);
                setRightOut.setTarget(ivAvatar);
                setRightOut.start();
                setLeftIn.setTarget(rlCheck);
                setLeftIn.start();
            } else {
                final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(mActivity,
                        R.animator.flip_right_out);

                final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(mActivity,
                        R.animator.flip_left_in);
                setRightOut.setTarget(rlCheck);
                setRightOut.start();
                setLeftIn.setTarget(ivAvatar);
                setLeftIn.start();
            }
            itemView.setActivated(isShowCheck);
        }


    }


    public interface OnItemSelectListener {
        void onItemSelect(UserViewCommunity userViewNotice);
    }

    private OnItemSelectListener onItemSelectListener;

    public void setOnMailItemSelectListener(OnItemSelectListener listener) {
        this.onItemSelectListener = listener;
    }


}
