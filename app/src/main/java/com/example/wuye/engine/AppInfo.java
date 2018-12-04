package com.example.wuye.engine;

import android.graphics.drawable.Drawable;

/**
 * Created by WUYE on 2018/9/6.
 */

public class AppInfo {
    public String packageName;
    public String appName;
    public Drawable icon;
    public boolean isSystem;
    public boolean isCard;

    public void setPackageName(String packageName){
        this.packageName=packageName;
    }

    public String  getPackage(){
        return packageName;
    }

    public void setAppName(String appName){
        this.appName=appName;
    }

    public String  getAppName(){
        return appName;
    }

    public void setIcon(Drawable Icon){
        this.icon=icon;
    }

    public Drawable getIcon(){
        return icon;
    }

    public void setIsSystem(boolean isSystem){
        this.isSystem=isSystem;
    }

    public boolean getIsSystem(){
        return isSystem;
    }

    public void setIsCard(boolean isCard){
        this.isCard=isCard;
    }

    public boolean getIsCard(){
        return isCard;
    }
}
