package com.example.drinksafe;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by MoonKyuTae on 2017-11-27.
 */

public class AppInfoAdapter extends ArrayAdapter<AppInfo> implements CompoundButton.OnCheckedChangeListener
{  SparseBooleanArray mCheckStates;
    Context context;
    AppInfo  data[] = null;
    LayoutInflater mInflater;

    public AppInfoAdapter(Context context, AppInfo[] data){
        super(context, 0,data);
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
        mCheckStates = new SparseBooleanArray(data.length);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        AppInfoHolder holder= null;

        if (row == null){
            row = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new AppInfoHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imageView1);
            holder.txtTitle = (TextView) row.findViewById(R.id.textView1);
            holder.chkSelect = (CheckBox) row.findViewById(R.id.checkBox1);
            row.setTag(holder);
        }
        else{
            holder = (AppInfoHolder)row.getTag();
        }

        AppInfo appinfo = data[position];
        holder.txtTitle.setText(appinfo.applicationName);
        holder.imgIcon.setImageDrawable(appinfo.icon);
        holder.chkSelect.setTag(position);
        holder.chkSelect.setChecked(mCheckStates.get(position, false));
        holder.chkSelect.setOnCheckedChangeListener(this);
        return row;
    }
    public boolean isChecked(int position) {
        return mCheckStates.get(position, false);
    }

    public void setChecked(int position, boolean isChecked) {
        mCheckStates.put(position, isChecked);
    }

    public void toggle(int position) {
        setChecked(position, !isChecked(position));
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {

        mCheckStates.put((Integer) buttonView.getTag(), isChecked);

    }
    static class AppInfoHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        CheckBox chkSelect;
    }
}
