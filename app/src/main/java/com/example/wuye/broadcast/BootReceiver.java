package com.example.wuye.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;

public class BootReceiver extends BroadcastReceiver {
    String tag="Setup3Activity";
    @Override
    public void onReceive(Context context, Intent intent) {
        String spnurber_phone = SpUtil.getString(context, ConstantUtil.NURBER_PHONE, "");
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String contact_phone = SpUtil.getString(context, ConstantUtil.CONTACT_PHONE, "");
        @SuppressLint("MissingPermission") String simSerialNumber = manager.getSimSerialNumber();
        if(!spnurber_phone.equals(simSerialNumber)){
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(contact_phone, null, "sim change!!!", null, null);

        }


    }
}
