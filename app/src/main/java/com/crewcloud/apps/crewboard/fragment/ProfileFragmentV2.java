package com.crewcloud.apps.crewboard.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.Belong;
import com.crewcloud.apps.crewboard.dtos.BelongDepartmentDTO;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.dtos.UserDto;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenter;
import com.crewcloud.apps.crewboard.module.profile.LogoutPresenterImp;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dazone on 5/9/2017.
 */

public class ProfileFragmentV2 extends BaseFragment implements LogoutPresenter.view {
    private static final int REQUEST_CALL = 1;
    private UserDto userData;
    private Intent callIntent;
    private LogoutPresenterImp logoutPresenterImp;

    @Bind(R.id.activity_new_profile_iv_avatar)
    CircleImageView ivAvatar;

    @Bind(R.id.activity_new_profile_tv_name)
    TextView tvName;

    @Bind(R.id.activity_new_profile_tv_depart_position)
    TextView tvDepartPositionName;

    @Bind(R.id.activity_new_profile_tv_company_name)
    TextView tvCompanyName;

    @Bind(R.id.activity_new_profile_tv_company_id)
    TextView tvCompanyId;

    @Bind(R.id.activity_new_profile_tv_persion_id)
    TextView tvPersionId;

    @Bind(R.id.activity_new_profile_tv_email)
    TextView tvEmail;

    @Bind(R.id.activity_new_profile_tv_pass)
    TextView tvPass;

    @Bind(R.id.activity_new_profile_tv_phone)
    TextView tvPhone;

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
        View view = inflater.inflate(R.layout.activity_new_profile, container, false);
        ButterKnife.bind(this, view);
        logoutPresenterImp = new LogoutPresenterImp(getBaseActivity());
        logoutPresenterImp.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutPresenterImp.getProfile();
        Prefs pref = CrewBoardApplication.getInstance().getmPrefs();
        PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
        tvName.setText(pref.getUserName());
        tvCompanyId.setText(preferenceUtilities.getCompany());
        tvEmail.setText(pref.getMailAddress());
        tvPersionId.setText(pref.getUserid());
        tvPass.setText(pref.getPass());
        tvCompanyName.setText(pref.getCompanyName());

        Picasso.with(getContext()).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + pref.getAvatar()).into(ivAvatar);
    }

    @OnClick(R.id.activity_new_profile_iv_call_number)
    public void onClickCall() {
        callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + "123456789"));


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(callIntent);
        }

    }

    @OnClick(R.id.message)
    public void onClickMessage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", tvPhone.getText().toString(), null)));
    }

    @OnClick(R.id.sent_email)
    public void onClickSendEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String[] recipients = new String[]{tvEmail.getText().toString(), "",};
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an Email client"));
    }

    @OnClick(R.id.change_pass)
    public void onClickChangePass() {
        BaseEvent baseEvent = new BaseEvent(BaseEvent.EventType.CHANGE_PASS);
        Bundle data = new Bundle();
//        data.putSerializable(Statics.ID_COMMUNITY,;
        MenuEvent menuEvent = new MenuEvent();
        menuEvent.setBundle(data);
        baseEvent.setMenuEvent(menuEvent);
        EventBus.getDefault().post(baseEvent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                }
            }
        }
    }

    @Override
    public void onGetProfileSuccess(Profile profile) {
        for (BelongDepartmentDTO belong : profile.getBelongs()) {
            if (belong.isDefault()) {
                tvDepartPositionName.setText(belong.getDepartName() + "/" + belong.getPositionName());
            }
        }


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
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

}
