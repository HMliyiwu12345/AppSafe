package com.example.wuye.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.engine.AddressDao;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.ToastUtil;

public class AddressService extends Service {
    private MyPhoneStateListener mPhoneStateListener;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private View view;
    private TextView textView;
    private WindowManager mWindowManager;
    private TelephonyManager mTtelephonyManager;
    private String inquiry;
    private int location_x;
    private int location_y;
    private int mScreenHeight;
    private int mScreenWidth;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            textView.setText(inquiry);


        }
    };
    private int startX;
    private int startY;

    public AddressService() {


    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTtelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mPhoneStateListener = new MyPhoneStateListener();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mTtelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyPhoneStateListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mWindowManager != null && view != null) {
                        mWindowManager.removeView(view);
                    }
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    showToast(incomingNumber);
                    //响铃(展示吐司)
                    break;
            }
        }
    }


    private void showToast(final String incomingaddress) {
        final WindowManager.LayoutParams params = mParams;
        int[] colorstyle = new int[]{R.mipmap.call_locate_white, R.mipmap.call_locate_orange, R.mipmap.call_locate_blue,
                R.mipmap.call_locate_gray, R.mipmap.call_locate_green,};
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //  params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        try {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } catch (Exception e) {
            startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
        }
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //   | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
         params.gravity = Gravity.CENTER;
        location_x = SpUtil.getInt(getApplicationContext(), ConstantUtil.LOCATION_X, 0);
        location_y = SpUtil.getInt(getApplicationContext(), ConstantUtil.LOCATION_Y, 0);


        view = View.inflate(this, R.layout.toast_view_layout, null);
        textView = (TextView) view.findViewById(R.id.tv_toast);

        int SpColor = SpUtil.getInt(this, ConstantUtil.COLORSTYLE, 0);
        textView.setBackgroundResource(colorstyle[SpColor]);
        mWindowManager.addView(view, params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                inquiry = AddressDao.inquiry(incomingaddress);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
//        params.x = location_x;
//        params.y = location_y;
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx= (int)  event.getRawX()-startX;
                        int dy= (int)  event.getRawY()-startY;
                        params.x= (int) params.x+dx;
                        params.y=(int)   params.y+dy;
                        mWindowManager.updateViewLayout(view, params);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        SpUtil.putInt(getApplicationContext(), ConstantUtil.LOCATION_X,  params.x);
                        SpUtil.putInt(getApplicationContext(), ConstantUtil.LOCATION_Y,  params.y);
                        break;
                }


                return true;
            }
        });




    }

    @Override
    public void onDestroy() {
        if (mPhoneStateListener != null && mTtelephonyManager != null) {
            mTtelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        super.onDestroy();
    }
}
