package com.example.drinksafe;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class GuardianList extends ListActivity {
        String tag = "cap";
        String [] name, new_name;
        int count = 0;
        ListView listView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
                {
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
                } else {
                        Cursor cursor = getURI();  // 전화번호부 가져오기
                        int end = cursor.getCount(); // 전화번호부의 갯수 세기
                        name = new String[end];   // 전화번호부의 이름을 저장할 배열 선언

                        String[] bbStr = cursor.getColumnNames();
                        for (int i = 0; i < bbStr.length; i++)
                                // 각각의 컬럼 이름 확인
                                Log.e(tag, "ColumnName " + i + " : " + cursor.getColumnName(i));

                        if (cursor.moveToFirst()) {    //항상 처음에서 시작
                                do {
                                        if (!cursor.getString(2).startsWith("01")) // 01로 시작하는 핸펀만
                                                continue;         // 이멜주소만 있는것은 제외
                                        // 요소값 얻기
                                        name[count] = cursor.getString(1);  //이름
                                        name[count] += "\n";
                                        name[count] += cursor.getString(2);  //전번
                                        count++;
                                } while (cursor.moveToNext());

                                new_name = new String[count];    //이멜주소 제외한 01번호 가져오기
                                for (int i = 0; i < count; i++) new_name[i] = name[i];  //복사
                        }
                        cursor.close(); // 반드시 커서 닫고
                        setListAdapter(new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_multiple_choice, // 멀티 쵸이스
                                new_name));
                        listView = getListView();         // 리스트뷰
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  // 반드시 설정해줘야 멀티초이스
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String mes = "";
                                        long num[] = listView.getCheckItemIds();  // 현재 체크된 id들의 배열 리턴
                                        // 화면표시 - 여기서 얻어온 값들 처리하심됨
                                        mes = "Selected Item : \n";
                                        for (int i = 0; i < num.length; i++) {
                                                mes += i + " : " + name[(int) num[i]];
                                                mes += "\n";
                                        }
                                }
                        });
                }
        }
        public void onRequestPermissionResult(int requestCode,String permission[], int[] grantResults) {
                if (requestCode == 123) {
                        Cursor cursor = getURI();  // 전화번호부 가져오기
                        int end = cursor.getCount(); // 전화번호부의 갯수 세기
                        name = new String[end];   // 전화번호부의 이름을 저장할 배열 선언

                        String[] bbStr = cursor.getColumnNames();
                        for (int i = 0; i < bbStr.length; i++)
                                // 각각의 컬럼 이름 확인
                                Log.e(tag, "ColumnName " + i + " : " + cursor.getColumnName(i));

                        if (cursor.moveToFirst()) {    //항상 처음에서 시작
                                do {
                                        if (!cursor.getString(2).startsWith("01")) // 01로 시작하는 핸펀만
                                                continue;         // 이멜주소만 있는것은 제외
                                        // 요소값 얻기
                                        name[count] = cursor.getString(1);  //이름
                                        name[count] += "\n";
                                        name[count] += cursor.getString(2);  //전번
                                        count++;
                                } while (cursor.moveToNext());

                                new_name = new String[count];    //이멜주소 제외한 01번호 가져오기
                                for (int i = 0; i < count; i++) new_name[i] = name[i];  //복사
                        }
                        cursor.close(); // 반드시 커서 닫고
                        setListAdapter(new ArrayAdapter<String>(this,
                                android.R.layout.simple_list_item_multiple_choice, // 멀티 쵸이스
                                new_name));
                        listView = getListView();         // 리스트뷰
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  // 반드시 설정해줘야 멀티초이스
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String mes = "";
                                        long num[] = listView.getCheckItemIds();  // 현재 체크된 id들의 배열 리턴
                                        // 화면표시 - 여기서 얻어온 값들 처리하심됨
                                        mes = "Selected Item : \n";
                                        for (int i = 0; i < num.length; i++) {
                                                mes += i + " : " + name[(int) num[i]];
                                                mes += "\n";
                                        }
                                }
                        });
                }
        }
        private Cursor getURI() {
                Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

                String[] projection = new String[] { // 세개만 프로젝션함
                        ContactsContract.Contacts._ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };
                // 정렬방식 설정
                String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
                return managedQuery(uri, projection, null, null, sortOrder);
        }
}
