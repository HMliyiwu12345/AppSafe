package com.example.wuye.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.wuye.appsafe.R;
import com.example.wuye.server.LocationServer;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean setover = SpUtil.getBoolean(context, ConstantUtil.SETUPOVER, false);
        if (setover) {
            Object[] object = (Object[]) intent.getExtras().get("pdus");
            for (Object o : object) {
                //创建消息对象
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
                //创建消息体
                String messageBody = smsMessage.getMessageBody();
                if (messageBody.contains("我爱小皮皮")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.xpp);
                    mediaPlayer.setLooping(true);
                    Log.d("tag","3");
                    mediaPlayer.start();
                }
                if (messageBody.contains("#*location*#")) {
                    context.startService(new Intent(context, LocationServer.class));
                    Log.d("tag","2");
                }
            }
        }
    }
}
