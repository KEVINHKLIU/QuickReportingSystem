package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//消防單位->災情類別(緊急救護)->選擇災情的頁面
public class firefighting_unit_case3 extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firefighting_unit_case3);
        Button button_firefighting_case3_No1 = (Button) findViewById(R.id.溺水);
        Button button_firefighting_case3_No2 = (Button) findViewById(R.id.急病);
        Button button_firefighting_case3_No3 = (Button) findViewById(R.id.精神異常);
        Button button_firefighting_case3_No4 = (Button) findViewById(R.id.癲癇);
        Button button_firefighting_case3_No5 = (Button) findViewById(R.id.孕婦急產);
        Button button_firefighting_case3_No6 = (Button) findViewById(R.id.中毒相關);
        Button button_firefighting_case3_other = (Button) findViewById(R.id.其它);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");

        button_firefighting_case3_No1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "溺水";
                activityJump();
            }
        });

        button_firefighting_case3_No2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "急病";
                activityJump();
            }
        });

        button_firefighting_case3_No3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "精神異常";
                activityJump();
            }
        });

        button_firefighting_case3_No4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "癲癇";
                activityJump();
            }
        });

        button_firefighting_case3_No5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "孕婦急產";
                activityJump();
            }
        });

        button_firefighting_case3_No6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "中毒相關";
                activityJump();
            }
        });

        button_firefighting_case3_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                unit_case_case = editText.getText().toString();
                activityJump();
            }
        });
    }

    public void activityJump() {
        Intent intent = new Intent();
        intent.setClass(firefighting_unit_case3.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        firefighting_unit_case3.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(firefighting_unit_case3.this, firefighting_unit.class);
            startActivity(intent);
            firefighting_unit_case3.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

