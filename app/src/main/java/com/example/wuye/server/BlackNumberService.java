package com.example.wuye.server;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;


import com.android.internal.telephony.ITelephony;
import com.example.wuye.activity.SettingActivity;
import com.example.wuye.db.dao.BlackNumberOpenHelperDao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.support.v4.app.ActivityCompat.requestPermissions;


public class BlackNumberService extends Service {

    private BlackNumberOpenHelperDao mDAO = null;
    private InnerSmsReceiver mInnerSmsReceiver;
    private TelephonyManager mTtelephonyManager;
    private MyBlackerPhoneStateListener mPhoneStateListener;
    private MyContentResolver mContentRResolver;


    public BlackNumberService() {
    }

    @Override
    public void onCreate() {

        mDAO = BlackNumberOpenHelperDao.getInstance(this);

        //拦截短信
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        mInnerSmsReceiver = new InnerSmsReceiver();
        registerReceiver(mInnerSmsReceiver, filter);


        //拦截电话

        mTtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyBlackerPhoneStateListener();
        mTtelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容,获取发送短信电话号码,如果此电话号码在黑名单中,并且拦截模式也为1(短信)或者3(所有),拦截短信
            //1,获取短信内容


            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //2,循环遍历短信过程
            for (Object object : objects) {
                //3,获取短信对象
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
                //4,获取短信对象的基本信息
                String originatingAddress = sms.getOriginatingAddress();
                String messageBody = sms.getMessageBody();

                int mode = mDAO.getMode(originatingAddress);

                if (mode == 1 || mode == 3) {
                    //拦截短信(android 4.4版本失效	短信数据库,删除)
                    abortBroadcast();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mInnerSmsReceiver != null ) {
            unregisterReceiver(mInnerSmsReceiver);
        }
        if( mPhoneStateListener != null) {
            mTtelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if(mContentRResolver!=null) {
            getContentResolver().unregisterContentObserver(mContentRResolver);
        }

        super.onDestroy();
    }

    class MyBlackerPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:

                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    endCall(incomingNumber);
                    break;
            }

        }
    }

    private void endCall(String phone) {
        // ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        int mode = mDAO.getMode(phone);
        if (mode == 2 || mode == 3) {


            try {

                Class clazz = Class.forName("android.os.ServiceManager");
                //2,获取方法
                Method method = clazz.getMethod("getService", String.class);
                //3,反射调用此方法
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                //4,调用获取aidl文件对象方法
                ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                //5,调用在aidl中隐藏的endCall方法
                iTelephony.endCall();

            } catch (Exception e) {
                Log.d("tag", "1");
                e.printStackTrace();

            }



            mContentRResolver= new MyContentResolver(new Handler(), phone);
            getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true,
                    mContentRResolver);

        }
    }

    class MyContentResolver extends ContentObserver {
        private String phone;

        public MyContentResolver(Handler handler, String phone) {
            super(handler);
            this.phone = phone;
        }

        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().delete(Uri.parse("content://call_log/calls"),"number=?",new String[]{phone});
            super.onChange(selfChange);

        }
    }
}