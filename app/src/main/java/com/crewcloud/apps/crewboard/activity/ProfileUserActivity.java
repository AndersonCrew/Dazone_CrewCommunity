package com.crewcloud.apps.crewboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.dtos.BelongDepartmentDTO;
import com.crewcloud.apps.crewboard.dtos.Profile;
import com.crewcloud.apps.crewboard.interfaces.GetUserCallBack;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.HttpRequest;
import com.crewcloud.apps.crewboard.util.Prefs;
import com.crewcloud.apps.crewboard.util.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;


public class ProfileUserActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_name, tv_personal, tv_email, tv_company, tv_company_phone, tvExtensionNumber,
            tvCellPhone, tv_position, tv_password, tv_date, tv_birthday;
    private LinearLayout layoutExtensionNumber, layoutCellPhone, layoutCompanyPhone;
    private String cellPhone = "";
    private String emailAddress = "";
    private ImageView avatar_imv;
    public Prefs prefs;
    private RelativeLayout lay_image_profile;
    private static final int REQUEST_CALL = 1;
    private TextView btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.nav_back_ic);
        toolbar.setTitle(getString(R.string.profle));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        prefs = CrewBoardApplication.getInstance().getmPrefs();
        lay_image_profile = findViewById(R.id.lay_image_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_personal = findViewById(R.id.tv_personal);
        tv_email = findViewById(R.id.tv_email);
        tv_company = findViewById(R.id.tv_company);
        tv_company_phone = findViewById(R.id.tv_company_phone);
        tv_position = findViewById(R.id.tv_position);
        tv_password = findViewById(R.id.tv_password);
        tv_birthday = findViewById(R.id.tv_birthday);
        tv_date = findViewById(R.id.tv_join);
        tvExtensionNumber = findViewById(R.id.tvExtensionNumber);
        tvCellPhone = findViewById(R.id.tvCellPhone);
        layoutExtensionNumber = findViewById(R.id.layoutExtensionNumber);
        layoutCellPhone = findViewById(R.id.layoutCellPhone);
        layoutCompanyPhone = findViewById(R.id.layoutCompanyNumber);
        avatar_imv = findViewById(R.id.avatar_imv);
        btnChangePassword = findViewById(R.id.btn_change_password);
        btnChangePassword.setOnClickListener(this);

        llJoinDate = findViewById(R.id.layoutJoinDate);
        llBirthday = findViewById(R.id.layout_birthday);

        findViewById(R.id.img_call).setOnClickListener(this);
        findViewById(R.id.img_email).setOnClickListener(this);
        findViewById(R.id.img_message).setOnClickListener(this);
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpRequest.getInstance().GetUser(prefs.getUserNo(), new GetUserCallBack() {
            @Override
            public void onGetUserSuccess(Profile profile) {
                fillData(profile);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void fillData(Profile profile) {
        Prefs prefs = new Prefs();

        emailAddress = profile.getMailAddress();
        final String url = prefs.getServerSite() + profile.getAvatar();
        ImageLoader.getInstance().displayImage(url, avatar_imv);

        avatar_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileUserActivity.this, ImageViewActivity.class);
                intent.putExtra(Statics.KEY_URL, url);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        tv_name.setText(profile.getName());
        tv_personal.setText(profile.getUserId());
        tv_name.setText(profile.getName());
        tv_email.setText(profile.getMailAddress());
        tv_position.setText(getPositionName(profile));
        tv_password.setText(prefs.getPass());

        String company = prefs.getStringValue(Statics.PREFS_KEY_COMPANY_NAME, "");
        tv_company.setText(company);
        cellPhone = !TextUtils.isEmpty(profile.getCellPhone().trim()) ?
                profile.getCellPhone() :
                !TextUtils.isEmpty(profile.getCompanyPhone().trim()) ?
                        profile.getCompanyPhone() :
                        "";

        String companyPhone = "";
        String extensionNumber = "";
        try {
            companyPhone = profile.getCompanyPhone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            extensionNumber = profile.getExtensionNumber();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(">>>",">>>" + cellPhone);
        tvCellPhone.setText(cellPhone);
        tvExtensionNumber.setText(extensionNumber);
        tv_company_phone.setText(companyPhone);
        if (cellPhone.length() > 0) layoutCellPhone.setVisibility(View.VISIBLE);
        else layoutCellPhone.setVisibility(View.GONE);
        if (companyPhone.length() > 0) layoutCompanyPhone.setVisibility(View.VISIBLE);
        else layoutCompanyPhone.setVisibility(View.GONE);
        if (extensionNumber.length() > 0) layoutExtensionNumber.setVisibility(View.VISIBLE);
        else layoutExtensionNumber.setVisibility(View.GONE);

        String birthDay = TimeUtils.displayTimeWithoutOffsetV2(profile.getBirthDate());
        int yearBirthDay = Integer.parseInt(TimeUtils.formatYear(profile.getBirthDate()));

        String joinDate = TimeUtils.displayTimeWithoutOffsetV2(profile.getEntranceDate());
        int yearDateJoin = Integer.parseInt(TimeUtils.formatYear(profile.getEntranceDate()));

        if(yearBirthDay <= 1900) {
            llBirthday.setVisibility(View.GONE);
        } else {
            llBirthday.setVisibility(View.VISIBLE);
            tv_birthday.setText(birthDay);
        }

        if(yearDateJoin <= 1900) {
            llJoinDate.setVisibility(View.GONE);
        } else {
            llJoinDate.setVisibility(View.VISIBLE);
            tv_date.setText(joinDate);
        }
    }

    private LinearLayout llBirthday, llJoinDate;

    @Override
    public void onBackPressed() {
        if (lay_image_profile.getVisibility() == View.GONE) {
            super.onBackPressed();
        } else {
            lay_image_profile.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String realPath = "";
            switch (requestCode) {
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                callActivity(ChangePasswordActivity.class);
                break;
            case R.id.img_call:
                onClickCall(cellPhone);
                break;
            case R.id.img_email:
                onSendEmail(emailAddress);
                break;
            case R.id.img_message:
                onClickMessage(cellPhone);
                break;
        }
    }

    Intent callIntent;
    public void onClickCall(String phoneNumber) {
        callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    public void onClickMessage(String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)));
    }

    private void onSendEmail(String emailAddress){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailAddress, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private String getPositionName(Profile profile){
        String result = "";

        if(profile.getBelongs()!= null && profile.getBelongs().size() > 0){
            BelongDepartmentDTO belongDefault =  null;
            for (BelongDepartmentDTO belong : profile.getBelongs()) {
                if(belong.isDefault()){
                    belongDefault = belong;
                    result = belong.getDepartName() + "/" + ((belong.getPositionName() == null || belong.getPositionName().isEmpty())? belong.getDutyName() : belong.getPositionName());
                    break;
                }
            }
            if(belongDefault == null){
                BelongDepartmentDTO belong = profile.getBelongs().get(0);
                result = belong.getDepartName() + "/" + ((belong.getPositionName() == null || belong.getPositionName().isEmpty())? belong.getDutyName() : belong.getPositionName());
            }
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    Toast.makeText(this, "please try again", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}