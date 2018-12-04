package com.example.wuye.activity;

import android.annotation.SuppressLint;
import android.database.DataSetObserver;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.engine.AppInfo;
import com.example.wuye.engine.AppManage;

import java.util.ArrayList;
import java.util.List;

public class AppManageActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private TextView tv_magnetic;
    private TextView tv_sd;
    private ListView lv_appmanage;
    private List<AppInfo> appInfoList;
    private List<AppInfo> system;
    private List<AppInfo> custom;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyAdapter myAdapter = new MyAdapter();
            lv_appmanage.setAdapter(myAdapter);
            if (lv_appmanage != null && tv_destittle != null) {
                tv_destittle.setText("用户应用" + "(" + custom.size() + ")");
            }
        }
    };
    private TextView tv_destittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manage);
        init_UI();
        init_Data();
    }

    private void init_Data() {
        magneticData();
        //获取app数据
        getApp();

    }

    private void getApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                system = new ArrayList<AppInfo>();
                custom = new ArrayList<AppInfo>();
                //appInfo的分类
                appInfoList = AppManage.getAppInfoList(getApplicationContext());
                for (AppInfo appInfo : appInfoList) {
                    if (appInfo.isSystem) {
                        system.add(appInfo);
                    } else {
                        custom.add(appInfo);
                    }
                }
                if (appInfoList != null) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    //磁盘数据的初始化
    private void magneticData() {
        String path = Environment.getDataDirectory().getAbsolutePath();
        String sd_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        float magnetic = changeData(getAvailableSpace(path));
        float sdspace = changeData(getAvailableSpace(sd_path));
        tv_magnetic.setText("磁盘可用：" + String.valueOf(magnetic) + "GB");
        tv_sd.setText("sd卡可用：" + sdspace + "GB");
    }

    //获取可用空间大小
    private long getAvailableSpace(String path) {
        StatFs statFs = new StatFs(path);
        long count = statFs.getAvailableBlocks();
        long size = statFs.getBlockSize();
        return count * size;
    }

    //转换数据
    private float changeData(long data) {
        Float floatfata = Float.valueOf(data);
        return floatfata / (1024 * 1024 * 1024);
    }


    //UI初始化
    private void init_UI() {
        tv_magnetic = (TextView) findViewById(R.id.tv_magnetic);
        tv_sd = (TextView) findViewById(R.id.tv_sd);
        lv_appmanage = (ListView) findViewById(R.id.lv_appmanage);
        lv_appmanage.setOnScrollListener(this);
        tv_destittle = (TextView) findViewById(R.id.tv_destittle);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (system != null && custom != null) {
            if (firstVisibleItem > custom.size()) {

                tv_destittle.setText("系统应用" + "(" + system.size() + ")");
            }
            else{
                tv_destittle.setText("用户应用" + "(" + custom.size() + ")");
            }
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getItemViewType(int position) {
            if (position == custom.size()) {
                return 0;
            } else {
                return 1;
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

        @Override
        public int getCount() {
            return custom.size() + system.size() + 1;
        }

        @Override
        public AppInfo getItem(int position) {
            if (position == custom.size()) {
                return null;
            } else {
                if (position < custom.size()) {
                    return custom.get(position);
                } else {
                    return system.get(position - custom.size() - 1);
                }

            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
//            //设置标题
            if (type == 0) {
                convertView = View.inflate(getApplicationContext(), R.layout.app_tittle_layout, null);
                TextView tv_apptittle = convertView.findViewById(R.id.tv_apptittle);
                tv_apptittle.setText("系统应用" + "(" + system.size() + ")");
                return convertView;
            } else {
                //设置appitem
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = View.inflate(getApplicationContext(), R.layout.app_item_layout, null);
                    viewHolder = new ViewHolder();
                    viewHolder.iv_appicon = convertView.findViewById(R.id.iv_appicon);
                    viewHolder.tv_appname = convertView.findViewById(R.id.tv_appname);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                viewHolder.iv_appicon.setBackground(getItem(position).getIcon());
                viewHolder.tv_appname.setText(getItem(position).getAppName());
                return convertView;

                //  }
            }
        }
    }

    static class ViewHolder {
        TextView tv_appname;

        ImageView iv_appicon;
    }


}
