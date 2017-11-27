package com.example.drinksafe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class Voice extends Activity{
    protected static final int RESULT_SPEECH = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice);

        Button start = (Button)findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
                startActivityForResult(i, RESULT_SPEECH);
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (text.get(0).equals("drink a lot") || text.get(0).equals("나 안 취했어")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Result")
                            .setMessage("Voice Recognition Success(Result is "+text.get(0)+")")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Voice.this, Gyro.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Voice.this, Gyro.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Result")
                            .setMessage("Voice Recognition Fail(Result is "+text.get(0)+")")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                break;
            }

        }
    }
}
