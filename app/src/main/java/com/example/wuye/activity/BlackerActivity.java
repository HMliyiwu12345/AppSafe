package com.example.wuye.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.db.BlackNumberInfo;
import com.example.wuye.db.dao.BlackNumberOpenHelperDao;

import java.util.List;

public class BlackerActivity extends AppCompatActivity implements View.OnClickListener {
    private Button add;
    private ListView lv_blacker;
    private BlackNumberOpenHelperDao dao;
    private List<BlackNumberInfo> inquiry;
    private int mCount;
    private MyAdapter myAdapter;
    private boolean mCurrentload=false;
    private int mode = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mCurrentload=false;
            if (myAdapter == null) {
                myAdapter = new MyAdapter();
                lv_blacker.setAdapter(myAdapter);
            } else {
                myAdapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blacker);
        init_Data();
        init_UI();
        add.setOnClickListener(this);

    }

    private void init_Data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dao = BlackNumberOpenHelperDao.getInstance(getApplicationContext());
                inquiry = dao.find(0);
                mCount=dao.getCount();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    //UI初始化
    private void init_UI() {
        add = (Button) findViewById(R.id.add);
        lv_blacker = (ListView) findViewById(R.id.lv_blacker);
        lv_blacker.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
      //          AbsListView.OnScrollListener.SCROLL_STATE_FLING   飞速转动阶段
       //         AbsListView.OnScrollListener.SCROLL_STATE_IDLE     空闲阶段
//                AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL   拿手去触摸滚动阶段
                if(inquiry!=null) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&
                            lv_blacker.getLastVisiblePosition() >= inquiry.size() - 1&&!mCurrentload) {
                        if (inquiry.size() < mCount) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    mCurrentload=true;
                                    dao = BlackNumberOpenHelperDao.getInstance(getApplicationContext());
                                    List<BlackNumberInfo> inquirymore = dao.find(inquiry.size());
                                    inquiry.addAll(inquirymore);
                                    mHandler.sendEmptyMessage(0);
                                }
                            }).start();
                        }

                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(BlackerActivity.this);
        final AlertDialog alertDialog = dialog.create();
        final View view = View.inflate(this, R.layout.blackerdialog, null);
        final Button postive = (Button) view.findViewById(R.id.postive2);
        final Button nagtive = (Button) view.findViewById(R.id.nagtive2);
        final EditText et_blacker = (EditText) view.findViewById(R.id.et_blacker);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        final RadioButton rd_phone = (RadioButton) view.findViewById(R.id.rb_phone);
        final RadioButton rd_message = (RadioButton) view.findViewById(R.id.rb_message);
        final RadioButton rd_all = (RadioButton) view.findViewById(R.id.rb_all);
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_message:
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        mode = 3;
                        break;
                }

            }
        });

        postive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_blacker.getText().toString().trim();
                dao.insert(phone, String.valueOf(mode));
                BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
                blackNumberInfo.setMode(String.valueOf(mode));
                blackNumberInfo.setPhone(phone);
                inquiry.add(0,blackNumberInfo);
                myAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
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
            return inquiry.size();
        }

        @Override
        public Object getItem(int position) {
            return inquiry.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.blacker_view_layout, null);
                viewHolder=new ViewHolder();
                viewHolder.tv_phone=(TextView) convertView.findViewById(R.id.tv_blacker_phone);
                viewHolder.tv_mode=convertView.findViewById(R.id.tv_blacker_mode);
                viewHolder.iv_delete=(ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            }
            else{
               viewHolder=(ViewHolder) convertView.getTag();
            }

            viewHolder.tv_phone.setText(inquiry.get(position).phone);
            switch (Integer.parseInt(inquiry.get(position).mode)) {
                case 1:
                    viewHolder.tv_mode.setText("短信");
                    break;
                case 2:
                    viewHolder.tv_mode.setText("电话");
                    break;
                case 3:
                    viewHolder.tv_mode.setText("全部");
                    break;
            }
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone1 = inquiry.get(position).phone;
                    dao.delete(phone1);
                    inquiry.remove(inquiry.get(position));
                    myAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
    }

     class  ViewHolder {
         TextView tv_phone;
         TextView tv_mode;
         ImageView iv_delete;
    }
}
