package com.newbee.system_applist_lib.systemapp.broadcastreceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SystemAppReceiverUtil {

    private SystemAppReceiver systemAppReceiver;

    public SystemAppReceiverUtil() {
        systemAppReceiver = new SystemAppReceiver();
    }

    public void start(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_RESTARTED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addDataScheme("package");
        context.registerReceiver(systemAppReceiver, intentFilter);
    }


    public void close(Context context) {
        context.unregisterReceiver(systemAppReceiver);
    }


}
