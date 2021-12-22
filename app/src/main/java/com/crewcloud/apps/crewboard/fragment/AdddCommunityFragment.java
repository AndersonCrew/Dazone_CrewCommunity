package com.crewcloud.apps.crewboard.fragment;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.adapter.GroupAdapter;
import com.crewcloud.apps.crewboard.adapter.JobAdapter;
import com.crewcloud.apps.crewboard.adapter.PhotoAdapter;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dialog.GroupDialog;
import com.crewcloud.apps.crewboard.dtos.Attachments;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.dtos.CommunityDetail;
import com.crewcloud.apps.crewboard.event.EventChildBoard;
import com.crewcloud.apps.crewboard.module.addcommunity.AddCommunityPresenter;
import com.crewcloud.apps.crewboard.module.addcommunity.AddCommunityPresenterImp;
import com.crewcloud.apps.crewboard.service.UploadFileToServer;
import com.crewcloud.apps.crewboard.util.Util;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.http.HTTP;

/**
 * Created by dazone on 2/27/2017.
 */

public class AdddCommunityFragment extends BaseFragment implements AddCommunityPresenter.view {

    public static final int REQUEST_ATTACH = 1;
    public static final int REQUEST_CAMERA = 2;

    @Bind(R.id.fragment_add_community_et_title)
    EditText etTitle;

    @Bind(R.id.fragment_add_community_tv_job)
    TextView tvJob;

    @Bind(R.id.switch_btn)
    Switch switchAlarm;

    @Bind(R.id.fragment_add_community_lv_attach)
    RecyclerView lvAttach;

    @Bind(R.id.fragment_add_community_et_content)
    EditText etContent;

    JobAdapter jobAdapter;
    AttachmentAdapter adapter;
    List<ChildFolders> jobsList;
    private int broadNo;
    private boolean switchControl;
    private CommunityDetail communityDetail;
    private List<String> images;
    AddCommunityPresenterImp addCommunityPresenterImp;
    private GroupDialog dialog;

    public AdddCommunityFragment() {
    }

    public static BaseFragment newInstance(Bundle bundle) {

        AdddCommunityFragment adddCommunityFragment = new AdddCommunityFragment();
        adddCommunityFragment.setArguments(bundle);
        return adddCommunityFragment;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobsList = new ArrayList<>();
        setHasOptionsMenu(true);
        adapter = new AttachmentAdapter(getBaseActivity());
        if (getArguments() != null) {
            communityDetail = (CommunityDetail) getArguments().getSerializable("DETAIL");

        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_community, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.menu_add_community_camera:
//                ImagePicker.with(getBaseActivity()).startActivityForResult(4, REQUEST_CAMERA);
//                return true;
            case R.id.menu_add_community_check:
                addCommunityPresenterImp.validate(etTitle.getText().toString(), etContent.getText().toString(),broadNo);
                return true;
            case R.id.menu_add_community_attaches:
//                Intent i = new Intent(getActivity(), FilePickerActivity.class);
//                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
//                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
//                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
//                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
//
//                startActivityForResult(i, REQUEST_ATTACH);
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_community, container, false);
        ButterKnife.bind(this, view);
        getEventBus().register(this);
        setActionFloat(true);
        addCommunityPresenterImp = new AddCommunityPresenterImp(getBaseActivity());
        addCommunityPresenterImp.attachView(this);
        setTitle("");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addCommunityPresenterImp.getMenuLeft();
        if (communityDetail != null) {
            showData(communityDetail);
        }
        adapter.setAdd(true);
        etTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getBaseActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etTitle, InputMethodManager.SHOW_IMPLICIT);
        lvAttach.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvAttach.setAdapter(adapter);
//
        adapter.setOnClickItemAttach(new AttachmentAdapter.ItemClickListener() {
            @Override
            public void onItemClichAttach(int position) {

            }

            @Override
            public void onRemoveItem(int position) {
                adapter.getItems().remove(position);
                adapter.notifyDataSetChanged();
            }
        });

        switchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                if (on) {
                    switchControl = true;
                } else {
                    switchControl = false;
                }
            }
        });

    }

    private void showData(CommunityDetail communityDetail) {
        etTitle.setText(Html.fromHtml(communityDetail.getTitle()));
        etContent.setText(Html.fromHtml(communityDetail.getContent()));
        switchAlarm.setChecked(communityDetail.isAlarm());
        adapter.addAll(communityDetail.getAttachmentses());

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CAMERA:
//                    if (data != null) {
//                        ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
//
//                        List<Attachments> lstAttachments = new ArrayList<>();
//                        Attachments attachments = new Attachments();
//                        for (int i = 0; i < images.size(); i++) {
//                            String newAvatar = images.get(i).path;
//                            if (!TextUtils.isEmpty(newAvatar)) {
////                                attachments.setDownloadUrl(newAvatar);
////                                attachments.setFileName("name" + i++);
//                                lstAttachments.add(attachments);
//                            }
//                        }
//                        adapter.addAll(lstAttachments);
//                    }
//                    break;
//                case REQUEST_ATTACH:
//                    if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
//                        // For JellyBean and above
//                        if (!data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
//                            // The URI will now be something like content://PACKAGE-NAME/root/path/to/file
//                            Uri uri = data.getData();
//                            // A utility method is provided to transform the URI to a File object
//                            File file = Util.getFileForUri(uri);
//                            // If you want a URI which matches the old return value, you can do
//                            Uri fileUri = Uri.fromFile(file);
//                            GetFile(fileUri);
//                        } else {
//                            // Handling multiple results is one extra step
//                            ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
//                            if (paths != null) {
//                                for (String path : paths) {
//                                    Uri uri = Uri.parse(path);
//                                    // Do something with the URI
//                                    File file = Util.getFileForUri(uri);
//                                    // If you want a URI which matches the old return value, you can do
//                                    Uri fileUri = Uri.fromFile(file);
//                                    // Do something with the result...
//                                    GetFile(fileUri);
//                                }
//                            }
//                        }
//                    }
//
//                    break;
//            }
//        }
//
//
//    }


    private class Upload extends UploadFileToServer {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private void GetFile(Uri uri) {
        String path = Util.getPathFromURI(uri, getActivity());
        String fileName = Util.getFileName(uri, getActivity());
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        long fileSize = Util.getFileSize(path);

        new Upload().execute(path, fileName);

        List<Attachments> lstAttachments = new ArrayList<>();
        Attachments attachments = new Attachments();
        attachments.setName(fileName);
        lstAttachments.add(attachments);
        adapter.addAll(lstAttachments);

    }

    @OnClick({R.id.fragment_add_community_tv_job, R.id.fragment_add_community_iv_select})
    public void onClickGroup() {
        dialog = new GroupDialog(getBaseActivity());
        dialog.setDatas(jobsList);
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        getEventBus().unregister(this);
        addCommunityPresenterImp.detachView();
    }

    @Override
    public void onAddSuccess() {
        getBaseActivity().changeFragment(new CommunityFragment(), false, CommunityFragment.class.getSimpleName());
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetSuccess(List<ChildFolders> childFolderses) {
        jobsList.addAll(childFolderses);
        List<ChildBoards> lstBoards = new ArrayList<>();
        for (ChildFolders childFolders : childFolderses) {

            if (childFolders.getLstChildBoards() != null && childFolders.getLstChildBoards().size() > 0) {
                for (ChildBoards childBoards : childFolders.getLstChildBoards()) {
                    lstBoards.add(childBoards);
                }
            }
            if (childFolders.getLstChildFolders() != null && childFolders.getLstChildFolders().size() > 0) {

                for (ChildFolders childFolder : childFolders.getLstChildFolders()) {
                    if (childFolder.getLstChildBoards().size() > 0 && childFolder.getLstChildBoards() != null) {
                        for (ChildBoards chilBoard : childFolder.getLstChildBoards()) {
                            lstBoards.add(chilBoard);
                        }
                    }
                }

            }

        }

    }

    @Subscribe
    public void onEvent(EventChildBoard event) {
        if (event != null) {
            tvJob.setText(event.getChildBoards().getName());
            broadNo = event.getChildBoards().getBoardNo();
            dialog.dismiss();
        }
    }

    @Override
    public void onValiDateSuccess(String title, String content) {

        addCommunityPresenterImp.addCommunity(title, content, images, broadNo, switchControl);
    }
}
