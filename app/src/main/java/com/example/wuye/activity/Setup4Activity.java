package com.example.wuye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.ToastUtil;

public class Setup4Activity extends BaseSetupActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        init_UI();
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);

    }

    @Override
    protected void showNextPage() {
        Boolean isCheck= SpUtil.getBoolean(this,ConstantUtil.SETUPOVER,false);
        if (isCheck) {
            Intent intent = new Intent(this, SetupOverActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.next_in, R.anim.next_out);
        }
        else{
            ToastUtil.show(this,"您还没有完成设置");
        }


        //开启平移动画


    }

    private void init_UI() {
        boolean key = SpUtil.getBoolean(getApplicationContext(), ConstantUtil.SETUPOVER, false);
        final CheckBox checkBox=(CheckBox) findViewById(R.id.cb_checkbok);

        if(key){

            checkBox.setText("您已经开启防盗保护");
            checkBox.setChecked(key);
        }
        else{
            checkBox.setText("您没有开启防盗保护");
            checkBox.setChecked(key);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isCheck = checkBox.isChecked();
                if(isCheck){
                    checkBox.setText("您已经开启防盗保护");
                    checkBox.setChecked(isCheck);
                    SpUtil.putBoolean(getApplicationContext(), ConstantUtil.SETUPOVER,true);
                }
                else{
                    checkBox.setText("您没有开启防盗保护");
                    checkBox.setChecked(isCheck);
                    SpUtil.putBoolean(getApplicationContext(), ConstantUtil.SETUPOVER,false);
                }
            }
        });
    }


}
