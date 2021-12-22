package com.crewcloud.apps.crewboard.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenter;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenterImp;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.DialogUtil;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Dazone on 6/14/2017.
 */

public class SettingFragment extends BaseFragment implements LogoutPresenter.view {
    @Bind(R.id.ln_profile)
    LinearLayout lnProfile;

    @Bind(R.id.img_avatar)
    CircleImageView ivAvatar;

    @Bind(R.id.ln_about)
    LinearLayout lnAbout;

    LogoutPresenterImp logoutPresenterImp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setActionFloat(true);
        setTitle(getString(R.string.setting));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        logoutPresenterImp = new LogoutPresenterImp(getBaseActivity());
        logoutPresenterImp.attachView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String avatar = CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentServiceDomain() + CrewBoardApplication.getInstance().getPreferenceUtilities().getUserAvatar();

        Picasso.with(getActivity())
                .load(avatar)
                .error(R.drawable.avatar_l)
                .placeholder(R.drawable.avatar_l)
                .into(ivAvatar);
    }

    @OnClick(R.id.ln_logout)
    public void logout() {
        //logout
//        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity())
//                .setMessage(R.string.are_you_sure_loguot)
//                .setPositiveButton(Util.getString(R.string.auto_login_button_yes), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        logoutPresenterImp.logout(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
//
//                    }
//                })
//                .setNegativeButton(Util.getString(R.string.auto_login_button_no), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.show();
        DialogUtil.customAlertDialog(getActivity(), getString(R.string.are_you_sure_loguot), getString(R.string.auto_login_button_yes), getString(R.string.auto_login_button_no),
                new DialogUtil.OnAlertDialogViewClickEvent() {
                    @Override
                    public void onOkClick(DialogInterface alertDialog) {
                        logoutPresenterImp.logout(CrewBoardApplication.getInstance().getPreferenceUtilities().getCurrentMobileSessionId());
                    }

                    @Override
                    public void onCancelClick() {

                    }
                });
    }

    @OnClick(R.id.ln_profile)
    public void onClickProfile() {
        getBaseActivity().changeFragment(new ProfileFragmentV2(), true, getString(R.string.profle));
    }

    @OnClick(R.id.ln_about)
    public void onClickAbout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.about_crewcommunity));

        String versionName = BuildConfig.VERSION_NAME;
        String user_version = getResources().getString(R.string.user_version) + " " + versionName;

        builder.setMessage(user_version);

        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button b = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if (b != null) {
            b.setTextColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        }
    }

    @Override
    public void onLogoutSuccess() {
        BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.LOGOUT);
        MenuEvent menuEvent = new MenuEvent();
        baseEvent.setMenuEvent(menuEvent);
        EventBus.getDefault().post(baseEvent);
    }

    @Override
    public void onGetProfileSuccess(Profile profile) {

    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
