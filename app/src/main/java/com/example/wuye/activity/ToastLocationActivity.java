package com.example.wuye.activity;

import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.wuye.appsafe.R;
import com.example.wuye.util.ConstantUtil;
import com.example.wuye.util.SpUtil;

public class ToastLocationActivity extends AppCompatActivity implements View.OnTouchListener{
    private ImageView iv_drag;
    private Button button_top;
    private Button button_buttom;
    private PointF startpoint=new PointF();
    private WindowManager mWM;
    private int mScreenHeight;
    private int mScreenWidth;
    private    int locatin_x;
    private    int locatin_y;
    private int startX;
    private int startY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        init_UI();
    }

    private void init_UI() {
        mWM = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWM.getDefaultDisplay().getHeight();
        mScreenWidth = mWM.getDefaultDisplay().getWidth();
        iv_drag=findViewById(R.id.iv_drag);
        button_top=findViewById(R.id.top_button);
        button_buttom=findViewById(R.id.buttom_button);
        iv_drag.setOnTouchListener(this);
        locatin_x=SpUtil.getInt(this,ConstantUtil.LOCATION_X,locatin_x);
        locatin_y=SpUtil.getInt(this,ConstantUtil.LOCATION_Y,locatin_y);
       //iv_drag.layout(locatin_x,locatin_y,locatin_x+iv_drag.getMeasuredWidth(),locatin_y+iv_drag.getMeasuredHeight());
        //左上角坐标作用在iv_drag上
        //iv_drag在相对布局中,所以其所在位置的规则需要由相对布局提供

        //指定宽高都为WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //将左上角的坐标作用在iv_drag对应规则参数上
        layoutParams.leftMargin = locatin_x;
        layoutParams.topMargin = locatin_y;
        //将以上规则作用在iv_drag上
        iv_drag.setLayoutParams(layoutParams);
        if(locatin_y>mScreenHeight/2){
            button_buttom.setVisibility(View.INVISIBLE);
            button_top.setVisibility(View.VISIBLE);
        }else{
            button_buttom.setVisibility(View.VISIBLE);
            button_top.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX= (int) event.getRawX();
                startY= (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx=  ((int) (event.getRawX())-startX);
                int dy=  ((int) (event.getRawY())-startY);

                //1,当前控件所在屏幕的(左,上)角的位置
                int left = iv_drag.getLeft()+dx;//左侧坐标
                int top = iv_drag.getTop()+dy;//顶端坐标
                int right = iv_drag.getRight()+dx;//右侧坐标
                int bottom = iv_drag.getBottom()+dy;//底部坐标
                if(left<0){
                    return true;
                }

                //右边边缘不能超出屏幕
                if(right>mScreenWidth){
                    return true;
                }

                //上边缘不能超出屏幕可现实区域
                if(top<0){
                    return true;
                }

                //下边缘(屏幕的高度-22 = 底边缘显示最大值)
                if( bottom>mScreenHeight - 22){
                    return true;
                }
                iv_drag.layout(left,top,right,bottom);
                if(top>mScreenHeight/2){
                    button_buttom.setVisibility(View.INVISIBLE);
                    button_top.setVisibility(View.VISIBLE);
                }else{
                    button_buttom.setVisibility(View.VISIBLE);
                    button_top.setVisibility(View.INVISIBLE);
                }
                startX= (int) event.getRawX();
                startY= (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
        SpUtil.putInt(this, ConstantUtil.LOCATION_X, iv_drag.getLeft());
        SpUtil.putInt(this, ConstantUtil.LOCATION_Y, iv_drag.getTop());
                break;
        }



//        if(event.getAction()==MotionEvent.ACTION_DOWN){
//            startX= (int) event.getX();
//            startY= (int) event.getY();
//            return  true;
//        }
//        if(event.getAction()==MotionEvent.ACTION_MOVE){
//            int dx=  ((int) (event.getX())-startX);
//            int dy=  ((int) (event.getY())-startY);
//
//            //1,当前控件所在屏幕的(左,上)角的位置
//            int left = iv_drag.getLeft()+dx;//左侧坐标
//            int top = iv_drag.getTop()+dy;//顶端坐标
//            int right = iv_drag.getRight()+dx;//右侧坐标
//            int bottom = iv_drag.getBottom()+dy;//底部坐标
//            if(left<0){
//                return true;
//            }
//
//            //右边边缘不能超出屏幕
//            if(right>mScreenWidth){
//                return true;
//            }
//
//            //上边缘不能超出屏幕可现实区域
//            if(top<0){
//                return true;
//            }
//
//            //下边缘(屏幕的高度-22 = 底边缘显示最大值)
//            if( bottom>mScreenHeight - 22){
//                return true;
//            }
//            iv_drag.layout(left,top,right,bottom);
//            if(top>mScreenHeight/2){
//                button_buttom.setVisibility(View.INVISIBLE);
//                button_top.setVisibility(View.VISIBLE);
//            }else{
//                button_buttom.setVisibility(View.VISIBLE);
//                button_top.setVisibility(View.INVISIBLE);
//            }
//        }
//        if(event.getAction()==MotionEvent.ACTION_UP){
//            SpUtil.putInt(this, ConstantUtil.LOCATION_X, iv_drag.getLeft());
//            SpUtil.putInt(this, ConstantUtil.LOCATION_Y, iv_drag.getTop());
//        }
        return true;
    }
}
