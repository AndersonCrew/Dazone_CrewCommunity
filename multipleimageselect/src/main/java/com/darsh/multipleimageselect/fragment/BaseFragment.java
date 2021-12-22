package com.darsh.multipleimageselect.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.darsh.multipleimageselect.activities.ImagePickerActivity;

/**
 * Created by mb on 5/10/16.
 */
public class BaseFragment extends Fragment {
    protected ImagePickerActivity activity;
    private String optionMenu;
    private OnOptionMenuClickListener onOptionMenuClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ImagePickerActivity) getActivity();
    }

    public void showOptionMenu(String text, OnOptionMenuClickListener onOptionMenuClickListener) {
        optionMenu = text;
        this.onOptionMenuClickListener = onOptionMenuClickListener;
        activity.showOptionMenu(optionMenu, onOptionMenuClickListener);
    }

    public interface OnOptionMenuClickListener {
        void onClicked();
    }

    @Override
    public void onResume() {
        super.onResume();
        showOptionMenu(optionMenu, onOptionMenuClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.showOptionMenu(null, null);
    }
}
