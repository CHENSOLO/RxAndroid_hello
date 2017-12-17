package com.example.chensolo.rxandroid_hello.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/12/17.
 */

public class AppInfo {
    private String appName;
    private Drawable appIcon;
    private long lastUpdateTime;

    public AppInfo() {

    }

    public String getAppName() {
        return appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public AppInfo(String appName, Drawable appIcon, long lastUpdateTime) {
        this.appName = appName;
        this.appIcon = appIcon;
        this.lastUpdateTime = lastUpdateTime;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "appName='" + appName + '\'' +
                ", appIcon=" + appIcon +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
