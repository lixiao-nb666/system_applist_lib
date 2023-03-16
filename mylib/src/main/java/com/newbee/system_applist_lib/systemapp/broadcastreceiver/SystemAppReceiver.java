package com.newbee.system_applist_lib.systemapp.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.newbee.system_applist_lib.systemapp.PackageManagerUtil;


public class SystemAppReceiver extends BroadcastReceiver {
    private final String tag=getClass().getName()+">>>>";

    @Override
    public void onReceive(Context context, Intent intent) {
        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
//            System.out.println("安装了:" +packageName + "包名的程序");
        }
        //接收卸载广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString();
//            System.out.println("卸载了:"  + packageName + "包名的程序");
        }
        PackageManagerUtil.getInstance().toGetSystemApps();
    }
}
