package com.example.drinksafe;

import android.graphics.drawable.Drawable;

/**
 * Created by MoonKyuTae on 2017-11-26.
 */

public class AppInfo {
    public Drawable icon;
    public String applicationName;
    public String packageName;

    public AppInfo(){
        super();
    }

    public AppInfo(Drawable icon, String applicationName, String packageName){
        super();
        this.icon = icon;
        this.applicationName = applicationName;
        this.packageName = packageName;
    }
    public AppInfo(Drawable icon, String applicationName){
        super();
        this.icon = icon;
        this.applicationName = applicationName;
    }
}
