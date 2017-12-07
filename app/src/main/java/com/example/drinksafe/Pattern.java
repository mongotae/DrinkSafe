package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.amnix.materiallockview.MaterialLockView;
import java.util.List;

public class Pattern extends Activity {
    private String CorrectPattern = "123";
    private MaterialLockView materialLockView;
    private String[] key = {"16748", "248651", "321478965", "47852369", "51832496","67485923", "748596321", "87692451","94381672"};
    private int ran;
    private boolean passFlag = false;
    MainActivity mc;
    Thread patternCheckThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern);
        materialLockView = (MaterialLockView) findViewById(R.id.pattern);
        ran = randomRange(0,key.length-1);
        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                Log.e("SimplePattern", SimplePattern);
                if (!SimplePattern.equals(CorrectPattern)) {
                    Intent i = new Intent("com.example.drinksafe.SMSmain");
                    i.putExtra("phone", mc.phone);
                    i.setPackage("com.example.drinksafe");
                    startService(i);
                    Intent intent = new Intent(Pattern.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                } else {
                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);
                    patternCheckThread.interrupt();
                    mc.flag=false;
                    if(mc.countDownTimer!=null) mc.countDownTimer.onFinish();
                    passFlag=true;
                    finish();
                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

        ((CheckBox) findViewById(R.id.stealthmode)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("checked", isChecked+"");
                materialLockView.setInStealthMode(isChecked);
            }
        });

        final TextView tv = (TextView) findViewById(R.id.correct_pattern_edittext);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CorrectPattern = tv.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tv.setText(key[ran]);
        patternCheckThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(300000);
                    Pattern.super.finish();
                } catch (Exception e) {
                }
            }
        };
        patternCheckThread.start();
    }

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(passFlag==false){
            Intent i = new Intent("com.example.drinksafe.SMSmain");
            i.putExtra("phone", mc.phone);
            i.setPackage("com.example.drinksafe");
            startService(i);
        }
    }
}