package com.newbee.system_applist_lib.systemapp.share;

import android.content.Context;
import android.text.TextUtils;

import com.newbee.data_sava_lib.share.BaseShare;

/**
 * @author lixiaogege!
 * @description: one day day ,no zuo no die !
 * @date :2021/3/15 0015 15:49
 */
public class AppInfoShare extends BaseShare {


    public AppInfoShare(Context context) {
        super(context);
    }


    final String appIndex = "appIndex";

    public void putAppIndex(String pagName, int index) {
        if (TextUtils.isEmpty(pagName)) {
            return;
        }

        putString(pagName + appIndex, index + "");
    }

    public int getAppIndex(String pagName) {
        try {
            if (!TextUtils.isEmpty(pagName)) {
                String shareStr = getString(pagName + appIndex);
                return Integer.valueOf(shareStr);
            }

        } catch (Exception e) {

        }
        return -1;
    }

}
