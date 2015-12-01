package com.example.developmc.smsvalidate;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by developmc on 15/11/30.
 */
public class SmsObserver extends ContentObserver {
    private Context mContext ;
    private Handler mHandler ;
    public SmsObserver(Context context ,Handler handler){
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    //回调时自动调用
    @Override
    public void onChange(boolean selfChange,Uri uri) {
        super.onChange(selfChange,uri);
        String code = "" ;
        //first action : do nothing
        if(uri.toString().equals("content://sms/raw")){
            return ;
        }
        Uri inboxUri = uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(uri,null,null,null,"date desc");
        if(cursor!=null){
            if(cursor.moveToFirst()){
                //address是发送短信的号码，可依据手机号码过滤验证码
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String body = cursor.getString(cursor.getColumnIndex("body")) ;

                Pattern pattern = Pattern.compile("(\\d{6})") ;
                Matcher matcher = pattern.matcher(body);
                if(matcher.find()){
                    //可能有多个匹配，取第一个
                    code = matcher.group(0) ;
                    //发送消息，同时将相关内容发送过去
                    mHandler.obtainMessage(MainActivity.MSG_RECEIVED_CODE,code).sendToTarget();
                }
            }
            cursor.close();
        }
    }
}
