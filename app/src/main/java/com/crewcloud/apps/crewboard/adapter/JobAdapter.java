package com.crewcloud.apps.crewboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseAdapter;
import com.crewcloud.apps.crewboard.dtos.ChildBoards;
import com.crewcloud.apps.crewboard.dtos.ChildFolders;
import com.crewcloud.apps.crewboard.dtos.Jobs;

import java.util.List;

/**
 * Created by dazone on 2/27/2017.
 */

public class JobAdapter extends ArrayAdapter<ChildBoards> {

    private Context context;
    private List<ChildBoards> values;

    public JobAdapter(Context context, int textViewResourceId,
                      List<ChildBoards> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    public ChildBoards getItem(int position) {
        return values.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setText(values.get(position).getName());

        return label;
    }

    // And here is when the "chooser" is popped up
   // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setTextSize(18);
        label.setGravity(Gravity.CENTER_VERTICAL);
        label.setText(values.get(position).getName());

        return label;
    }
}
