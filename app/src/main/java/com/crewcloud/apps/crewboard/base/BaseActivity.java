package com.crewcloud.apps.crewboard.base;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.interfaces.ActionCallback;
import com.crewcloud.apps.crewboard.listener.OnClickOptionMenu;
import com.crewcloud.apps.crewboard.listener.OnCreateCustomOptionMenuListener;
import com.crewcloud.apps.crewboard.listener.OnItemClickOptionMenu;
import com.crewcloud.apps.crewboard.net.MyApi;
import com.crewcloud.apps.crewboard.util.BackgroundUtils;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

/**
 * Created by dazone on 2/20/2017.
 */

public class BaseActivity extends AppCompatActivity {

    private int backPressCount;
    private boolean isLock;
    private Dialog mProgressDialog;

    @Inject
    Realm realm;

    @Inject
    MyApi api;

    @Inject
    EventBus eventBus;

    public void setTitle(String title) {

    }

    public void changeFragment(BaseFragment fragment, boolean addBackStack,
                               String name) {

    }

    public void changeFragmentBundle(BaseFragment.FragmentEnums fragment, boolean addBackStack,
                                     Bundle bundle) {

    }

    public Realm getRealm() {
        return realm;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrewBoardApplication.getInstance().getAppComponent().inject(this);

    }

    public void setOptionMenu(String title, OnClickOptionMenu listener) {

    }

    public void setOptionMenu(int icon, OnClickOptionMenu listener) {

    }

    public void setLeftMenu(int res) {

    }

    public void setShowActionbarLogo(boolean isShow) {

    }

    public void setShowSearchIcon(boolean isShow) {

    }

    public void resetLeftDrawerStatus() {

    }

    public void setActionFloat(boolean isShow) {

    }



    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        }
        backPressCount++;
        if (backPressCount > 1) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.mainActivity_message_exit), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressCount = 0;
                }
            }, 3000);
        }
    }

    public void showProgressDialog() {
        if (null == mProgressDialog || !mProgressDialog.isShowing()) {
            mProgressDialog = new Dialog(this, R.style.ProgressCircleDialog);
            mProgressDialog.setTitle(getString(R.string.loading_content));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setOnCancelListener(null);
            mProgressDialog.addContentView(new ProgressBar(this), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public MyApi getApi() {
        return api;
    }

    public EventBus getEventBus() {
        if (eventBus == null) {
            eventBus = EventBus.getDefault();
        }
        return eventBus;
    }

    public <T> void requestAPI(Observable<T> observable, ResponseListener<T> responseListener) {
        if (observable == null) {
            return;
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseListener);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        getRealm().close();
    }
}