package com.newbee.system_applist_lib.systemapp.observer;

public interface PackageManagerSubject {
    public void addObserver(PackageManagerObserver baseObserver);

    public void removeObserver(PackageManagerObserver baseObserver);

    public void close();

    public void update(PackageManagerType eventBs, Object object);
}
