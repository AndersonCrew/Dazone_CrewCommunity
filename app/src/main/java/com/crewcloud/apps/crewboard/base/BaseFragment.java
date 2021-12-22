package com.crewcloud.apps.crewboard.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;


import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.fragment.AdddCommunityFragment;
import com.crewcloud.apps.crewboard.fragment.ChangePasswordFragment;
import com.crewcloud.apps.crewboard.fragment.CommunityDetailFragment;
import com.crewcloud.apps.crewboard.fragment.CommunityFragment;
import com.crewcloud.apps.crewboard.fragment.ListUserViewFragment;
import com.crewcloud.apps.crewboard.listener.OnClickOptionMenu;
import com.crewcloud.apps.crewboard.net.MyApi;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;


/**
 * Created by tunglam on 11/10/16.
 */

public class BaseFragment extends Fragment {
    private BaseActivity activity;
    private String title;
    private String optionMenuName;
    private OnClickOptionMenu listener;
    private int optionIcon;
    private OnViewCreatedListener onViewCreatedListener;
    private int viewPosition;

    @Inject
    MyApi api;

    @Inject
    EventBus eventBus;


    @Inject
    Realm realm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        CrewBoardApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getRealm() != null) {
            getRealm().close();
        }
    }

    public Realm getRealm() {
        return realm;
    }

    public void setTitle(String title) {
        this.title = title;
        activity.setTitle(title);
    }

    public void setOptionMenu(String name, OnClickOptionMenu listener) {
        optionMenuName = name;
        this.listener = listener;
        activity.setOptionMenu(name, listener);
    }

    public void setOptionMenu(int icon, OnClickOptionMenu listener) {
        this.optionIcon = icon;
        this.listener = listener;
        activity.setOptionMenu(icon, listener);
    }

    public void setLeftMenu(int res) {
        activity.setLeftMenu(res);
    }

    public void setActionFloat(boolean isShow) {
        activity.setActionFloat(isShow);
    }

    public void setShowSearchIcon(boolean isShow){
        activity.setShowSearchIcon(isShow);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMenu();
        if (onViewCreatedListener != null) {
            onViewCreatedListener.onViewCreated(viewPosition);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.setOptionMenu("", null);
        activity.setOptionMenu(0, null);
    }

    public void onOptionMenuClick(View view) {
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    public int getColorTheme() {
        return ContextCompat.getColor(getBaseActivity(), R.color.colorAccent);
    }

    /**
     * Remove supper, override empty code for fragments inside BaseTabFragment
     */
    protected void setUpMenu() {
        if (activity == null) {
            return;
        }
        activity.setTitle(title);
        activity.setOptionMenu(optionMenuName, listener);
        activity.setOptionMenu(optionIcon, listener);
    }

    public enum FragmentEnums {
        COMMUNITY_DETAIL(CommunityDetailFragment.class.getName()),
        ADD_COMMUNITY(AdddCommunityFragment.class.getName()),
        LIST_USER_VIEW(ListUserViewFragment.class.getName()),
        EDIT_COMMUNITY(AdddCommunityFragment.class.getName()),
        LIST_COMUNITY(CommunityFragment.class.getName()),
        CHANGE_PASS(ChangePasswordFragment.class.getName());

        private String fragmentName;

        FragmentEnums(String name) {
            this.fragmentName = name;
        }

        public String getFragmentName() {
            return this.fragmentName;
        }
    }

    public void setOnViewCreatedListener(OnViewCreatedListener onViewCreatedListener, int viewPosition) {
        this.onViewCreatedListener = onViewCreatedListener;
        this.viewPosition = viewPosition;
    }

    public boolean onBackPressed() {
        return false;
    }

    public interface OnViewCreatedListener {
        void onViewCreated(int position);
    }

    public boolean hasOptionMenu() {
        return !TextUtils.isEmpty(optionMenuName) || optionIcon > 0;
    }

    public String getTitle() {
        return title;
    }

    public OnClickOptionMenu getListener() {
        return listener;
    }

    public String getOptionMenuName() {
        return optionMenuName;
    }

    public int getOptionIcon() {
        return optionIcon;
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

        if (getBaseActivity() == null) {
            return;
        }

        getBaseActivity().requestAPI(observable, responseListener);
    }
}
