package com.example.wuye.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.server.AddressService;
import com.example.wuye.server.BlackNumberService;
import com.example.wuye.server.RocketService;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.view.SettingclickView;
import com.example.wuye.view.SettingitemView;

import static android.support.v4.app.ActivityCompat.requestPermissions;

public class SettingActivity extends AppCompatActivity {

    private SettingitemView view;
    private SettingitemView view1;
    private SettingclickView view2;
    private SettingclickView view3;
    private SettingitemView view4;
    private SettingitemView view5;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private  int colorstyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init_UI();
        init_Update();
        init_address();
        init_style();
        init_toastlocation();
        init_rocket();
        init_blacknumber();
    }

    private void init_blacknumber() {

        boolean lastCheck = SpUtil.getBoolean(this, ConstantUtil.BLACK_NUMBER, false);
        view5.setCheck(lastCheck);

        view5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ischeck = view5.Checkbook();
                view5.setCheck(!ischeck);
                Boolean currentCheck = view5.Checkbook();
                SpUtil.putBoolean(getApplicationContext(), ConstantUtil.BLACK_NUMBER, currentCheck);

                if(currentCheck==true){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CALL_LOG);

                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){

                            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},REQUEST_CODE_ASK_PERMISSIONS);

                            return;

                        }

                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_CALL_LOG);

                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED){

                            requestPermissions(new String[]{Manifest.permission.WRITE_CALL_LOG},REQUEST_CODE_ASK_PERMISSIONS);

                            return;

                        }

                    }
                    startService(new Intent(getApplicationContext(),BlackNumberService.class));
                }
                else{
                    stopService(new Intent(getApplicationContext(),BlackNumberService.class));
                }
            }
        });
    }

    private void init_rocket() {
        boolean lastCheck = SpUtil.getBoolean(this, ConstantUtil.ROCKET_OPEN, false);
        view4.setCheck(lastCheck);
        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ischeck = view4.Checkbook();
                view4.setCheck(!ischeck);
                Boolean currentCheck = view4.Checkbook();
                SpUtil.putBoolean(getApplicationContext(), ConstantUtil.ROCKET_OPEN, currentCheck);
                if(currentCheck){
                    new Intent(getApplicationContext(), RocketService.class);
                    startService(new Intent(getApplicationContext(), RocketService.class));
                }else{
                    stopService(new Intent(getApplicationContext(), RocketService.class));
                }

            }
        });

    }

    private void init_toastlocation() {
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startActivity( new Intent(SettingActivity.this,ToastLocationActivity.class));
            }
        });
    }

    private void init_style() {
        final String[] itemstyle = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        colorstyle= SpUtil.getInt(this, ConstantUtil.COLORSTYLE, 0);
        view2.setDes(itemstyle[colorstyle]);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }

            protected void showDialog() {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setIcon(R.drawable.pkq);
                builder.setTitle("请选择归属地样式");
                colorstyle= SpUtil.getInt(getApplicationContext(), ConstantUtil.COLORSTYLE, 0);
                builder.setSingleChoiceItems(itemstyle, colorstyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtil.putInt(getApplicationContext(), ConstantUtil.COLORSTYLE, which);
                        view2.setDes(itemstyle[which]);
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    private void init_Update() {
        boolean lastCheck = SpUtil.getBoolean(this, ConstantUtil.UPDATA_OPEN, false);
        view.setCheck(lastCheck);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ischeck = view.Checkbook();
                view.setCheck(!ischeck);
                Boolean currentCheck = view.Checkbook();
                SpUtil.putBoolean(getApplicationContext(), ConstantUtil.UPDATA_OPEN, currentCheck);
            }
        });
    }

    /**
     * 电话归属地设置界面
     */

    private void init_address() {
        boolean lastCheck1 = SpUtil.getBoolean(this, ConstantUtil.ADDRESS_PHONE, false);
        view1.setCheck(lastCheck1);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean ischeck = view1.Checkbook();
                view1.setCheck(!ischeck);
                Boolean currentCheck = view1.Checkbook();
                if (currentCheck) {
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
                SpUtil.putBoolean(getApplicationContext(), ConstantUtil.ADDRESS_PHONE, currentCheck);
            }
        });
    }

    private void init_UI() {
        view = (SettingitemView) findViewById(R.id.settingitemview);
        view1 = (SettingitemView) findViewById(R.id.settingitemview1);
        view2 = (SettingclickView) findViewById(R.id.settingclickview);
        view3 = (SettingclickView) findViewById(R.id.settingtouchView);
        view4 = (SettingitemView) findViewById(R.id.settingrocket);
        view5 = (SettingitemView) findViewById(R.id.settingblacknumber);

    }
}
