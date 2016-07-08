package app.tomlai.com.ambulancereportingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


public class child extends AppCompatActivity {
    private String ambulanceID;
    private String ambulanceSTID;
    private String reportID;
    private String major;
    private String trace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = this.getIntent();
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");
        reportID= intent.getStringExtra("reportID");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");



        Button child = (Button) findViewById(R.id.新生兒);
        child.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(child.this, InjuredInfo.class);
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("term_child", "1");
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);
                startActivity(intent);
                child.this.finish();
            }
        });


        Button nonChild = (Button) findViewById(R.id.非新生兒);
        nonChild.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(child.this, InjuredInfo.class);
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("term_child", "0");
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);
                startActivity(intent);
                child.this.finish();
            }
        });


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
        AlertDialog.Builder ad=new AlertDialog.Builder(child.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                child.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }
}

