package com.example.wuye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wuye.appsafe.R;

public class Setup1Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    protected void showPrePage() {

    }

    @Override
    protected void showNextPage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.next_in, R.anim.next_out);

    }


}
