package com.crewcloud.apps.crewboard.adapter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.dtos.Community;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.crewcloud.apps.crewboard.widget.AutoSizeTextView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 2/22/2017.
 */

public class CommunityAdapter extends BaseAdapter<Community, CommunityAdapter.ViewHolder> {

    private OnClickNewsListener onClickNewsListener;

    public void setOnClickNewsListener(OnClickNewsListener onClickNewsListener) {
        this.onClickNewsListener = onClickNewsListener;
    }

    public CommunityAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new CommunityAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CommunityAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_list_community_iv_image)
        ImageView ivImage;

        @Bind(R.id.item_list_community_tv_content)
        TextView tvContent;

        @Bind(R.id.item_list_community_tv_author)
        AutoSizeTextView tvAuthor;

        @Bind(R.id.item_list_community_tv_time)
        TextView tvTime;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickNewsListener != null) {
                        onClickNewsListener.onItemClicked(getAdapterPosition());
                    }
                }
            });
        }

        void bind(int position) {
            final Community community = getItem(position);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            if (community.getReplyCount() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    SpannableString spannableString = new SpannableString(String.valueOf(community.getTitle()));
                    ClickableSpan clickableTitle = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {
                            BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.COMMUNITY_DETAIL);
                            Bundle data = new Bundle();
                            data.putInt(Statics.ID_COMMUNITY, community.getContentNo());
                            MenuEvent menuEvent = new MenuEvent();
                            menuEvent.setBundle(data);
                            baseEvent.setMenuEvent(menuEvent);
                            EventBus.getDefault().post(baseEvent);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    spannableString.setSpan(clickableTitle, 0, String.valueOf(community.getTitle()).length(), 0);
                    spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(android.R.color.black)), 0, String.valueOf(community.getTitle()).length(), 0);
                    builder.append(spannableString).append("  ")
                            .append(" ", new ImageSpan(getContext(), R.drawable.ic_comment), 0).append(" ");
                    SpannableString commnetSpan = new SpannableString(String.valueOf(community.getReplyCount()));

                    commnetSpan.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.app_base_color)), 0, String.valueOf(community.getReplyCount()).length(), 0);
                    builder.append(commnetSpan);

                    if (community.getViewCount() > 0) {
                        SpannableStringBuilder stringBuilder = new SpannableStringBuilder().append(" ")
                                .append(" ", new ImageSpan(getContext(), R.drawable.home_ic_view), 0).append(" ");
                        builder.append(stringBuilder);

                        SpannableString viewSpan = new SpannableString(String.valueOf(community.getViewCount()));

                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LIST_USER_VIEW);
                                Bundle data = new Bundle();
                                data.putSerializable("COMMUNITY", community);
                                MenuEvent event = new MenuEvent();
                                event.setBundle(data);
                                baseEvent.setMenuEvent(event);
                                EventBus.getDefault().post(baseEvent);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                            }
                        };
                        viewSpan.setSpan(clickableSpan, 0, String.valueOf(community.getViewCount()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(viewSpan);
                    }

                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (community.getViewCount() > 0) {
                        SpannableString spannableString = new SpannableString(String.valueOf(community.getTitle()));
                        ClickableSpan clickableTitle = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.COMMUNITY_DETAIL);
                                Bundle data = new Bundle();
                                data.putInt(Statics.ID_COMMUNITY, community.getContentNo());
                                MenuEvent menuEvent = new MenuEvent();
                                menuEvent.setBundle(data);
                                baseEvent.setMenuEvent(menuEvent);
                                EventBus.getDefault().post(baseEvent);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                            }
                        };
                        spannableString.setSpan(clickableTitle, 0, String.valueOf(community.getTitle()).length(), 0);
                        spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(android.R.color.black)), 0, String.valueOf(community.getTitle()).length(), 0);
                        builder.append(spannableString).append(" ").append(" ", new ImageSpan(getContext(), R.drawable.home_ic_view), 0).append(" ");
                        SpannableString viewSpan = new SpannableString(String.valueOf(community.getViewCount()));
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LIST_USER_VIEW);
                                Bundle data = new Bundle();
                                data.putSerializable("COMMUNITY", community);
                                MenuEvent event = new MenuEvent();
                                event.setBundle(data);
                                baseEvent.setMenuEvent(event);
                                EventBus.getDefault().post(baseEvent);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                            }
                        };
                        viewSpan.setSpan(clickableSpan, 0, String.valueOf(community.getViewCount()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.append(viewSpan);
                    } else {
                        SpannableString spannableString = new SpannableString(String.valueOf(community.getTitle()));
                        ClickableSpan clickableTitle = new ClickableSpan() {
                            @Override
                            public void onClick(View textView) {
                                BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.COMMUNITY_DETAIL);
                                Bundle data = new Bundle();
                                data.putInt(Statics.ID_COMMUNITY, community.getContentNo());
                                MenuEvent menuEvent = new MenuEvent();
                                menuEvent.setBundle(data);
                                baseEvent.setMenuEvent(menuEvent);
                                EventBus.getDefault().post(baseEvent);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                            }
                        };
                        spannableString.setSpan(clickableTitle, 0, String.valueOf(community.getTitle()).length(), 0);
                        spannableString.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(android.R.color.black)), 0, String.valueOf(community.getTitle()).length(), 0);
                        builder.append(spannableString);
                    }
                }
            }


            tvContent.setText(builder);
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            tvContent.setHighlightColor(Color.TRANSPARENT);
            long time = TimeUtils.getTimeFromString(community.getRegDate());
            long timeCreated = TimeUtils.getTimeForMail(time);
            if (timeCreated == -2) {
                tvTime.setText(mActivity.getString(R.string.today) + TimeUtils.displayTimeWithoutOffset(community.getRegDate(), true));
            } else {
                tvTime.setText(TimeUtils.displayTimeWithoutOffset(community.getRegDate(), false));
            }

            tvAuthor.setText(community.getRegUserName() + "( " + community.getRegDepartName() + " ) ");
        }
    }

    public interface OnClickNewsListener {
        void onItemClicked(int position);
    }
}
