# system_applist_lib
[![](https://jitpack.io/v/lixiao-nb666/system_applist_lib.svg)](https://jitpack.io/#lixiao-nb666/system_applist_lib)

第一步必须加上权限

    <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
        tools:ignore="QueryAllPackagesPermission" />

第二步初始化

        PackageManagerSubscriptionSubject.getInstance().addObserver(observer);
        List<String> needHidePack=new ArrayList<>();
        needHidePack.add("com.huawei.appmarket");
        needHidePack.add("com.huawei.android.ds");
        Map<String,Integer> sortMap=new HashMap<>();
        sortMap.put("com.android.soundrecorder",11);
        sortMap.put("com.android.deskclock",1);
        sortMap.put("com.newbee.aip.asrwakeup3",1);
        PackageManagerUtil.getInstance().init(this,needHidePack,sortMap);

    private PackageManagerObserver observer=new PackageManagerObserver() {
        @Override
        public void update(PackageManagerType eventBs, Object object) {
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

第三步：調用查詢方法

    PackageManagerUtil.getInstance().toGetSystemApps();

第四步：關閉    

    PackageManagerUtil.getInstance().close();
    PackageManagerSubscriptionSubject.getInstance().removeObserver(observer);


如果需要系统安装和卸载事件，添加一个广播即可
<receiver
    android:name="com.newbee.system_applist_lib.systemapp.broadcastreceiver.SystemAppReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
    <action android:name="android.intent.action.PACKAGE_ADDED"></action>
    <action android:name="android.intent.action.PACKAGE_CHANGED"></action>
    <action android:name="android.intent.action.PACKAGE_REMOVED"></action>
    <action android:name="android.intent.action.PACKAGE_REPLACED"></action>
    <action android:name="android.intent.action.PACKAGE_RESTARTED"></action>
    <action android:name="android.intent.action.PACKAGE_INSTALL"></action>
    <data android:scheme="package"></data>
</intent-filter>
</receiver>
/
 
