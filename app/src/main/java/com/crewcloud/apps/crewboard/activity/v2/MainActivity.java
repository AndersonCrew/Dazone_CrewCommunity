package com.crewcloud.apps.crewboard.activity.v2;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.activity.logintest.Statics;
import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseEvent;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.event.EventGroupList;
import com.crewcloud.apps.crewboard.event.EventHandler;
import com.crewcloud.apps.crewboard.event.MenuEvent;
import com.crewcloud.apps.crewboard.event.MenuItem;
import com.crewcloud.apps.crewboard.fragment.AdddCommunityFragment;
import com.crewcloud.apps.crewboard.fragment.ChangePasswordFragment;
import com.crewcloud.apps.crewboard.fragment.CommunityDetailFragment;
import com.crewcloud.apps.crewboard.fragment.CommunityFragment;
import com.crewcloud.apps.crewboard.fragment.ListUserViewFragment;
import com.crewcloud.apps.crewboard.fragment.ProfileFragment;
import com.crewcloud.apps.crewboard.fragment.ProfileFragmentV2;
import com.crewcloud.apps.crewboard.fragment.SettingFragment;
import com.crewcloud.apps.crewboard.listener.OnClickOptionMenu;
import com.crewcloud.apps.crewboard.module.leftmenu.LeftMenuPresenter;
import com.crewcloud.apps.crewboard.module.leftmenu.LeftMenuPresenterImp;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;
import com.crewcloud.apps.crewboard.util.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

/**
 * Created by dazone on 2/20/2017.
 */

public class MainActivity extends BaseActivity implements LeftMenuPresenter.view {

    private BaseFragment currentFragment;
    @Bind(R.id.lv_menu)
    RecyclerView lvMenu;

    @Bind(R.id.left_menu_cb_check)
    CheckBox cbCheck;

    private LeftMenuAdapterV2 leftMenuAdapter;
    private LeftMenuPresenterImp leftMenuPresenterImp;

