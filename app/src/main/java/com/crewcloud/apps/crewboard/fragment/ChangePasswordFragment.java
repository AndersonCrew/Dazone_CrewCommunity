package com.crewcloud.apps.crewboard.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.module.profile.changepass.ChangePassPresenter;
import com.crewcloud.apps.crewboard.module.profile.changepass.ChangePassPresenterImp;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dazone on 5/11/2017.
 */

public class ChangePasswordFragment extends BaseFragment implements ChangePassPresenter.view {

    @Bind(R.id.fragment_change_pass_et_confirm_pass)
    EditText etConfirmPass;

    @Bind(R.id.fragment_change_pass_et_new_pass)
    EditText etNewPass;

    @Bind(R.id.fragment_change_pass_et_old_pass)
    EditText etOldPass;

    private ChangePassPresenterImp changePassPresenterImp;

    public static BaseFragment newInstance(Bundle bundle) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setTitle(getString(R.string.change_password));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        ButterKnife.bind(this, view);
        changePassPresenterImp = new ChangePassPresenterImp(getBaseActivity());
        changePassPresenterImp.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        changePassPresenterImp.detachView();
    }

    @OnClick(R.id.fragment_change_pass_btn_change_pass)
    public void ChangePass() {
        String oldPass = etOldPass.getText().toString();
        String newPass = etNewPass.getText().toString();
        String confirmPass = etConfirmPass.getText().toString();

        changePassPresenterImp.CheckPass(oldPass, newPass, confirmPass);
    }

    @Override
    public void ChangePassSuccess() {
      getBaseActivity().onBackPressed();

    }

    @Override
    public void ChangePassError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

    }

}
