package com.example.developmc.smsvalidate;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final int MSG_RECEIVED_CODE = 1;
    private SmsObserver mObserver ;
    private EditText et_code ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case MSG_RECEIVED_CODE:
                    String code = (String)msg.obj ;
                    et_code.setText(code);
                    break ;
            }
        }
    } ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_code = (EditText)findViewById(R.id.et_code) ;
        //注册短信监听
        mObserver = new SmsObserver(MainActivity.this,mHandler) ;
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri,true,mObserver);
    }
    //销毁的时候取消注册
    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mObserver);
    }
}
