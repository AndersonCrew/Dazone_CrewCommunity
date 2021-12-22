package com.crewcloud.apps.crewboard.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.crewcloud.apps.crewboard.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;


/**
 * Created by mb on 8/4/16.
 */

public class BitmapUtil {

    public static Bitmap drawAptCircle(Context context, int number, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        paint.setColor(Color.WHITE);
        canvas.drawCircle((float) width / 2, (float) height / 2, (float) Math.min(width, height) / 2, paint);

        paint.setColor(ContextCompat.getColor(context, R.color.blue));
        canvas.drawCircle((float) width / 2, (float) height / 2, (float) Math.min(width, height) / 2 - Util.convertDpToPixel(3, context), paint);

        String text = String.valueOf(number);
        Rect rect = new Rect();
        paint.setColor(Color.WHITE);
        paint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.default_text_size_big));
        paint.getTextBounds(text, 0, text.length(), rect);
        canvas.drawText(text, (float) width / 2 - (float) rect.width() / 2, (float) height / 2 + (float) rect.height() / 2, paint);
        canvas.save();
        return bitmap;
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(view.getLayoutParams().width, ViewGroup.LayoutParams.WRAP_CONTENT);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);
        return bitmap;
    }

    public static String compress(Context context, String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return "Cannot insert Photo";
            }
            File file = new File(path);
            if (!file.exists()) {
                Log.d(TAG,"Image file not exist");
                return "Cannot insert Photo";
            }
            long sizeO = file.length() / 1024;
            Log.d(TAG,"Original Size: " + sizeO + " KB");
            if (sizeO >= Util.getAvailableMemory(context)) {
                Log.d(TAG,"Out of memory");
                return "Out of memory. Can\'t insert more photo!";
            }
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap == null) {
                Log.d(TAG,"Decode image file failed");
                return "Cannot insert Photo";
            }

            bitmap = getRotatedBitmap(path, bitmap);
            if (bitmap == null) {
                Log.d(TAG,"Rotate bitmap failed");
                return "Cannot insert Photo";
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            long maxSize = 1024 * 10;
            long size = bytes.size() / 1024;
            Log.d(TAG,"Compressed Size: " + size + " KB");
            if (size > maxSize) {
                return context.getString(R.string.max_photo_size);
            }

            OutputStream outputStream = new FileOutputStream(file);
            bytes.writeTo(outputStream);
            bytes.flush();
            bytes.close();
            return "";
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
            return "Cannot insert photo";
        }
    }

    public static Bitmap getRotatedBitmap(String path, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Matrix m = new Matrix();
        ExifInterface exif = null;
        int orientation = 1;

        try {
            if (path != null) {
                // Getting Exif information of the file
                exif = new ExifInterface(path);
            }
            if (exif != null) {
                orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        m.preRotate(270);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        m.preRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        m.preRotate(180);
                        break;
                    default:
                        return bitmap;
                }
                // Rotates the image according to the orientation
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            }
        } catch (IOException e) {
            Log.e(TAG,e.getMessage());
        }

        return null;
    }

    public static Bitmap drawableToBitmap(Drawable drawable, Context context, int width, int height) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

//        int width = drawable.getIntrinsicWidth();
//        width = width > 0 ? width : 90;
//        int height = drawable.getIntrinsicHeight();
//        height = height > 0 ? height : 90;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDensity((int) Util.getDisplayDensity(context));
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static String saveBitmap(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
        }
        return file.getAbsolutePath();
    }


    public static Bitmap decodeSampledBitmapFromResource(String path, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void drawTextAndBreakLine(final Canvas canvas, final Paint paint,
                                            final float x, final float y, final float maxWidth, final String text) {
        String textToDisplay = text;
        String tempText = "";
        char[] chars;
        float textHeight = paint.descent() - paint.ascent();
        float lastY = y;
        int nextPos = 0;
        int lengthBeforeBreak = textToDisplay.length();
        do {
            lengthBeforeBreak = textToDisplay.length();
            chars = textToDisplay.toCharArray();
            nextPos = paint.breakText(chars, 0, chars.length, maxWidth, null);
            tempText = textToDisplay.substring(0, nextPos);
            textToDisplay = textToDisplay.substring(nextPos, textToDisplay.length());
            canvas.drawText(tempText, x, lastY, paint);
            lastY += textHeight;
        } while(nextPos < lengthBeforeBreak);
    }
}
