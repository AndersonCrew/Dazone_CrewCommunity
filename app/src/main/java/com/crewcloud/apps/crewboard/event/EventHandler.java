package com.crewcloud.apps.crewboard.event;

import android.content.Intent;
import android.os.Bundle;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.LoginActivity;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.activity.v2.MainActivity;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.fragment.AdddCommunityFragment;
import com.crewcloud.apps.crewboard.fragment.ProfileFragmentV2;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.PreferenceUtilities;


/**
 * Created by tunglam on 12/16/16.
 */

public class EventHandler {
    private MainActivity activity;

    public EventHandler(MainActivity activity) {
        this.activity = activity;
    }

    public void onEvent(BaseEvent event) {

        switch (event.getType()) {
            case BaseEvent.EventType.SETTING:
                activity.changeFragment(new ProfileFragmentV2(), true, activity.getString(R.string.profle));
                break;
            case BaseEvent.EventType.MENU:
                handleMenuEvent(event);
                break;
            case BaseEvent.EventType.COMMUNITY_DETAIL:
                Bundle data1 = event.getMenuEvent().getBundle();
                activity.changeFragmentBundle(BaseFragment.FragmentEnums.COMMUNITY_DETAIL, true, data1);
                break;

            case BaseEvent.EventType.LIST_USER_VIEW:
                Bundle data = event.getMenuEvent().getBundle();
                activity.changeFragmentBundle(BaseFragment.FragmentEnums.LIST_USER_VIEW, true, data);
                break;
            case BaseEvent.EventType.ADD_COMMUNITY:
                activity.changeFragment(new AdddCommunityFragment(), true, "");
                break;
            case BaseEvent.EventType.EDIT_COMMUNITY:
                Bundle bundle = event.getMenuEvent().getBundle();
                activity.changeFragmentBundle(BaseFragment.FragmentEnums.EDIT_COMMUNITY, true, bundle);
                break; case BaseEvent.EventType.CHANGE_PASS:
                Bundle bundle1 = event.getMenuEvent().getBundle();
                activity.changeFragmentBundle(BaseFragment.FragmentEnums.CHANGE_PASS, true, bundle1);
                break;
            case BaseEvent.EventType.LOGOUT:
                gotoLogin();
                break;
        }
    }

    private void handleMenuEvent(BaseEvent event) {
        if (!(event instanceof MenuEvent)) {
            return;
        }
        MenuItem menuItem = ((MenuEvent) event).getMenuItem();
        Bundle data = ((MenuEvent) event).getBundle();
        switch (menuItem.getId()) {
            case Statics.MENU.COMMUNITY:
                activity.changeFragmentBundle(BaseFragment.FragmentEnums.LIST_COMUNITY, false, data);
                break;
        }
    }

    private void gotoLogin() {
        PreferenceUtilities preferenceUtilities = CrewBoardApplication.getInstance().getPreferenceUtilities();
        preferenceUtilities.setCurrentMobileSessionId("");
        preferenceUtilities.setCurrentCompanyNo(0);
        preferenceUtilities.setCurrentUserID("");
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
    }
}
