package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

//在警察單位選擇災情類別的頁面
public class police_unit extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    Intent intentJump = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_unit);
        Button button_police_case1 = (Button) findViewById(R.id.重大刑案);
        Button button_police_case2 = (Button) findViewById(R.id.一般刑案);
        Button button_police_case3 = (Button) findViewById(R.id.為民服務1);
        Button button_police_case4 = (Button) findViewById(R.id.交通事故);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");

        button_police_case1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case = "重大刑案";
                intentJump.setClass(police_unit.this, police_unit_case1.class);
                activityJump();
            }
        });

        button_police_case2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case = "一般刑案";
                intentJump.setClass(police_unit.this, police_unit_case2.class);
                activityJump();
            }
        });

        button_police_case3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case = "為民服務";
                intentJump.setClass(police_unit.this, police_unit_case3.class);
                activityJump();

            }
        });

        button_police_case4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                unit_case = "交通事故";
                intent.setClass(police_unit.this, camera.class);
                intent.putExtra("injuries_number", injuries_number);
                intent.putExtra("inform_unit", inform_unit);
                intent.putExtra("unit_case", unit_case);
                intent.putExtra("unit_case_case", "交通事故");
                startActivity(intent);
                police_unit.this.finish();
            }
        });

    }

    public void activityJump(){


        intentJump.putExtra("injuries_number", injuries_number);
        intentJump.putExtra("inform_unit", inform_unit);
        intentJump.putExtra("unit_case", unit_case);
        startActivity(intentJump);
        police_unit.this.finish();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(police_unit.this, inform_unit.class);
            startActivity(intent);
            police_unit.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

