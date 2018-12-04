package com.example.wuye.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wuye.db.BlackNumberInfo;
import com.example.wuye.db.BlackNumberOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WUYE on 2018/8/22.
 */

public class BlackNumberOpenHelperDao {

    private BlackNumberOpenHelper blackNumberOpenHelper;


    private BlackNumberOpenHelperDao(Context context) {
        blackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    public static BlackNumberOpenHelperDao blackNumberOpenHelperDao = null;

    public static BlackNumberOpenHelperDao getInstance(Context context) {
        if (blackNumberOpenHelperDao == null) {
            blackNumberOpenHelperDao = new BlackNumberOpenHelperDao(context);
        }
        return blackNumberOpenHelperDao;
    }

    /*
    添加数据
     */
    public void insert(String phone, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("mode", mode);
        writableDatabase.insert("blacknumber", null, contentValues);
        writableDatabase.close();


    }

    /**
     * 删除一条数据
     */
    public void delete(String phone) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        writableDatabase.delete("blacknumber", "phone=?", new String[]{phone});
        writableDatabase.close();
    }

    /**
     * 更新数据
     */
    public void update(String phone, String mode) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", mode);
        writableDatabase.update("blacknumber", values, "phone=?", new String[]{phone});
        writableDatabase.close();
    }

    /**
     * 查询数据
     */
    public List<BlackNumberInfo> inquiry() {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("blacknumber", new String[]{"phone", "mode"}, null, null,
                null, null, "_id desc");
        List<BlackNumberInfo> BN = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            BN.add(blackNumberInfo);
        }
        cursor.close();
        writableDatabase.close();
        return BN;
    }

    /**
     * 查询部分数据
     */
    public List<BlackNumberInfo> find(int index) {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20;", new String[]{index + ""});
        List<BlackNumberInfo> BN = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.phone = cursor.getString(0);
            blackNumberInfo.mode = cursor.getString(1);
            BN.add(blackNumberInfo);
        }
        cursor.close();
        writableDatabase.close();
        return BN;
    }

    /**
     * 获取数据库数据的总条目
     */
    public int getCount() {
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select count(*) from blacknumber;", null);
        int count = 0;
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        writableDatabase.close();
        return count;
    }

    /**
     * 获取数据的mode
     */
    public int getMode(String phone) {
        int mode = 0;
        SQLiteDatabase writableDatabase = blackNumberOpenHelper.getWritableDatabase();
        Cursor cursor = writableDatabase.query("blacknumber", new String[]{"mode"}, "phone=?",
                new String[]{phone}, null, null, null);
        if (cursor.moveToNext()) {
            mode = cursor.getInt(0);
        }
        return mode;
    }
}
