package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by MoonKyuTae on 2017-11-28.
 */

public class Block extends Activity {
    Intent intent2;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.block);

        Button b1 = (Button)findViewById(R.id.blockStart);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent2 = new Intent(getApplicationContext(), BlockService.class);
                intent2.setPackage("com.example.drinksafe");
                startService(intent2);
            }
        });

        Button b2 = (Button)findViewById(R.id.BlockStop);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent2);
            }
        });

    }
}
