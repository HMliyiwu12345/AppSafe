package com.example.wuye.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.engine.BackupSms;

public class AtoolsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final int COUNT = 100;
    private static final int PROGRESS = 101;

    private TextView tv_count;
    private TextView tv_progress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COUNT) {
                tv_count.setText((String) msg.obj);
            }
            if (msg.what == PROGRESS) {
                tv_progress.setText((String) msg.obj);

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        init_UI();
    }

    private void init_UI() {
        TextView tv_queryaddress = (TextView) findViewById(R.id.tv_queryaddress);
        tv_queryaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), QueryAddressActivity.class);
                startActivity(intent);
            }
        });
        TextView tv_backupsms = (TextView) findViewById(R.id.tv_backupsms);
        tv_backupsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AtoolsActivity.this);
        final AlertDialog alertDialog = builder.create();
        View view = View.inflate(getApplicationContext(), R.layout.backup_sms_layout, null);
        alertDialog.setView(view);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        tv_count = view.findViewById(R.id.tv_count);
        tv_progress = view.findViewById(R.id.tv_progress);
        alertDialog.show();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_SMS);

            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.READ_SMS}, REQUEST_CODE_ASK_PERMISSIONS);

                return;

            }


            new Thread(new Runnable() {
                @Override
                public void run() {
                    ;
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "wuyeaikengkeng.xml";
                    BackupSms.getSms(getApplicationContext(), path, mHandler, new BackupSms.CallBack() {
                        @Override
                        public void setMAX(int index) {
                            progressBar.setMax(index);
                        }

                        @Override
                        public void setProgress(int progress) {
                            progressBar.setProgress(progress);
                        }
                    });
                    alertDialog.dismiss();
                }

            }).start();
            // alertDialog.dismiss();
        }


    }
}