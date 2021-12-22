package com.crewcloud.apps.crewboard.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.adapter.CommentAdapter;
import com.crewcloud.apps.crewboard.adapter.PhotoAdapter;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dialog.DownloadAttachDialog;
import com.crewcloud.apps.crewboard.dtos.Attachments;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dazone on 3/1/2017.
 */
public class AttachmentAdapter extends BaseAdapter<Attachments, AttachmentAdapter.ViewHolder> {

    private boolean isAdd;

    public AttachmentAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public AttachmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attach, parent, false);
        return new AttachmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttachmentAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.item_attach_tv_name_attach)
        TextView tvName;

        @Bind(R.id.item_attach_tv_height)
        TextView tvHeight;


        @Bind(R.id.item_attach_remove)
        ImageView ivRemove;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadAttachDialog dialog = new DownloadAttachDialog(getContext());
                    dialog.setUrl(getItem(getAdapterPosition()).getName());
                    dialog.setNameAttach(Util.getFileName(getItem(getAdapterPosition()).getName()));
                    dialog.show();
                }
            });
        }

        public void bind(final int position) {
            Attachments attachments = getItem(position);
            tvName.setText(Util.getFileName(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + attachments.getName()) + "(" + Util.readableFileSize(attachments.getSize()) + ")");
            if (isAdd) {
                ivRemove.setVisibility(View.VISIBLE);
            } else {
                ivRemove.setVisibility(View.GONE);
            }
            ivRemove.setOnClickListener(new View.OnClickListener() {
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