package com.newbee.system_applist_lib.systemapp.bean;










import java.io.Serializable;

public class SystemAppInfoBean implements Serializable {
    private String name;
    private String pakeageName;
    private String indexActivityClass;
    private String needStartClass;
    private int iconRs;
    private int index;
    private final int defIndex=9999;

    public SystemAppInfoBean() {
        index=defIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPakeageName() {
        return pakeageName;
    }

    public void setPakeageName(String pakeageName) {
        this.pakeageName = pakeageName;
    }

    public String getIndexActivityClass() {
        return indexActivityClass;
    }

    public void setIndexActivityClass(String indexActivityClass) {
        this.indexActivityClass = indexActivityClass;
    }


    public String getNeedStartClass() {
        return needStartClass;
    }

    public void setNeedStartClass(String needStartClass) {
        this.needStartClass = needStartClass;
    }


    public int getIconRs() {
        return iconRs;
    }

    public void setIconRs(int iconRs) {
        this.iconRs = iconRs;
    }

    public int getIndex() {
        if(index<0){
            index=defIndex;
        }
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "SystemAppInfoBean{" +
                "name='" + name + '\'' +
                ", pakeageName='" + pakeageName + '\'' +
                ", indexActivityClass='" + indexActivityClass + '\'' +
                ", needStartClass='" + needStartClass + '\'' +
                ", iconRs=" + iconRs +
                ", index=" + index +
                '}';
    }
}
