package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by MoonKyuTae on 2017-11-20.
 */

public class Splash extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(3000);
                } catch (Exception e) {
                } finally {
                    Intent i = new Intent(Splash.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
