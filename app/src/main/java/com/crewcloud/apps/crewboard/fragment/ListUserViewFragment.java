package com.crewcloud.apps.crewboard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.adapter.ViewUserAdapter;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.Community;
import com.crewcloud.apps.crewboard.dtos.UserViewCommunity;
import com.crewcloud.apps.crewboard.module.userview.UserViewPresenter;
import com.crewcloud.apps.crewboard.module.userview.UserViewPresenterImp;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dazone on 1/9/2017.
 */

public class ListUserViewFragment extends BaseFragment implements UserViewPresenter.view {

    @Bind(R.id.list_user_view)
    RecyclerView recyclerView;

    ViewUserAdapter adapter;

    private int contentNo;
    boolean isCheck = false;
    private List<UserViewCommunity> lstUser;

    private UserViewPresenterImp userViewPresenterImp;


    public static BaseFragment newInstance(Bundle bundle) {
        ListUserViewFragment listUserViewFragment = new ListUserViewFragment();
        listUserViewFragment.setArguments(bundle);
        return listUserViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            Community community = (Community) getArguments().getSerializable("COMMUNITY");
            if (community != null) {
                contentNo = community.getContentNo();
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_user_view, container, false);
        ButterKnife.bind(this, view);
        setActionFloat(true);
        userViewPresenterImp = new UserViewPresenterImp(getBaseActivity());
        userViewPresenterImp.attachView(this);
        adapter = new ViewUserAdapter(getBaseActivity());
        setTitle(getString(R.string.list_view));
        lstUser = new ArrayList<>();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewPresenterImp.getUserView(contentNo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_list_check_all:
//                if (isCheck) {
//                    isCheck = false;
//                    releaseAllSelectedItem();
//                } else {
//                    selectAllListUser();
//                    isCheck = true;
//                }
//                break;
//            case R.id.menu_list_sent_email:
//
//                if (lstUser.size() > 0) {
//                    BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.SENT_EMAIL);
//                    Bundle data = new Bundle();
//                    data.putSerializable(Statics.USER_VIEW_NOTICE, (Serializable) lstUser);
//                    MenuEvent event = new MenuEvent();
//                    event.setBundle(data);
//                    baseEvent.setMenuEvent(event);
//                    EventBus.getDefault().post(baseEvent);
//                } else {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity())
//                            .setTitle(R.string.app_name)
//                            .setMessage(R.string.please_select_user)
//                            .setPositiveButton(Util.getString(R.string.string_ok), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                }
//                            });
//                    builder.show();
//                }
//                break;
//        }
//        return false;
//    }
//
//    private void releaseAllSelectedItem() {
//        int index = 0;
//        lstUser.clear();
//        for (UserViewNotice userViewNotice : adapter.getItems()) {
//            if (userViewNotice.ismIsSelected()) {
//                userViewNotice.setmIsSelected(false);
//                lstUser.add(userViewNotice);
//            }
//            index++;
//
//        }
//        setTitle(getString(R.string.list_view));
//        adapter.notifyDataSetChanged();
//    }
//
//    private void selectAllListUser() {
//        int index = 0;
//        lstUser.clear();
//        for (UserViewNotice userViewNotice : adapter.getItems()) {
//            if (!userViewNotice.ismIsSelected()) {
//                userViewNotice.setmIsSelected(true);
//                lstUser.add(userViewNotice);
//            }
//            index++;
//        }
//        setTitle("" + countRead + "/" + countRead);
//        adapter.notifyDataSetChanged();
//
//    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        userViewPresenterImp.detachView();
    }

    @Override
    public void onGetUserViewSuccess(List<UserViewCommunity> list) {
        adapter.addAll(list);
    }

    @Override
    public void onError(String message) {

    }
}


