package com.example.drinksafe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class Voice extends Activity{
    protected static final int RESULT_SPEECH = 1;
    private int count=0;
    private int count2=0;
    private boolean passFlag = false;
    MainActivity mc;
    Thread voiceCheckThread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);

        Button start = (Button)findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if(count>5){
                    Intent i = new Intent("com.example.drinksafe.SMSmain");
                    i.putExtra("phone", mc.phone);
                    i.setPackage("com.example.drinksafe");
                    startService(i);
                    finish();
                }
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                startActivityForResult(i, RESULT_SPEECH);
            }
        });

        voiceCheckThread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    sleep(300000);
                    Voice.super.finish();
                } catch (Exception e) {
                }
            }
        };
        voiceCheckThread.start();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
        switch (requestCode) {
            case RESULT_SPEECH: {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (text.get(0).equals("drink a lot") || text.get(0).equals("나 안 취했어")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Result")
                            .setMessage("Voice Recognition Success(Result is " + text.get(0) + ")")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    passFlag=true;
                                    Intent i = new Intent(Voice.this, Pattern.class);
                                    startActivity(i);
                                    voiceCheckThread.interrupt();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    passFlag=true;
                                    Intent i = new Intent(Voice.this, Pattern.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Result")
                            .setMessage("Voice Recognition Fail(Result is " + text.get(0) + ")\nYou failed" + count + "times.\nOnly "+(5-count)+"times left.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    if(count>=5){
                        Intent i = new Intent("com.example.drinksafe.SMSmain");
                        i.putExtra("phone", mc.phone);
                        i.setPackage("com.example.drinksafe");
                        startService(i);
                        finish();
                    }
                }
                break;
            }

        }
        }catch (NullPointerException e){
            Toast.makeText(this, "Do not disturb recognition. Retry!", Toast.LENGTH_SHORT).show();
        }
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
