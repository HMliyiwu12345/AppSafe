package com.example.wuye.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;

public class SetupOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setuoover = SpUtil.getBoolean(this, ConstantUtil.SETUPOVER, false);
        if(setuoover) {
            setContentView(R.layout.activity_setup_over);

            init();
        }
        else{
            Intent intent=new Intent(this,Setup1Activity.class);
            startActivity(intent);
            finish();
        }

    }

    private void init() {
        String  contact_phone = SpUtil.getString(this, ConstantUtil.CONTACT_PHONE, "");
        TextView textView1=(TextView) findViewById(R.id.tv_contact_phone);
        textView1.setText(contact_phone);
        TextView textView=(TextView) findViewById(R.id.tv_refrsh_setup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Setup1Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