    @Bind(R.id.tv_title)
    TextView tvTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ic_left)
    ImageView imvLeft;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Bind(R.id.left_menu)
    View leftMenuLayout;

    @Bind(R.id.option_menu)
    TextView tvOptionMenu;

    @Bind(R.id.imv_option_menu)
    ImageView imvOptionMenu;

    @Bind(R.id.imv_logo)
    ImageView imvActionbarLogo;

    @Bind(R.id.avatar)
    CircleImageView avatar;

    @Bind(R.id.user_name)
    TextView user_name;

    @Bind(R.id.email)
    TextView email;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    private int leftIcon;
    private EventHandler eventHandler;

    private List<ChildFolders> lstLeftMenu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_v2);
        ButterKnife.bind(this);
        setupToolbar();
        changeFragment(new CommunityFragment(), false, CommunityFragment.class.getSimpleName());
        EventBus.getDefault().register(this);
        eventHandler = new EventHandler(this);

        leftMenuPresenterImp = new LeftMenuPresenterImp(this);
        leftMenuPresenterImp.attachView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = lvMenu.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        lvMenu.setLayoutManager(layoutManager);
        leftMenuAdapter = new LeftMenuAdapterV2(this);
        lvMenu.setAdapter(leftMenuAdapter);
        leftMenuAdapter.setMenuClickGroup(new LeftMenuAdapterV2.onItemClickListener() {
            @Override
            public void onClickItemGroup(ChildFolders childBoards) {
                if (childBoards.getName().equals("All")) {
                    drawerLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            if (drawerLayout != null) {
                                drawerLayout.closeDrawer(Gravity.LEFT);
                            }
                        }
                    });


                    Bundle bundle = new Bundle();
                    bundle.putString("NAME_ALL", childBoards.getName());
                    bundle.putParcelable("OBJECT", childBoards);
                    MenuEvent menuEvent = new MenuEvent(BaseEvent.EventType.MENU);
                    MenuItem item = new MenuItem(Statics.MENU.COMMUNITY);
                    menuEvent.setMenuItem(item);
                    menuEvent.setBundle(bundle);
                    EventBus.getDefault().post(menuEvent);
                }
            }
        });

        initView();

        leftMenuPresenterImp.getLeftMenu();


    }

    private void initView() {
        String ava = CrewBoardApplication.getInstance().getmPrefs().getAvatar();
        String name = CrewBoardApplication.getInstance().getmPrefs().getUserName();
        String email = CrewBoardApplication.getInstance().getmPrefs().getMailAddress();
        if (!TextUtils.isEmpty(ava)) {
            Picasso.with(this).load(CrewBoardApplication.getInstance().getmPrefs().getServerSite() + ava)
                    .placeholder(R.mipmap.photo_placeholder)
                    .error(R.mipmap.photo_placeholder)
                    .into(avatar);
        }
        this.user_name.setText(name);
        this.email.setText(email);

        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbCheck.setChecked(isChecked);
                drawerLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if (drawerLayout != null) {
                            drawerLayout.closeDrawer(Gravity.LEFT);
                        }
                    }
                });
            }
        });
    }


    @Subscribe
    public void onEvent(BaseEvent event) {
        if (event.getType() == BaseEvent.EventType.MENU) {
//            leftDrawer.highlightMenu(((MenuEvent) event).getMenuItem().getId());
        }
        if (event.getType() == BaseEvent.EventType.LOCK) {
            if (event.isLock()) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

        }
        eventHandler.onEvent(event);
    }

    @Subscribe
    public void onEventChild(EventGroupList event) {
        Log.d("Tag", event.toString());
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                if (drawerLayout != null) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        });


        Bundle bundle = new Bundle();
        bundle.putParcelable("OBJECT", event.getChildBoards());
        MenuEvent menuEvent = new MenuEvent(BaseEvent.EventType.MENU);
        MenuItem item = new MenuItem(Statics.MENU.COMMUNITY);
        menuEvent.setMenuItem(item);
        menuEvent.setBundle(bundle);
        EventBus.getDefault().post(menuEvent);


    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        tvTitle.setSelected(true);
    }


    public void changeFragmentBundle(BaseFragment.FragmentEnums tag, boolean addBackStack, Bundle bundle) {
        BaseFragment fragment = null;
        switch (tag) {
            case LIST_COMUNITY:
                fragment = CommunityFragment.newInstance(bundle);
                break;
            case COMMUNITY_DETAIL:
                fragment = CommunityDetailFragment.newInstance(bundle);
                break;
            case LIST_USER_VIEW:
                fragment = ListUserViewFragment.newInstance(bundle);
                break;
            case EDIT_COMMUNITY:
                fragment = AdddCommunityFragment.newInstance(bundle);
                break;
            case CHANGE_PASS:
                fragment = ChangePasswordFragment.newInstance(bundle);
                break;
        }
        if (fragment != null) {
            changeFragment(fragment, addBackStack, tag.getFragmentName());
        }
    }

    @Override
    public void changeFragment(BaseFragment fragment, boolean addBackStack,
                               String name) {
        if (fragment == null) {
            return;
        }
        currentFragment = fragment;

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!addBackStack) {
            setLeftMenu(R.drawable.ic_menu_white_24dp);
            //clear stack
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }
        }
        transaction.replace(R.id.frame_container, fragment, name);

        if (addBackStack) {
            setLeftMenu(R.drawable.ic_keyboard_backspace_white_24dp);
            transaction.addToBackStack(name);
        }

        transaction.commitAllowingStateLoss();

    }

    public void setLeftMenu(int drawable) {
        leftIcon = drawable;
        imvLeft.setImageResource(leftIcon);
        switch (leftIcon) {
            case R.drawable.ic_keyboard_backspace_white_24dp:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
                break;
            case R.drawable.ic_menu_white_24dp:
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        Util.hideKeyboard(this);
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (currentFragment != null && currentFragment.onBackPressed()) {
            Log.d("BACK", "Fragment back pressed");
        } else {
//            if (getSupportFragmentManager().getBackStackEntryCount() == 0 && !(currentFragment instanceof NoticeFragment)) {
//                changeFragment(new NoticeFragment(), false, NoticeFragment.class.getSimpleName());
//                return;
//            } else
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                currentFragment = getActiveFragment();
            }
            super.onBackPressed();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                setLeftMenu(R.drawable.ic_menu_white_24dp);
            }
        }
    }

    @Override
    public void setShowSearchIcon(boolean isShow) {
        if (isShow) {
            imvOptionMenu.setVisibility(View.VISIBLE);
        } else {
            imvOptionMenu.setVisibility(View.GONE);
        }
    }

    @Override
    public void setShowActionbarLogo(boolean isShow) {
        if (imvActionbarLogo != null && tvTitle != null) {
            imvActionbarLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
            tvTitle.setVisibility(!isShow ? View.VISIBLE : View.GONE);
        }
    }

    public BaseFragment getActiveFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void setTitle(String title) {
        if (tvTitle != null && title != null) {
            tvTitle.setText(title);
        }
    }

    @OnClick(R.id.ic_left)
    void onClickLeftIcon() {
        switch (leftIcon) {
            case R.drawable.ic_keyboard_backspace_white_24dp:
                onBackPressed();
                break;
            case R.drawable.ic_menu_white_24dp:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
    }

    @OnClick(R.id.option_menu)
    void onClickOptionMenu(View view) {
        if (currentFragment != null) {
            currentFragment.onOptionMenuClick(view);
        }
    }

    @Override
    public void setOptionMenu(String name, final OnClickOptionMenu listener) {
        if (tvOptionMenu == null) {
            return;
        }

        if (TextUtils.isEmpty(name)) {
            name = "";
            tvOptionMenu.setOnClickListener(null);
            tvOptionMenu.setVisibility(View.GONE);
        } else {
            tvOptionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
            });
            tvOptionMenu.setVisibility(View.VISIBLE);
        }
        tvOptionMenu.setText(name);
    }

    @Override
    public void setOptionMenu(int icon, final OnClickOptionMenu listener) {
        if (imvOptionMenu == null) {
            return;
        }
        if (icon != 0) {
            imvOptionMenu.setVisibility(View.VISIBLE);
            imvOptionMenu.setImageResource(icon);
            imvOptionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
            });
        } else {
            imvOptionMenu.setVisibility(View.GONE);
            imvOptionMenu.setImageDrawable(null);
            imvOptionMenu.setOnClickListener(null);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        ButterKnife.unbind(this);
        leftMenuPresenterImp.detachView();
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.fab)
    public void onClickFab() {
        changeFragment(new AdddCommunityFragment(), true, "");
    }

    @Override
    public void setActionFloat(boolean isShow) {
        if (!isShow) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.setting_drawer)
    void onClickSetting() {
        changeFragment(new SettingFragment(), true, getString(R.string.profle));
    }

    @Override
    public void onGetLeftMenuSuccess(final List<ChildFolders> leftMenus, boolean isCached) {
        ChildFolders childFolders = new ChildFolders();
        childFolders.setName("All");
        childFolders.setFolderNo(1);
        leftMenus.add(0, childFolders);

        leftMenuAdapter.addAll(leftMenus);
//        if (!isCached) {
//            getRealm().executeTransaction(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.where(ChildFolders.class).findAll().deleteAllFromRealm();
//                }
//            });
//        }

    }

    @Override
    public void onError(String message) {
        if (!message.isEmpty())
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }
}
