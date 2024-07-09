package com.newbee.system_applist;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;

import com.newbee.system_applist_lib.systemapp.PackageManagerUtil;
import com.newbee.system_applist_lib.systemapp.bean.ResultSystemAppInfoBean;
import com.newbee.system_applist_lib.systemapp.bean.SystemAppInfoBean;
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
                    for(SystemAppInfoBean systemAppInfoBean:resultSystemAppInfoBean.getAppList()){
                        Log.i("kankanlist","kankanlistdata:"+systemAppInfoBean.getName()+"---------"+systemAppInfoBean.getPakeageName()+"!!!("+systemAppInfoBean.getIndex());
                    }



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
        sortMap.put("com.mediatek.camera",0);

        sortMap.put("cm.aptoidetv.pt",3);
        sortMap.put("com.google.android.youtube.tv",3);
        sortMap.put("com.instagram.android",5);
        sortMap.put("com.amazon.avod.thirdpartyclient",6);
        sortMap.put("com.tiktok.tv",7);
        sortMap.put("com.xiaobaifile.tv",8);
        sortMap.put("com.nrmyw.share_screen",9);
        sortMap.put("com.android.settings",10);
        Map<String,Integer> sortFuzzyNameMap=new HashMap<>();
        sortFuzzyNameMap.put("UC",2);
        sortFuzzyNameMap.put("_lib",1);

        Map<String,String> usePckChangeNameMap=new ArrayMap<>();
        usePckChangeNameMap.put("cm.aptoidetv.pt","12345");
        usePckChangeNameMap.put("com.opera.browser","Opera Browser");
        Map<String,String> useNameChangeNameMap=new ArrayMap<>();
        useNameChangeNameMap.put("MyW User Guide","87489279347");
        Map<String,String> nameReplaceMap=new ArrayMap<>();//键值是名称中包含的字符串，V值是需要替换成的字符串
        nameReplaceMap.put("TV","");
        nameReplaceMap.put(" TV","");
        nameReplaceMap.put("TV ","");

        Map<String,Integer> usePckChangeIconMap=new ArrayMap<>();
        usePckChangeIconMap.put("cm.aptoidetv.pt",R.mipmap.ic_launcher);

        PackageManagerUtil.getInstance().init(this,needHidePack,sortMap,sortFuzzyNameMap,usePckChangeNameMap,useNameChangeNameMap,nameReplaceMap,usePckChangeIconMap);
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