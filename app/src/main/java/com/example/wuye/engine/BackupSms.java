package com.example.wuye.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by WUYE on 2018/9/5.
 */

public class BackupSms {

    private static final int COUNT = 100;
    private static final int PROGRESS = 101;


    public static void getSms(Context context, String path,Handler handler,CallBack callBack){
           int index=0;
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address", "date", "type", "body"}
                , null, null, null);
        File file=new File(path);
        FileOutputStream fileOutputStream=null;
        try {
            fileOutputStream=new FileOutputStream(file);
            XmlSerializer xmlSerializer= Xml.newSerializer();
            xmlSerializer.setOutput(fileOutputStream,"utf-8");
            xmlSerializer.startDocument("utf-8",true);
            xmlSerializer.startTag(null,"smss");
            callBack.setMAX(cursor.getCount());
           // progressBar.setMax(cursor.getCount());
            Message message=Message.obtain();
            message.what=COUNT;
            String count=String.valueOf(cursor.getCount());
            message.obj=count;
            handler.sendMessage(message);

            while(cursor.moveToNext()){
                xmlSerializer.startTag(null,"sms");

                xmlSerializer.startTag(null,"address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null,"address");

                xmlSerializer.startTag(null,"date");
                xmlSerializer.text(cursor.getString(1));
                xmlSerializer.endTag(null,"date");

                xmlSerializer.startTag(null,"type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null,"type");

                xmlSerializer.startTag(null,"body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null,"body");

                xmlSerializer.endTag(null,"sms");


                index++;
                Thread.sleep(500);
              //  progressBar.setProgress(index);
                callBack.setProgress(index);
                Message message1=Message.obtain();
                message1.what=PROGRESS;
                String progress=String.valueOf(index);
                message1.obj=progress;
                handler.sendMessage(message1);

            }
            xmlSerializer.endTag(null,"smss");
            xmlSerializer.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(fileOutputStream!=null&&cursor!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cursor.close();
            }
        }

    }
   public interface CallBack{
        //设置最大值
        public void setMAX(int index);
        //设置进度
        public void setProgress(int progress);
    }

}
