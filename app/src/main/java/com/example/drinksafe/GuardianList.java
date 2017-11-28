package com.example.drinksafe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class GuardianList extends ListActivity {
        String tag = "cap";
        String [] name;
        public static String[] new_name;
        String phone;
        int count = 0;
        ListView listView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Cursor cursor = getURI();
            int end = cursor.getCount();
            name = new String[end];

            String[] bbStr = cursor.getColumnNames();
            for (int i = 0; i < bbStr.length; i++)
                    Log.e(tag, "ColumnName " + i + " : " + cursor.getColumnName(i));

            if (cursor.moveToFirst()) {
                    do {
                            if (!cursor.getString(2).startsWith("01"))
                                    continue;
                            // 요소값 얻기
                            name[count] = cursor.getString(1);
                            name[count] += "\n";
                            name[count] += cursor.getString(2);
                            count++;
                    } while (cursor.moveToNext());

                    new_name = new String[count];
                    for (int i = 0; i < count; i++) new_name[i] = name[i];
            }
            cursor.close();
            setListAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice,
                    new_name));
            listView = getListView();
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            if(MainActivity.check!=null) {
                    for (int i = 0; i < MainActivity.check.length; i++) {
                            listView.setItemChecked((int) MainActivity.check[i], true);
                    }
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            long num[] = listView.getCheckItemIds();
                            MainActivity.check= num;
                        phone="";
                        if(num.length>1){
                            for(int k =0; k<=num.length-2;k++){
                                phone = phone+"+82"+name[(int) num[k]].split("\n")[1].substring(1)+",";
                            }
                            phone=phone+"+82"+name[(int) num[num.length-1]].split("\n")[1].substring(1);
                        }else{
                            phone="+82"+name[(int) num[0]].split("\n")[1].substring(1);
                        }
                            MainActivity.phone=phone;
                    }
            });
        }
        private Cursor getURI() {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String[] projection = new String[] {
                        ContactsContract.Contacts._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
                return managedQuery(uri, projection, null, null, sortOrder);
        }
}
