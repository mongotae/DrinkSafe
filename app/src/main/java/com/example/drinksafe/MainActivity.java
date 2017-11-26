package com.example.drinksafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends Activity {
    final int CONTEXT_MENU_VIEW = 1;
    final int CONTEXT_MENU_EDIT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button lock = (Button)findViewById(R.id.lock);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(){
//
//                }else{
//
//                }
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
