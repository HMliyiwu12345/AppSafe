package com.example.wuye.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by WUYE on 2018/8/10.
 */

public class AddressDao {

    public static String path = "data/data/com.example.wuye.appsafe/files/address.db";
    public static String mAddress = "未知号码";
    private static SQLiteDatabase db= SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);;

    public static String inquiry(String phone) {
        String regularExpression = "^1[3-8]\\d{9}";
        mAddress = "未知号码";
        if (phone.matches(regularExpression)) {
            String substring = phone.substring(0, 7);
            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id=?", new String[]{substring},
                    null, null, null);
            if (cursor.moveToNext()) {
                String outkey = cursor.getString(0);
                Cursor cursor1 = db.query("data2", new String[]{"location"}, "id=?", new String[]{outkey},
                        null, null, null);
                if (cursor1.moveToNext()) {
                    mAddress = cursor1.getString(0);

                }
            }
        }
        else {
            int length = phone.length();
            switch (length){
                case 3:
                    if(phone.equals("110")){
                        mAddress="报警电话";
                    }
                    if(phone.equals("120")){
                        mAddress="急救电话";
                    }
                    if(phone.equals("119")){
                        mAddress="消防电话";
                    }
                    if(phone.equals("114")){
                        mAddress="查询电话";
                    }
                    break;
                case 4://119 110 120 114
                    mAddress = "模拟器";
                    break;
                case 5://10086 99555
                    mAddress = "服务电话";
                    break;
                case 7:
                    mAddress = "本地电话";
                    break;
                case 8:
                    mAddress = "本地电话";
                    break;
                case 11:
                    String substring = phone.substring(1, 3);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area=?", new String[]{substring},
                            null, null, null);
                    if (cursor.moveToNext()) {
                        mAddress = cursor.getString(0);
                    }
                    else{
                        mAddress="未知号码";
                    }
                    break;
                case 12:
                    String substring1= phone.substring(1, 4);
                    Cursor cursor1 = db.query("data2", new String[]{"location"}, "area=?", new String[]{substring1},
                            null, null, null);
                    if (cursor1.moveToNext()) {
                        mAddress = cursor1.getString(0);
                    }
                    else{
                        mAddress="未知号码";
                    }
                    break;
            }
        }
       // Log.d("tag","mAdress="+mAddress);
        return mAddress;
    }

}
