package com.newbee.system_applist;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.newbee.system_applist_lib.systemapp.PackageManagerUtil;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerObserver;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerSubscriptionSubject;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private PackageManagerObserver observer=new PackageManagerObserver() {
        @Override
        public void update(PackageManagerType eventBs, Object object) {
            switch (eventBs){
                case GET_SYSTEM_APPS:
                    Log.i("kankanshuju","kankanshujuzenmehuishi:"+object);
                    break;
                case ERR:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManagerSubscriptionSubject.getInstance().addObserver(observer);
        List<String> needHidePack=new ArrayList<>();
        needHidePack.add("com.huawei.appmarket");
        needHidePack.add("com.huawei.android.ds");
        Map<String,Integer> sortMap=new HashMap<>();
        sortMap.put("com.android.soundrecorder",11);
        sortMap.put("com.android.deskclock",1);
        sortMap.put("com.newbee.aip.asrwakeup3",1);
        PackageManagerUtil.getInstance().init(this,needHidePack,sortMap);
        PackageManagerUtil.getInstance().toGetSystemApps();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PackageManagerUtil.getInstance().close();
        PackageManagerSubscriptionSubject.getInstance().removeObserver(observer);
    }
}