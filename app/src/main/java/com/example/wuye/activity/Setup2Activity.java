package com.example.wuye.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.ToastUtil;
import com.example.wuye.view.SettingitemView;

public class Setup2Activity extends  BaseSetupActivity {
    private SettingitemView settingitemView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        settingitemView = (SettingitemView) findViewById(R.id.bindingphone);
        check();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);

    }

    @Override
    protected void showNextPage() {
        String nurber_phone = SpUtil.getString(this, ConstantUtil.NURBER_PHONE, "");
        if(TextUtils.isEmpty(nurber_phone)){
            ToastUtil.show(this,"请先获取本机号码");
        }
        else{
            Intent intent = new Intent(this, Setup3Activity.class);
            startActivity(intent);
            finish();
            //开启平移动画
            overridePendingTransition(R.anim.next_in, R.anim.next_out);
        }

    }

    /**
     * 将上一次得绑定手机得状态传递给下一次，并且注册点击事件
     */
    private void check() {
       String nurber_phone = SpUtil.getString(this, ConstantUtil.NURBER_PHONE, "");
        if (TextUtils.isEmpty(nurber_phone)) {
            settingitemView.setCheck(false);
        } else {
            settingitemView.setCheck(true);
        }
        settingitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkbook = settingitemView.Checkbook();
                settingitemView.setCheck(!checkbook);
                if (!checkbook) {
                    //获取本机号码
                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    @SuppressLint("MissingPermission") String simSerialNumber = manager.getSimSerialNumber();
                    SpUtil.putString(getApplicationContext(), ConstantUtil.NURBER_PHONE, simSerialNumber);
                } else {
                    //抹除节点
                    SpUtil.remove(getApplicationContext(), ConstantUtil.NURBER_PHONE);
                }
            }
        });

    }




}
