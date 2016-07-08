package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

//在選擇通報單位的頁面
public class inform_unit extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_unit);

        Button button_police = (Button) findViewById(R.id.警察單位);
        Button button_firefighting = (Button) findViewById(R.id.消防單位);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");

        button_police.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                inform_unit = "警察";
                intent.setClass(inform_unit.this, police_unit.class);
                intent.putExtra("injuries_number", injuries_number);
                intent.putExtra("inform_unit", inform_unit);
                startActivity(intent);
                inform_unit.this.finish();
            }
        });

        button_firefighting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                inform_unit = "消防";
                intent.setClass(inform_unit.this, firefighting_unit.class);
                intent.putExtra("injuries_number", injuries_number);
                intent.putExtra("inform_unit", inform_unit);
                startActivity(intent);
                inform_unit.this.finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(inform_unit.this, MainActivity.class);
            startActivity(intent);
            inform_unit.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
