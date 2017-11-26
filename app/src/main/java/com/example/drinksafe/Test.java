package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by MoonKyuTae on 2017-11-26.
 */

public class Test extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = new Intent(Test.this, Voice.class);
        startActivity(i);
        finish();
    }

}
