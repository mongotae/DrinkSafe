package com.example.drinksafe;

/**
 * Created by MoonKyuTae on 2017-11-28.
 */

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.ArrayList;

/**
 * Created by 2nd_user on 2016-04-10.
 */
public class WindowChangeDetectingService extends AccessibilityService {
    MainActivity mc;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(mc.flag==true && mc.checkedAppList!=null) {
            ArrayList<String> applist = new ArrayList<>();
            applist = mc.checkedAppList;
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && applist != null) {
                for (String app : applist) {
                    if (app.equals(event.getPackageName())) {
                        gotoHome();
                    }
                    break;
                }
            }
        }
//        if(mc.flag==false){
//            disableSelf();
//        }
    }

    private void gotoHome() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_FORWARD_RESULT
                | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
    }

    @Override
    public void onInterrupt() {

    }
}

