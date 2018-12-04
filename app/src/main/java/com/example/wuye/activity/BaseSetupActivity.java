package com.example.wuye.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.wuye.appsafe.R;

/**
 * Created by WUYE on 2018/7/27.
 */

public abstract class BaseSetupActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getX() - e2.getX() > 0) {
                    showNextPage();

                } else if (e1.getX() - e2.getX() < 0) {
                    showPrePage();

                }
                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });

    }

    protected abstract void showPrePage();

    protected abstract void showNextPage();

    public void NextPage(View view) {
        showNextPage();
    }

    public void PrePage(View view) {
        showPrePage();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }
}
