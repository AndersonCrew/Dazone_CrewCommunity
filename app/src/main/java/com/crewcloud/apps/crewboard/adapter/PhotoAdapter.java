package com.crewcloud.apps.crewboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.Attachments;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tunglam on 12/16/16.
 */

public class PhotoAdapter extends BaseAdapter<Attachments, PhotoAdapter.ViewHolder> {

    private boolean isAdd;

    public PhotoAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_photo_image)
        ImageView ivImage;

        @Bind(R.id.item_photo_name)
        TextView tvName;

        @Bind(R.id.item_photo_remove)
        ImageView ivRemove;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClichAttach(getAdapterPosition());
                    }
                }
            });
        }

        public void bind(final int position) {
            Attachments photo = getItem(position);
            tvName.setText(photo.getName());
            if (isAdd) {
                ivRemove.setVisibility(View.VISIBLE);
            } else {
                ivRemove.setVisibility(View.GONE);
            }

            Util.animateClickButton(ivRemove, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRemoveItem(position);
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClichAttach(int position);

        void onRemoveItem(int position);
    }

    private ItemClickListener listener;

    public void setOnClickItemAttach(ItemClickListener listener) {
        this.listener = listener;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}

