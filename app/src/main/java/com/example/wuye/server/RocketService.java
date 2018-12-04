package com.example.wuye.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.wuye.appsafe.R;

public class RocketService extends Service {
    private WindowManager mWindowManager;
    private View view;
    final WindowManager.LayoutParams params = creatToast();
    final WindowManager.LayoutParams params1 = creatToast();
    private View viewRocket;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SEND_ROCKET) {
                int y = (int) msg.obj;
                params.y = y;
                mWindowManager.updateViewLayout(view, params);
            }

            if (msg.what == END_ROCKET) {
                mWindowManager.removeView(viewRocket);
            }

        }
    };
    private int SEND_ROCKET = 1;
    private int END_ROCKET = 2;

    public RocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        showRocket();
    }

    private void showRocket() {

        view = View.inflate(getApplicationContext(), R.layout.rocketlayout, null);
        ImageView imageView = view.findViewById(R.id.iv_rocket);
        AnimationDrawable background = (AnimationDrawable) imageView.getBackground();
        background.start();
        mWindowManager.addView(view, params);
        view.setOnTouchListener(new View.OnTouchListener() {
            int start_x;
            int start_y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        start_x = (int) event.getRawX();
                        start_y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - start_x;
                        int dy = (int) event.getRawY() - start_y;
                        params.x = params.x + dx;
                        params.y = params.y + dy;
                        mWindowManager.updateViewLayout(view, params);
                        start_x = (int) event.getRawX();
                        start_y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (params.y > 1400) {
                            final WindowManager.LayoutParams params1 = creatToast();
                            viewRocket = View.inflate(getApplicationContext(), R.layout.activity_rocket_end, null);
                            ImageView imageView = viewRocket.findViewById(R.id.iv_rocket_end);
                            AnimationDrawable background = (AnimationDrawable) imageView.getBackground();
                            background.start();
                            params1.x = params.x;
                            params1.y = 1400;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i < 14; i++) {
                                        int y = 1400 - 140 * i;
                                        try {
                                            Thread.sleep(20);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        Message message = Message.obtain();
                                        message.what = SEND_ROCKET;
                                        message.obj = y;
                                        mHandler.sendMessage(message);
                                    }
                                }
                            }).start();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1001);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Message message1 = Message.obtain();
                                    message1.what = END_ROCKET;
                                    mHandler.sendMessage(message1);
                                }
                            }).start();
                            mWindowManager.addView(viewRocket, params1);
                        }
                        break;


                }

                return false;
            }
        });
    }

    @NonNull
    private WindowManager.LayoutParams creatToast() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        //  params.windowAnimations = com.android.internal.R.style.Animation_Toast;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } catch (Exception e) {
                startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
            }
        }else{
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //   | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        return params;
    }

    @Override
    public void onDestroy() {
        mWindowManager.removeView(view);
        super.onDestroy();
    }
}
