package com.example.wuye.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.Md5Util;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.ToastUtil;

public class HomeActivity extends AppCompatActivity {


    private String[] mTitleStrs;
    private int[] mPicture;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init_UI();
        init_Data();

    }

    private void init_UI() {
        GridLayout gridLayout;
        gridView = (GridView) findViewById(R.id.gv_gridView);
    }

    private void init_Data() {
        mTitleStrs = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };
        mPicture = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
        };
        gridView.setAdapter(new MyAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDialog();
                        break;
                    case 1:
                        startActivity(new Intent(getApplicationContext(),BlackerActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getApplicationContext(),AppManageActivity.class));
                        break;
                    case 7:
                        Intent intent1 = new Intent(getApplicationContext(), AtoolsActivity.class);
                        startActivity(intent1);
                        break;
                    case 8:
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showDialog() {
        String psw = SpUtil.getString(this, ConstantUtil.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psw)) {
            showsetpsw();
        } else {
            showconfirmpsw();

        }
    }
//再次登陆得密码
    private void showconfirmpsw() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog, null);
        final Button postive = (Button) view.findViewById(R.id.postive1);
        final Button nagtive = (Button) view.findViewById(R.id.nagtive1);
        final EditText tv_psw = (EditText) view.findViewById(R.id.tv_psw1);
        postive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Sppsw=SpUtil.getString(getApplicationContext(), ConstantUtil.MOBILE_SAFE_PSD, "詹莹莹");
                if (!TextUtils.isEmpty(tv_psw.getText().toString())) {
                    String Mdpsd = Md5Util.encoder(tv_psw.getText().toString());
                    if (Sppsw.equals(Mdpsd)) {
                        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }else{
                        ToastUtil.show(getApplication(), "密码输入错误");
                    }

                } else {
                    ToastUtil.show(getApplication(), "你没有输入密码");
                }
            }
        });
        nagtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();


    }
//设置初始密码
    private void showsetpsw() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alertDialog = builder.create();
        final View view = View.inflate(this, R.layout.dialoginit, null);
        final Button postive = (Button) view.findViewById(R.id.postive);
        final Button nagtive = (Button) view.findViewById(R.id.nagtive);
        final EditText tv_psw = (EditText) view.findViewById(R.id.tv_psw);
        final EditText tv_confirmpsw = (EditText) view.findViewById(R.id.tv_confirmpsw);

        postive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(tv_psw.getText().toString()) && !TextUtils.isEmpty(tv_confirmpsw.getText().toString())) {
                    if (tv_psw.getText().toString().equals(tv_confirmpsw.getText().toString())) {
                        String Mdpsd = Md5Util.encoder(tv_psw.getText().toString());
                        SpUtil.putString(getApplicationContext(), ConstantUtil.MOBILE_SAFE_PSD, Mdpsd);
                        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    } else {
                        ToastUtil.show(getApplication(), "两次密码不一致");
                    }
                } else {
                    ToastUtil.show(getApplication(), "密码为空");
                }
            }
        });
        nagtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mTitleStrs.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitleStrs[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.icon, null);
            ImageView iconImage = (ImageView) view.findViewById(R.id.iv_iconImage);
            TextView iconText = (TextView) view.findViewById(R.id.tv_iconText);
            iconText.setText(mTitleStrs[position]);
            iconImage.setBackgroundResource(mPicture[position]);

            return view;
        }
    }
}
