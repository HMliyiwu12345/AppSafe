package com.example.wuye.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by WUYE on 2018/7/3.
 */

public class SpUtil {
    public static SharedPreferences sp;
    public static void putBoolean(Context context, String key,Boolean value){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context context, String  key, Boolean defValue){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key,defValue);
    }


    public static void putString(Context context, String key,String value){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,value).commit();
    }
    public static String getString(Context context, String  key, String defValue){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }


    public static void putInt(Context context, String key,int value){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,value).commit();
    }
    public static int getInt(Context context, String  key, int defValue){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getInt(key,defValue);
    }
    public static void remove(Context context, String  key){
        if(sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
       sp.edit().remove(key).commit();
    }
}
