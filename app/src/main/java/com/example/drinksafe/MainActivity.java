package com.example.drinksafe;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.ArrayList;

public class MainActivity extends Activity {
    final int CONTEXT_MENU_VIEW = 1;
    final int CONTEXT_MENU_EDIT = 2;
    ProgressBar progressBar;
    private static final int MILLISINFUTURE = 11*1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;
    public static long[] check = null;
    public static String phone = "";
    public static String[] nameList;
    private TextView countTxt;
    private CountDownTimer countDownTimer;
    public static ArrayList<String> checkedAppList;
    public static ArrayList<Integer> checkedAppIndexList;
    public static boolean flag = false;
    private int size;
    private int appSize;
    public static Button lock;
    public static Button unlock;
    String Str;
    int hour,minute,second;
    Notification.Builder builder;
    PendingIntent contentIntent;
    public static Ringtone r;

    @Override
    protected void onStart() {
        super.onStart();
        builder = new Notification.Builder(MainActivity.this);
        contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("DrinkSafe")
                .setContentText("Click to go to the app")
                .setContentIntent(contentIntent)
                .setAutoCancel(false);
        Notification notification = builder.getNotification();
        notification.flags |= Notification.FLAG_NO_CLEAR
                | Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadSharedPreferences();
        Typeface tf = Typeface.createFromAsset(getAssets(),"digital-7.ttf");
        if(countTxt==null) countTxt = (TextView)findViewById(R.id.count_txt);
        countTxt.setTypeface(tf);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        lock = (Button)findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar)findViewById(R.id.progressBar);
                switch (v.getId()) {
                    case R.id.lock:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                }
                if(countDownTimer==null) {
                    countDownTimer();
                    countDownTimer.start();
                }
                flag = true;
                lock.setEnabled(false);
                unlock.setEnabled(true);
                Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                Toast.makeText(MainActivity.this, "You have to check accesiibility permission!!!",Toast.LENGTH_LONG).show();
                startActivityForResult(intent, 0);
            }
        });

        unlock = (Button)findViewById(R.id.unlock);
        unlock.setEnabled(false);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Voice.class);
                startActivity(intent);
            }
        });

        final Button setting = (Button)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(setting);
                openContextMenu(setting);
            }
        });
    }

    public void loadSharedPreferences(){
        SharedPreferences preferences = this.getSharedPreferences("check",0);
        int size = preferences.getInt("check_size",0);
        check = new long[size];
        for (int i =0; i<size; i++){
            check[i]=preferences.getLong("check_"+i, 0);
        }
        phone = preferences.getString("phone",null);
        int appSize = preferences.getInt("app_size", 0);
        checkedAppIndexList = new ArrayList<>();
        checkedAppList = new ArrayList<>();
        for (int i = 0; i < appSize; i++) {
            checkedAppList.add(preferences.getString("appName_" + i, null));
            checkedAppIndexList.add(preferences.getInt("appIndex_" + i, 0));
        }
        Gson gson = new Gson();
        String json = preferences.getString("MyObject", "");
        countDownTimer = gson.fromJson(json, CountDownTimer.class);
    }

    public void countDownTimer(){
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                sum(millisUntilFinished);
                Str = String.format("%02d: %02d: %02d",hour,minute,second);
                builder.setContentText(Str);
                countTxt.setText(Str);
            }
            public void onFinish() {
                countTxt.setText(String.valueOf("Finish ."));
                progressBar.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, Voice.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                try {
                    final Vibrator vibrator= (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    long[] pattern = {100,300,100,700,300,2000};
                    vibrator.vibrate(pattern,-1);
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.setStreamType(AudioManager.STREAM_ALARM);
                    r.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        };
    }

    public void sum(long millisUntilFinished){
        hour = (int) millisUntilFinished/3600/1000;
        minute = ((int) millisUntilFinished - hour*3600*1000)/60/1000;
        second = ((int) millisUntilFinished - hour*3600*1000 - minute*60*1000)/1000;
    }

    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences = this.getSharedPreferences("check",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("check_size", check.length);
        for(int i=0;i<check.length;i++){
            editor.putLong("check_"+i,check[i]);
        }
        editor.putString("phone",phone);
        editor.putInt("app_size", checkedAppList.size());
        for(int i=0;i<checkedAppIndexList.size();i++){
            editor.putString("appName_"+i,checkedAppList.get(i));
            editor.putInt("appIndex_"+i,checkedAppIndexList.get(i));
        }
        Gson gson = new Gson();
        String json = gson.toJson(countDownTimer);
        editor.putString("MyObject", json);
        editor.commit();
    }

    public void onCreateContextMenu (ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo){
        menu.setHeaderTitle("Setting");
        menu.add(Menu.NONE, CONTEXT_MENU_VIEW, Menu.NONE, "Guardians List");
        menu.add(Menu.NONE, CONTEXT_MENU_EDIT, Menu.NONE, "Blocking Apps List");
    }

    public boolean onContextItemSelected (MenuItem item){
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case CONTEXT_MENU_VIEW: {
                Intent intent = new Intent(MainActivity.this, GuardianList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            break;
            case CONTEXT_MENU_EDIT: {
                Intent intent = new Intent(MainActivity.this, BlockingAppList.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
            break;
        }
        return super.onContextItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState){
        outState.putLongArray("checked",check);
        super.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        check=savedInstanceState.getLongArray("checked");
    }
}
