package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
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


public class MainActivity extends Activity {
    final int CONTEXT_MENU_VIEW = 1;
    final int CONTEXT_MENU_EDIT = 2;
    ProgressBar progressBar;
    private static final int MILLISINFUTURE = 11*1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;

    private int count = 10;
    private TextView countTxt ;
    private CountDownTimer countDownTimer;

    String Str;
    int hour,minute,second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button lock = (Button)findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = (ProgressBar)findViewById(R.id.progressBar);
                switch (v.getId()) {
                    case R.id.lock:
                        progressBar.setVisibility(View.VISIBLE);       // 버튼 클릭시 원진행 바가 보이게 함
                        break;
                }
                //타이머 실행
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
    }
    public void countDownTimer(){
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                count --;
                sum();
                Str = String.format("%02d시간 %02d분 %02d초",hour,minute,second);
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
    }
    public void onCreateContextMenu (ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo){
        //Context menu
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
}
