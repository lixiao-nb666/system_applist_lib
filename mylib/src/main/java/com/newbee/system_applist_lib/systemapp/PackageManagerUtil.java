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
    private Map<String,String> usePckChangeNameMap;//键值是包名，V值是需要替换成的名称
    private Map<String,String> useNameChangeNameMap;//键值是名称，V值是需要替换成的名称
    private Map<String,String> nameReplaceMap;//键值是名称中包含的字符串，V值是需要替换成的字符串
    private Map<String,Integer> usePckChangeIconMap;//键值是名称，V值是需要替换成的ICON



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



    public void init(Context context,List<String> needHidePackList,Map<String,Integer> sortMap,Map<String,Integer> sortFuzzyNameMap,Map<String,String> usePckChangeNameMap,Map<String,String> useNameChangeNameMap,  Map<String,String> nameReplaceMap,Map<String,Integer> usePckChangeIconMap){
        manager = context.getApplicationContext().getPackageManager();
        this.needHidePackList=needHidePackList;
        this.sortMap=sortMap;
        this.sortFuzzyNameMap=sortFuzzyNameMap;
        this.usePckChangeNameMap=usePckChangeNameMap;
        this.useNameChangeNameMap=useNameChangeNameMap;
        this.nameReplaceMap=nameReplaceMap;
        this.usePckChangeIconMap=usePckChangeIconMap;
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
        if(null!=usePckChangeNameMap){
            usePckChangeNameMap.clear();
            usePckChangeIconMap=null;
        }
        if(null!=useNameChangeNameMap){
            useNameChangeNameMap.clear();
            useNameChangeNameMap=null;
        }
        if(null!=nameReplaceMap){
            nameReplaceMap.clear();
            nameReplaceMap=null;
        }
        if(null!=usePckChangeIconMap){
            usePckChangeIconMap.clear();
            usePckChangeIconMap=null;
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
                        app.setName(getName(name,pkg));
                        app.setPakeageName(pkg);
                        app.setIndexActivityClass(cls);
                        int index=getIndex(app);
                        if(index!=-1){
                            app.setIndex(index);
                        }
                        int iconRs=getIconRs(pkg);
                        app.setIconRs(iconRs);
                        resultSystemAppInfoBean.add(app);
                    } catch (Exception e) {
                    }
                }
                resultSystemAppInfoBean.sort();
                PackageManagerSubscriptionSubject.getInstance().update(PackageManagerType.GET_SYSTEM_APPS, resultSystemAppInfoBean);
                threadIsRun=false;
            }

            private int getIndex(SystemAppInfoBean app){
                if(null!=sortMap&&null!=sortMap.get(app.getPakeageName())){
                    return sortMap.get(app.getPakeageName());
                }else if(null!=sortFuzzyNameMap&&!TextUtils.isEmpty(app.getName())){
                    return getIndexBysortFuzzyName(app.getName());
                }
                return -1;
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


            private String getName(String name,String pck){
                if(null!=usePckChangeNameMap&&usePckChangeNameMap.size()>0){
                    String needName=usePckChangeNameMap.get(pck);
                    if(!TextUtils.isEmpty(needName)){
                        return needName;
                    }
                }
                if(null!=useNameChangeNameMap&&useNameChangeNameMap.size()>0){
                    String needName=useNameChangeNameMap.get(name);
                    if(!TextUtils.isEmpty(needName)){
                        return needName;
                    }
                }
                if(null!=nameReplaceMap&&nameReplaceMap.size()>0){
                    for(String k:nameReplaceMap.keySet()){
                        if(name.contains(k)){
                            return name.replace(k,nameReplaceMap.get(k));
                        }
                    }
                }
                return name;
            }


            private int getIconRs(String pck){
                if(null!=usePckChangeIconMap&&usePckChangeIconMap.size()>0){
                    Integer iconRs=usePckChangeIconMap.get(pck);
                    if(null!=iconRs){
                        return iconRs;
                    }
                }
                return 0;
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
