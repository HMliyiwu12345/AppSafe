package com.example.wuye.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wuye.appsafe.R;

/**
 * Created by WUYE on 2018/7/2.
 */

public class SettingitemView extends RelativeLayout {
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.wuye.appsafe";
    private CheckBox cb_checkbook;
    private TextView tv_2;
    private String mDestitle;
    private String mDeson;
    private String mDesoff;

    public SettingitemView(Context context) {
        super(context);
        //  this(context,null);
        init(context,null);
    }

    public SettingitemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //this(context,null,0);
        init(context,attrs);
    }

    public SettingitemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    protected void init(Context context,AttributeSet attrs) {
        View view = inflate(context, R.layout.settingitem, this);
       TextView tv_tittle=(TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        cb_checkbook = (CheckBox) findViewById(R.id.cb_checkbok);
        initAttrs(attrs);
        tv_tittle.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
    }

    public boolean Checkbook() {
        return cb_checkbook.isChecked();
    }

    public void setCheck(boolean isCheck) {
        //当前条目在选择的过程中,cb_box选中状态也在跟随(isCheck)变化
        cb_checkbook.setChecked(isCheck);
        if (isCheck) {
            //开启
            tv_2.setText(mDeson);
        } else {
            //关闭
            tv_2.setText(mDesoff);
        }


    }
}
