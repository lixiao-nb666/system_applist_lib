package com.newbee.system_applist_lib.systemapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.newbee.system_applist_lib.systemapp.PackageManagerUtil;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerSubscriptionSubject;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerType;


public class SystemAppReceiver extends BroadcastReceiver {
    private final String tag=getClass().getName()+">>>>";

    @Override
    public void onReceive(Context context, Intent intent) {
       String action=intent.getAction();
        Log.i("------------","kankantime:"+System.currentTimeMillis());
       if(PackageManagerUtil.getInstance().isReceiverGetAppList()){
           PackageManagerUtil.getInstance().toGetSystemApps();
           PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_GET_APPLIST_PLEASE_WAIT,action);
           return;
       }
      String  packageName = intent.getDataString();
        if (TextUtils.isEmpty(packageName)) {
            //包名是空的直接返回
            return;
        }
        switch (action) {
            case Intent.ACTION_PACKAGE_ADDED:
            case  Intent.ACTION_PACKAGE_INSTALL:
                //应用被添加
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_PACKAGE_ADDED,action);
                break;
            case  Intent.ACTION_PACKAGE_CHANGED:
                //应用被删除
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_PACKAGE_CHANGED,action);
                break;
            case  Intent.ACTION_PACKAGE_REMOVED:
                //应用被改变
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_PACKAGE_REMOVED,action);
                break;
            case  Intent.ACTION_PACKAGE_REPLACED:
                //应用被替换
//                ToastUtil.showToast(packageName + "替换成功");
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_PACKAGE_REPLACED,action);
                break;
            case  Intent.ACTION_PACKAGE_RESTARTED:
                //应用被重启
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.RECEIVER_PACKAGE_RESTARTED,action);
                break;


        }

    }
}
