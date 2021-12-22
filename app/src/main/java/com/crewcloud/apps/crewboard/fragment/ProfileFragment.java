package com.crewcloud.apps.crewboard.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenter;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenterImp;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dazone on 2/28/2017.
 */

public class ProfileFragment extends BaseFragment implements LogoutPresenter.view {

    @Bind(R.id.avatar_imv)
    CircleImageView ivAvatar;

    @Bind(R.id.tv_name)
    TextView tvName;

    @Bind(R.id.tv_personal)
    TextView tvPersional;

    @Bind(R.id.tv_email)
    TextView tvEmail;

    @Bind(R.id.tv_company)
    TextView tvCompany;

    LogoutPresenterImp logoutPresenterImp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setActionFloat(true);
        setTitle(getString(R.string.profle));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_myinfor, container, false);
        ButterKnife.bind(this, view);
        logoutPresenterImp = new LogoutPresenterImp(getBaseActivity());
        logoutPresenterImp.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Prefs pref = CrewBoardApplication.getInstance().getmPrefs();

        tvName.setText(pref.getUserName());
        tvPersional.setText(pref.getUserid());
        tvCompany.setText(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentCompanyDomain());
        Picasso.with(getBaseActivity()).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + pref.getAvatar())
                .error(R.mipmap.no_photo)
                .placeholder(R.mipmap.photo_placeholder)
                .into(ivAvatar);


        tvEmail.setText(pref.getMailAddress());

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_profile_logout:
                //logout
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity())
                        .setMessage(R.string.are_you_sure_loguot)
                        .setPositiveButton(Util.getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logoutPresenterImp.logout(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());

                            }
                        })
                        .setNegativeButton(Util.getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        logoutPresenterImp.detachView();
    }

    @Override
    public void onLogoutSuccess() {
        BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOGOUT);
        MenuEvent menuEvent = new MenuEvent();
        baseEvent.setMenuEvent(menuEvent);
        EventBus.getDefault().post(baseEvent);

//        Intent intent = new Intent(getActivity(), LoginV2Activity.class);
//        startActivity(intent);
    }

    @Override
    public void onGetProfileSuccess(Profile profile) {

    }

    @Override
    public void onError(String message) {

    }
}
