package com.example.wuye.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wuye.appsafe.R;

import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;

/**
 * Created by WUYE on 2018/8/15.
 */

public class SettingclickView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.wuye.appsafe";
    private TextView tv_tittle;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;
    private View view;

    public SettingclickView(Context context) {
        this(context, null);
    }

    public SettingclickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SettingclickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        view = View.inflate(context, R.layout.settingclick, this);
        tv_tittle = (TextView) findViewById(R.id.tv_tittle);
        tv_des = (TextView) findViewById(R.id.tv_des);
        initAttrs(attrs);
        tv_tittle.setText(mDestitle);
        tv_des.setText(mDeson);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

    }

    public void setDes(String des){
        tv_des.setText(des);
    }

}
