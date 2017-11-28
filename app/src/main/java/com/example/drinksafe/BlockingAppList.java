package com.example.drinksafe;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by MoonKyuTae on 2017-11-24.
 */

public class BlockingAppList extends Activity{
    static ArrayList<String> packageNm;
    static ArrayList<String> selectedPackage;
    static ArrayList<Integer> selectedPackageIndex;
    public static Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking_apps_list);
        activity=this;
        if(MainActivity.checkedAppList!=null){
            selectedPackage=MainActivity.checkedAppList;
        }else{
            selectedPackage = new ArrayList<>();
        }
        if(MainActivity.checkedAppIndexList!=null){
            selectedPackageIndex=MainActivity.checkedAppIndexList;
        }else{
            selectedPackageIndex= new ArrayList<>();
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container,
                    false);

            ApplicationInfo applicationInfo = getActivity().getApplicationInfo();
            PackageManager pm = getActivity().getPackageManager();
            List<PackageInfo> pInfo = new ArrayList<PackageInfo>();
            pInfo.addAll(pm.getInstalledPackages(0));
            final AppInfo[] app_info = new AppInfo[pInfo.size()];
            int counter = 0;
            packageNm = new ArrayList<String>();
            for(PackageInfo item: pInfo){
                try{
                    applicationInfo = pm.getApplicationInfo(item.packageName, 1);
                    app_info[counter] = new AppInfo(pm.getApplicationIcon(applicationInfo),
                            String.valueOf(pm.getApplicationLabel(applicationInfo)));
                    packageNm.add(applicationInfo.processName);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                counter++;
            }

            ListView listApplication = (ListView)rootView.findViewById(R.id.listView1);
            final AppInfoAdapter adapter = new AppInfoAdapter(getActivity(), app_info);
            listApplication.setAdapter(adapter);
            if(selectedPackage!=null){
            for(int i = 0; i<selectedPackage.size();i++){
                adapter.setChecked(selectedPackageIndex.get(i), true);
                }
            }
            Button b = (Button)rootView.findViewById(R.id.select);
            b.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    StringBuilder result = new StringBuilder();
                    for(int i=0;i<app_info.length;i++)
                    {
                        if(adapter.mCheckStates.get(i)==true)
                        {
                            selectedPackage.add(packageNm.get(i));
                            selectedPackageIndex.add(i);
                        }
                    }
                    MainActivity.checkedAppList = selectedPackage;
                    MainActivity.checkedAppIndexList=selectedPackageIndex;
                    try {
                        BlockingAppList.activity.finish();
                    } catch (Exception ignored) {}
                }
            });
            return rootView;
        }
    }
}
