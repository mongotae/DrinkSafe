package com.example.drinksafe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.amnix.materiallockview.MaterialLockView;

import java.util.List;

public class Pattern extends AppCompatActivity {
    private String CorrectPattern = "123";
    private MaterialLockView materialLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern);
        materialLockView = (MaterialLockView) findViewById(R.id.pattern);

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                Log.e("SimplePattern", SimplePattern);
                if (!SimplePattern.equals(CorrectPattern)) {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    materialLockView.clearPattern();

                } else {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);
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

        ((EditText) findViewById(R.id.correct_pattern_edittext)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CorrectPattern = "" + s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}