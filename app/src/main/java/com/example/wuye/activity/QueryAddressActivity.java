package com.example.wuye.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.engine.AddressDao;


public class QueryAddressActivity extends AppCompatActivity {
    private static final int ADD = 1;
    private EditText editText;
    private TextView textView;
    private String inquiry = null;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == ADD) {

                textView.setText(inquiry);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);
        init_UI();

    }

    private void init_UI() {
        editText = (EditText) findViewById(R.id.et_query_address);
        textView = (TextView) findViewById(R.id.tv_query_address);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, final int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String adress = editText.getText().toString();
                        if (!TextUtils.isEmpty(adress)) {
                            Message message = new Message();
                            message.what = ADD;
                            inquiry = AddressDao.inquiry(adress);
                            mHandler.sendMessage(message);

                        }
                    }
                }).start();

            }
        });
    }
}
