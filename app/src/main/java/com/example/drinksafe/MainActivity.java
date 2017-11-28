package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private int count = 10;
    private TextView countTxt ;
    private CountDownTimer countDownTimer;
    public static ArrayList<String> checkedAppList;
    public static ArrayList<Integer> checkedAppIndexList;

    String Str;
    int hour,minute,second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Button lock = (Button)findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar)findViewById(R.id.progressBar);
                switch (v.getId()) {
                    case R.id.lock:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                }
                countTxt = (TextView)findViewById(R.id.count_txt);
                countDownTimer();
                countDownTimer.start();
            }
        });

        Button unlock = (Button)findViewById(R.id.unlock);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test.class);
                startActivity(intent);
            }
        });

        final ImageButton setting = (ImageButton)findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(setting);
                openContextMenu(setting);
            }
        });

        Button sms = (Button)findViewById(R.id.sms);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SMSmain.class);

                i.putExtra("phone", phone);
                startActivity(i);
            }
        });

        Button block = (Button)findViewById(R.id.Blocking);
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Block.class);
                startActivity(i);
            }
        });
    }
    public void countDownTimer(){
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
                sum();
                Str = String.format("%02d: %02d: %02d",hour,minute,second);
                countTxt.setText(Str);

            }
            public void onFinish() {
                count=10;
                countTxt.setText(String.valueOf("Finish ."));
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
    }
    public void sum(){
        hour =  count/3600;
        minute = (count%3600)/60;
        second = (count%3600)%60;
    }

    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;

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
            }
            break;
            case CONTEXT_MENU_EDIT: {
                Intent intent = new Intent(MainActivity.this, BlockingAppList.class);
                startActivity(intent);
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
