package com.example.wuye.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.server.AddressService;
import com.example.wuye.server.RocketService;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.StreamUtil;
import com.example.wuye.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int UPDATE = 100;
    private static final int NONET = 102;
    private TextView textView;
    private String tag = "MainActivity";
    private String mVersionname;
    private static final int ENTER = 101;
    private int mVersionCode;
    private String versionDecr;
    private String downLoadAddress;
    private Boolean currentCheck;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE:
                    update();
                    break;
                case ENTER:
                    moveRealMainActivity();
                    break;
                case NONET:
                    ToastUtil.show(MainActivity.this, "没有网络");
                    moveRealMainActivity();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv_vesion_name);
        init_Data();
        init_DB();
        init_address();
        init_Rocket();
    }

    private void init_Rocket() {
        boolean rocket_open = SpUtil.getBoolean(getApplicationContext(), ConstantUtil.ROCKET_OPEN, false);
        if(rocket_open){
            new Intent(getApplicationContext(), RocketService.class);
            startService(new Intent(getApplicationContext(), RocketService.class));
        }
    }

    private void init_address() {
        boolean add = SpUtil.getBoolean(this, ConstantUtil.ADDRESS_PHONE, false);
        if (add) {
            startService(new Intent(this, AddressService.class));
        } else {
            stopService(new Intent(this, AddressService.class));
        }
    }

    private void init_DB() {
        File file = getFilesDir();
        File files = new File(file, "address.db");
        if (files.exists()) {
            return;
        } else {
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                inputStream = getAssets().open("address.db");
                fileOutputStream = new FileOutputStream(files);
                byte[] bytes = new byte[1024];
                int temp = -1;
                while ((temp = inputStream.read(bytes)) != -1) {
                    fileOutputStream.write(bytes, 0, temp);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null && fileOutputStream != null) {
                    try {
                        inputStream.close();
                        fileOutputStream.close();
                    } catch (IOException e) {


                    }
                }
            }

        }
    }


    private void moveRealMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void init_Data() {
        //获取当前检测版本更新得开关状态
        currentCheck = SpUtil.getBoolean(this, ConstantUtil.UPDATA_OPEN, false);

        //获取版本好
        mVersionCode = getVersionCode();
        mVersionname = getVersionName();
        textView.setText("当前版本名称" + mVersionname);
        //获取网络请求的json数据
        if (currentCheck == true) {
            getNetWork();
        } else {
            mhandler.sendEmptyMessageDelayed(ENTER, 4000);
        }

    }

    private void getNetWork() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://192.168.43.228:8080/temp/xiaopipi.json").build();

                    Response response = client.newCall(request).execute();
                    InputStream inputStream = response.body().byteStream();
                    String data = StreamUtil.streamToString(inputStream);
                    //                      Log.d(tag, data);


                    //获取版本名称 版本描述 版本号 下载地址
                    JSONObject jsonObject = new JSONObject(data);
                    String versionName = jsonObject.getString("versionName");
                    versionDecr = jsonObject.getString("versionDecr");
                    String versionCode = jsonObject.getString("versionCode");
                    downLoadAddress = jsonObject.getString("downLoad");
//
//                    Log.d(tag, versionName);
//                    Log.d(tag, versionDecr);
                    //                  Log.d(tag, versionCode);
//                    Log.d(tag, downLoad);
                    //提示更新

                    if (mVersionCode < Integer.parseInt(versionCode)) {

                        message.what = UPDATE;
                    } else {

                        message.what = ENTER;

                    }
                } catch (Exception e) {
                    message.what = NONET;
                    e.printStackTrace();
                } finally {
                    long endTime = System.currentTimeMillis();

                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    mhandler.sendMessage(message);
                }
            }
        }).start();
    }


    private void update() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.pkq);
        builder.setTitle("软件更新");
        builder.setMessage(versionDecr);
        builder.setPositiveButton("下载吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoad();
            }
        });
        builder.setNegativeButton("滚吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveRealMainActivity();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                moveRealMainActivity();
            }
        });
        builder.show();
    }

    private void downLoad() {
        OkHttpUtils//
                .get()//
                .url(downLoadAddress)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "safeapp.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtil.show(getApplicationContext(), "下载失败");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        ToastUtil.show(getApplicationContext(), "下载成功");
                        String path = response.getPath();
                        Log.d("tag", path);
                        install(MainActivity.this, path);
                    }


                });
    }


    public void install(Context context, String filePath) {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    context
                    , "com.example.wuye.fileprovider"
                    , apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        // context.startActivity(intent);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        moveRealMainActivity();
        super.onActivityResult(requestCode, resultCode, data);

    }

    private String getVersionName() {
        String versionName = null;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.wuye.appsafe", 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;

    }

    private int getVersionCode() {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.wuye.appsafe", 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK)

            return true;//不执行父类点击事件

        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件

    }

}
