package com.newbee.system_applist_lib.systemapp.observer;

public enum PackageManagerType {
    GET_SYSTEM_APPS,//获得系统应用
    ERR,//异常报错
    RECEIVER_GET_APPLIST_PLEASE_WAIT,//广播接收器自动去获取应用集合了，请等待
    RECEIVER_PACKAGE_ADDED,
    RECEIVER_PACKAGE_CHANGED,
    RECEIVER_PACKAGE_REMOVED,
    RECEIVER_PACKAGE_REPLACED,
    RECEIVER_PACKAGE_RESTARTED,




}
