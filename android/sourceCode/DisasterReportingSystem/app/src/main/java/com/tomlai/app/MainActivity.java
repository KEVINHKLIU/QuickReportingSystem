package com.tomlai.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
    private DisplayMetrics mPhone;
    private String injuries_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示

        //讀取手機解析度
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button6Up = (Button) findViewById(R.id.button6Up);

       LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if(!(status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))
        {

            // 無提供者, 顯示提示訊息
            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊
            ad.setTitle("偵測到未開啟GPS");
            ad.setMessage("請先開啟GPS?");
            ad.setPositiveButton("前往開啟", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
                public void onClick(DialogInterface dialog, int i) {
                    Intent intents = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intents);
                }
            });
            ad.show();//顯示訊息視窗
        }



      Intent intent = new Intent(MainActivity.this, GetLocation.class);  //啟動Service定位
       startService(intent);

        button0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                injuries_number = "0";
                intent.setClass(MainActivity.this, inform_unit.class);
                intent.putExtra("injuries_number", injuries_number);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "1";
                activityJump();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "2";
                activityJump();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "3";
                activityJump();

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "4";
                activityJump();
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "5";
                activityJump();
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                injuries_number = "6";
                activityJump();
            }
        });

        button6Up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                injuries_number = editText.getText().toString();
                activityJump();
            }
        });


    }

    public void activityJump()
    {
        Intent intent = new Intent();
      intent.putExtra("injuries_number", injuries_number);
        intent.setClass(MainActivity.this, emergy.class);
        startActivity(intent);
        MainActivity.this.finish();
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            ConfirmExit(); //呼叫ConfirmExit()函數
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                MainActivity.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }
}


