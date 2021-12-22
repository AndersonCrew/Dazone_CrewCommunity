package com.darsh.multipleimageselect;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by mb on 5/13/16.
 */
public class Util {
    public static String getRealPathFromURI(Activity activity, Uri uri) {
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            return "";
        }
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        String path = cursor.getString(idx);
        cursor.close();
        return path;
    }

    public static Uri insertImage(ContentResolver cr, String imagePath,
                                  String name, String description) throws FileNotFoundException {
        // Check if file exists with a FileInputStream
        FileInputStream stream = new FileInputStream(imagePath);
        try {
            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            Uri ret = insertImage(cr, bm, name, description);
            bm.recycle();
            return ret;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
            }
        }
    }

    public static Uri insertImage(ContentResolver cr, Bitmap source,
                                  String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        Uri url = null;
        String stringUrl = null;    /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null && url != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 60, imageOut);
                } catch (Exception e) {
                    Log.e("E", e.toString());
                } finally {
                    if (imageOut != null) {
                        imageOut.close();
                    }
                }

            } else {

                if (url != null) {
                    cr.delete(url, null, null);
                    url = null;
                }
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return Uri.parse(stringUrl);
    }
}
