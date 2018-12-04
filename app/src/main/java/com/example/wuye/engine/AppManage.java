package com.example.wuye.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WUYE on 2018/9/6.
 */

public class AppManage {
    public static List<AppInfo> getAppInfoList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for(PackageInfo packageInfo:packageInfoList){
            AppInfo appInfo=new AppInfo();
            //获取包名
            appInfo.packageName=packageInfo.packageName;
            //获取应用程序信息
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //获取应用程序名称
            appInfo.appName= (String) applicationInfo.loadLabel(packageManager);
            //获取应用程序图标
            appInfo.icon=applicationInfo.loadIcon(packageManager);
            //判断是否属于系统应用
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
                appInfo.isSystem=true;
            }
            else{
                appInfo.isSystem=false;

            }
            if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                appInfo.isCard=true;
            }
            else{
                appInfo.isCard=false;
            }
            appInfos.add(appInfo);

        }

        return appInfos;
    }

}
