package com.crewcloud.apps.crewboard.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.crewcloud.apps.crewboard.R;
import com.crewcloud.apps.crewboard.interfaces.DefaultFontView;
import com.lb.auto_fit_textview.AutoResizeTextView;


/**
 * Created by mb on 8/9/16.
 */

public class AutoSizeTextView extends AutoResizeTextView implements DefaultFontView {

    public AutoSizeTextView(Context context) {
        super(context);
        init(null);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AutoSizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    public void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.AutoSizeTextView);
            int size = typedArray.getDimensionPixelSize(R.styleable.AutoSizeTextView_mine_text_size, 0);
            if (size > 0) {
                setMinTextSize(size);
            }
            typedArray.recycle();
        }

    }

    @Override
    public void setFont(String font) {
    }
}
