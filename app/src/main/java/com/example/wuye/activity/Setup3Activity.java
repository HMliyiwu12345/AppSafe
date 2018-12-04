package com.example.wuye.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;
import com.example.wuye.util.ToastUtil;

public class Setup3Activity extends  BaseSetupActivity implements View.OnClickListener {
    private Button button;
    private String tag = "Setup3Activity";
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        initUI();
        button.setOnClickListener(this);
    }

    @Override
    protected void showPrePage() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();
        //开启平移动画
        overridePendingTransition(R.anim.pre_in, R.anim.pre_out);

    }

    @Override
    protected void showNextPage() {
        String phone = editText.getText().toString();
        //  String contactPhone = SpUtil.getString(this, ConstantUtil.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            Intent intent = new Intent(this, Setup4Activity.class);
            startActivity(intent);
            finish();

            SpUtil.putString(this,ConstantUtil.CONTACT_PHONE,phone);
            //开启平移动画
            overridePendingTransition(R.anim.next_in, R.anim.next_out);
        } else {
            ToastUtil.show(this, "请输入联系人");
        }


    }

    private void initUI() {
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText);
        String phone = SpUtil.getString(this, ConstantUtil.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            editText.setText(phone);
        }
    }



    @Override
    public void onClick(View v) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.pkq);
        builder.setTitle("选择联系人");
        builder.setMessage("是否跳转到联系人选择手机号码");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  moveSetup3Activity();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveSetup3Activity();
            }
        });

        builder.show();


    }

    private void moveSetup3Activity() {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Uri contactUri = data.getData();             //该数据URI是一个指向用户所选联系人的定位符

                Cursor c = getContentResolver().query(contactUri, null, null, null, null); //查询联系人数据库，获得一个可用的Cursor

                if (c.moveToFirst()) {                        //将Cursor移动到第一条记录


                    String suspect = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));        //通过Cursor c获得联系人名字

                    String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));                      //通过Cursor c获得联系人id


                    Cursor c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,

                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null, null);

                    c2.moveToFirst();


                    String phonenum = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));   //通过Cursor c2获得联系人电话
                    editText.setText(phonenum);
                 //   SpUtil.putString(this, ConstantUtil.CONTACT_PHONE, phonenum);
                    c2.close();                    //关闭Cursor c2

                    c.close();


                }
            }
        }
    }
}
