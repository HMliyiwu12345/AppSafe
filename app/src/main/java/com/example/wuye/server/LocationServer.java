package com.example.wuye.server;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;

public class LocationServer extends Service {
    private String contact_phone;


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        contact_phone = SpUtil.getString(getApplicationContext(), ConstantUtil.CONTACT_PHONE, "");
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2,以最优的方式获取经纬度坐标()
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//指定获取经纬度的精确度
        String bestProvider = lm.getBestProvider(criteria, true);
        Log.d("tag","1");
        //3,在一定时间间隔,移动一定距离后获取经纬度坐标
        MyLocationListener myLocationListener = new MyLocationListener();
        lm.requestLocationUpdates(bestProvider, 0, 0, myLocationListener);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            Log.d("tag","3");
            //经度
            double longitude = location.getLongitude();
            //纬度
            double latitude = location.getLatitude();

            //4,发送短信(添加权限)
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contact_phone, null, "longitude = "+longitude+",latitude = "+latitude, null, null);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }
}


