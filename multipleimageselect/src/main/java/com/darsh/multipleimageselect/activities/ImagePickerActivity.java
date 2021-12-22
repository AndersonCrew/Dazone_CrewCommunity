package com.darsh.multipleimageselect.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.darsh.multipleimageselect.R;
import com.darsh.multipleimageselect.fragment.AlbumFragment;
import com.darsh.multipleimageselect.fragment.BaseFragment;
import com.darsh.multipleimageselect.helpers.Constants;

/**
 * Created by mb on 5/10/16.
 */
public class ImagePickerActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private TextView tvTitle;
    private TextView tvOptionMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvOptionMenu = (TextView) findViewById(R.id.headerText);

        actionBar = getSupportActionBar();
        if (actionBar != null && toolbar != null) {
            toolbar.setContentInsetsAbsolute(0, 0);
//            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.multiple_image_select_toolbarPrimaryText));
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.mipmap.back_white);

            actionBar.setDisplayShowTitleEnabled(false);
//            actionBar.setTitle(R.string.album_view);
        }
        tvTitle.setText(R.string.album_view);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Constants.limit = intent.getIntExtra(Constants.INTENT_EXTRA_LIMIT, Constants.DEFAULT_LIMIT);
        changeFragment(new AlbumFragment(), false, AlbumFragment.class.getSimpleName());


        findViewById(R.id.leftText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setTitle(int title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    public void setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    private BaseFragment currentFragment;

    public void changeFragment(BaseFragment fragment, boolean addBackStack,
                               String name) {
        if (fragment == null) {
            return;
        }
        currentFragment = fragment;

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!addBackStack) {
            //clear stack
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            for (int i = 0; i < count; ++i) {
                fm.popBackStackImmediate();
            }
        }
        transaction.replace(R.id.layout_content, fragment, name);

        if (addBackStack) {
            transaction.addToBackStack(name);
        }

        transaction.commitAllowingStateLoss();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (currentFragment != null) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }

            default: {
                return false;
            }
        }
    }

    public void showOptionMenu(String text, final BaseFragment.OnOptionMenuClickListener onOptionMenuClickListener) {
        if (tvOptionMenu == null) {
            return;
        }
        if (TextUtils.isEmpty(text)) {
            tvOptionMenu.setVisibility(View.GONE);
            return;
        }
        tvOptionMenu.setText(text);
        tvOptionMenu.setVisibility(View.VISIBLE);
        tvOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOptionMenuClickListener != null) {
                    onOptionMenuClickListener.onClicked();
                }
            }
        });
    }

}
