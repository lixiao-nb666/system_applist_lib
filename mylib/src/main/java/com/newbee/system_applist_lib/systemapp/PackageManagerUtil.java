package com.newbee.system_applist_lib.systemapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.newbee.system_applist_lib.systemapp.bean.ResultSystemAppInfoBean;
import com.newbee.system_applist_lib.systemapp.bean.SystemAppInfoBean;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerSubscriptionSubject;
import com.newbee.system_applist_lib.systemapp.observer.PackageManagerType;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PackageManagerUtil {
    private static PackageManagerUtil managerUtil;
    private boolean initOk=false;
    private PackageManager manager;
//    private AppInfoShare appInfoShare;
    private List<String> needHidePackList;
    private Map<String,Integer> sortMap;//键值是包名，v值是排序的位置
    private Map<String,Integer> sortFuzzyNameMap;//键值是模糊匹配的软件名称，v值是排序的位置


    private PackageManagerUtil() {
    }

    public static PackageManagerUtil getInstance() {
        if (null == managerUtil) {
            synchronized (PackageManagerUtil.class) {
                if (null == managerUtil) {
                    managerUtil = new PackageManagerUtil();
                }
            }
        }
        return managerUtil;
    }



    public void init(Context context,List<String> needHidePackList,Map<String,Integer> sortMap,Map<String,Integer> sortFuzzyNameMap){
        manager = context.getApplicationContext().getPackageManager();
        this.needHidePackList=needHidePackList;
        this.sortMap=sortMap;
        this.sortFuzzyNameMap=sortFuzzyNameMap;
//        appInfoShare=new AppInfoShare(context.getApplicationContext());
        initOk=true;
    }

    public void close(){
        manager=null;
        if(null!=needHidePackList){
            needHidePackList.clear();
            needHidePackList=null;
        }
        if(null!=sortMap){
            sortMap.clear();
            sortMap=null;
        }
        if(null!=sortFuzzyNameMap){
            sortFuzzyNameMap.clear();
            sortFuzzyNameMap=null;
        }

//        appInfoShare=null;
        initOk=false;
    }


//    public AppInfoShare getAppInfoShare() {
//        return appInfoShare;
//    }

    public Drawable getIcon(String packageName) {
        if(initOk==false){
            return null;
        }
        try {

            return manager.getApplicationIcon(packageName);
        } catch (Exception e) {
            return null;
        }

    }

    public void toGetSystemAppsCanUseCache(){
        if(initOk==false){
           return;
        }
        if(null==resultSystemAppInfoBean||null==resultSystemAppInfoBean.getAppList()||resultSystemAppInfoBean.getAppList().size()==0){
            toGetSystemApps();
        }else {
            PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.GET_SYSTEM_APPS, resultSystemAppInfoBean);
        }
    }


    private boolean receiverGetAppList=false;

    public boolean isReceiverGetAppList() {
        return receiverGetAppList;
    }

    public void setReceiverGetAppList(boolean receiverGetAppList) {
        this.receiverGetAppList = receiverGetAppList;
    }

    private ResultSystemAppInfoBean resultSystemAppInfoBean;
    private boolean threadIsRun=false;
    public void toGetSystemApps() {

        if(initOk==false){
            return;
        }
        if(threadIsRun){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                threadIsRun=true;
                resultSystemAppInfoBean= new ResultSystemAppInfoBean();

                List<ResolveInfo> resolveInfoList = getResolveInfos();

                if (resolveInfoList == null || resolveInfoList.size() == 0) {
                    PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.ERR, "system app list is null!");
                    return;
                }
                for (int i = 0; i < resolveInfoList.size(); i++) {
                    ResolveInfo resolveInfo = resolveInfoList.get(i);
                    try {
                        String pkg = resolveInfo.activityInfo.packageName;
                        String cls = resolveInfo.activityInfo.name;
                        ApplicationInfo applicationInfo = manager.getPackageInfo(pkg, i).applicationInfo;
                        String name = applicationInfo.loadLabel(manager).toString();
                        if (TextUtils.isEmpty(pkg)||TextUtils.isEmpty(name) ) {

                            continue;
                        }
                        if(null!=needHidePackList&&needHidePackList.size()>0&&needHidePackList.contains(pkg)){

                            continue;
                        }
                        if(resultSystemAppInfoBean.checkIsExist(pkg,cls)){

                            continue;
                        }

                        SystemAppInfoBean app = new SystemAppInfoBean();
                        app.setName(name);
                        app.setPakeageName(pkg);
                        app.setIndexActivityClass(cls);
                        app.setIconRs(resolveInfo.getIconResource());
                        if(null!=sortMap&&null!=sortMap.get(app.getPakeageName())){
                            int index=sortMap.get(app.getPakeageName());
                            app.setIndex(index);
                        }else if(null!=sortFuzzyNameMap&&!TextUtils.isEmpty(app.getName())){
                            int index=getIndexBysortFuzzyName(app.getName());
                            if(index!=-1){
                                app.setIndex(index);
                            }
                        }
                        resultSystemAppInfoBean.add(app);
                        resultSystemAppInfoBean.sort();

                    } catch (Exception e) {

                    }
                }
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.GET_SYSTEM_APPS, resultSystemAppInfoBean);
                threadIsRun=false;
            }

            private int getIndexBysortFuzzyName(String appName){
                try {
                    for(String name:sortFuzzyNameMap.keySet()){
                        if(appName.contains(name)){
                            return sortFuzzyNameMap.get(name);
                        }
                    }
                }catch (Exception e){
                }
                return -1;
            }
        }).start();
    }





    private List<ResolveInfo> getResolveInfos() {
        List<ResolveInfo> appList = null;
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        appList = manager.queryIntentActivities(intent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(manager));
        return appList;
    }

    public boolean checkAppIsInstalled(String packageName) {
        try {
            return manager.getApplicationIcon(packageName) != null;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
