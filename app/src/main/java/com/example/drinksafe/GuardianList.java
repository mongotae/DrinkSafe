package com.example.drinksafe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class GuardianList extends ListActivity implements android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback {
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
                AlertDialog.Builder builder = new AlertDialog.Builder(GuardianList.this,0);
                builder.setTitle("Test Alert Dialog")
                .setMessage("READ_CONTACTS permission is needed to complete the request! would you try to get permission again?")
                .setNegativeButton("NO",null)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                        ActivityCompat.requestPermissions(GuardianList.this, new String[]{Manifest.permission.READ_CONTACTS},123);
                        }
                });
        builder.show();
        }
        else
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},123);
        }
        else{
                String columns[] = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.STARRED,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        String disp[] = new String[]{
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER};

        int[] colResIds = new int[]{R.id.name};

        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
        columns,ContactsContract.Contacts.STARRED+"=0",
        null,null);
        setListAdapter(new SimpleCursorAdapter(this,R.layout.guardian_list,c,disp,colResIds,0));
        }

        }
public void onRequestPermissionResult(int requestCode,String permission[], int[] grantResults){
        if(requestCode == 123){
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        String columns[] = new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.Contacts.STARRED,
                        ContactsContract.Contacts.HAS_PHONE_NUMBER
                        };

        String disp[] = new String[]{
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.HAS_PHONE_NUMBER};

        int[] colResIds = new int[]{R.id.name};

        ContentResolver cr = getContentResolver();
        Cursor c = cr.query(ContactsContract.Contacts.CONTENT_URI,
        columns,ContactsContract.Contacts.STARRED+"=0",
        null,null);
        setListAdapter(new SimpleCursorAdapter(this,R.layout.guardian_list,c,disp,colResIds,0));
        }
        }
        }
}
