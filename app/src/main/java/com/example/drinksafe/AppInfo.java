package com.example.drinksafe;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by MoonKyuTae on 2017-11-26.
 */

public class AppInfo {
    public Drawable icon;
    public String applicationName;

    public AppInfo(){
        super();
    }

    public AppInfo(Drawable icon, String applicationName){
        super();
        this.icon = icon;
        this.applicationName = applicationName;
    }
}
