package com.crewcloud.apps.crewboard.dialog;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;


import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.base.BaseDialog;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.crewcloud.apps.crewboard.util.Util.getTypeFile;


/**
 * Created by Dazone on 1/13/2017.
 */

public class DownloadAttachDialog extends BaseDialog {

    private String url;
    private String name;


    public DownloadAttachDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_attach);
        ButterKnife.bind(this);
    }

    @Override
    public void show() {
        super.show();
    }

    @OnClick(R.id.dialog_download_bt_ok)
    public void onClickOk() {
        //download attach
        String mimeType;
        String serviceString = Context.DOWNLOAD_SERVICE;
        String fileType = name.substring(name.lastIndexOf(".")).toLowerCase();
        final DownloadManager downloadmanager;
        downloadmanager = (DownloadManager) getContext().getSystemService(serviceString);
        Uri uri = Uri
                .parse(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Statics.pathDownload, name);
        //request.setTitle(name);
        int type = getTypeFile(fileType);
        switch (type) {
            case 1:
                request.setMimeType(Statics.MIME_TYPE_IMAGE);
                break;
            case 2:
                request.setMimeType(Statics.MIME_TYPE_VIDEO);
                break;
            case 3:
                request.setMimeType(Statics.MIME_TYPE_AUDIO);
                break;
            default:
                try {
                    mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                } catch (Exception e) {
                    e.printStackTrace();
                    mimeType = Statics.MIME_TYPE_ALL;
                }
                if (TextUtils.isEmpty(mimeType)) {
                    request.setMimeType(Statics.MIME_TYPE_ALL);
                } else {
                    request.setMimeType(mimeType);
                }
                break;
        }
        final Long reference = downloadmanager.enqueue(request);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(reference);
                    Cursor c = downloadmanager.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                        }
                    }
                }
            }
        };
        getContext().registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        dismiss();
    }


    @OnClick(R.id.dialog_download_bt_cancel)
    public void onClickCancel() {
        dismiss();
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setNameAttach(String nameAttach) {
        this.name = nameAttach;
    }
}
