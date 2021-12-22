package com.darsh.multipleimageselect;

import android.app.Activity;
import android.content.Intent;

import com.darsh.multipleimageselect.activities.ImagePickerActivity;
import com.darsh.multipleimageselect.helpers.Constants;

/**
 * Created by mb on 5/10/16.
 */
public class ImagePicker {
    static volatile ImagePicker singleton = null;
    private Activity activity;

    public static ImagePicker with(Activity activity) {
        if (singleton == null) {
            synchronized (ImagePicker.class) {
                if (singleton == null) {
                    singleton = new ImagePicker();
                }
            }
        }
        singleton.activity = activity;
        return singleton;
    }

    public void startActivityForResult(int limit, int requestCode) {
        if (limit == 0) {
            limit = 1;
        }
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, limit);
        activity.startActivityForResult(intent, requestCode);
    }
}
