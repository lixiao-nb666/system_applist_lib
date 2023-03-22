package com.newbee.system_applist;

import android.os.Bundle;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.newbee.system_applist_lib.systemapp.PackageManagerUtil;
import com.newbee.system_applist_lib.systemapp.bean.ResultSystemAppInfoBean;
import com.newbee.system_applist_lib.systemapp.broadcastreceiver.SystemAppReceiverUtil;
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
            Log.i("kankanlist","kankanlistdata:"+eventBs+"-----"+object);
            switch (eventBs){
                case GET_SYSTEM_APPS:
                    ResultSystemAppInfoBean resultSystemAppInfoBean= (ResultSystemAppInfoBean) object;
                    Log.i("kankanlist","kankanlistdata:"+resultSystemAppInfoBean);
                    break;
                case ERR:
                    break;
            }
        }
    };
    SystemAppReceiverUtil systemAppReceiverUtil=new SystemAppReceiverUtil();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManagerSubscriptionSubject.getInstance().addObserver(observer);
        List<String> needHidePack=new ArrayList<>();
        needHidePack.add("com.huawei.appmarket");
        needHidePack.add("com.huawei.android.ds");
        Map<String,Integer> sortMap=new HashMap<>();

        Map<String,Integer> sortFuzzyNameMap=new HashMap<>();
        sortFuzzyNameMap.put("UC",2);
        sortFuzzyNameMap.put("_lib",1);
        PackageManagerUtil.getInstance().init(this,needHidePack,sortMap,sortFuzzyNameMap);
        PackageManagerUtil.getInstance().setReceiverGetAppList(true);
        PackageManagerUtil.getInstance().toGetSystemApps();
        systemAppReceiverUtil.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PackageManagerUtil.getInstance().close();
        PackageManagerSubscriptionSubject.getInstance().removeObserver(observer);
        systemAppReceiverUtil.close(this);
    }
}