package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//警察單位->災情類別(一般刑案)->選擇災情的頁面
public class police_unit_case2 extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_unit_case2);
        Button button_police_case2_No1 = (Button) findViewById(R.id.傷害);
        Button button_police_case2_No2 = (Button) findViewById(R.id.毀損);
        Button button_police_case2_No3 = (Button) findViewById(R.id.猥褻);
        Button button_police_case2_No4 = (Button) findViewById(R.id.恐嚇取財);
        Button button_police_case2_No5 = (Button) findViewById(R.id.一般竊盜);
        Button button_police_case2_No6 = (Button) findViewById(R.id.汽機車竊盜);
        Button button_police_case2_No7 = (Button) findViewById(R.id.詐欺賭博);
        Button button_police_case2_No8 = (Button) findViewById(R.id.公共安全);
        Button button_police_case2_No9 = (Button) findViewById(R.id.煙毒);
        Button button_police_case2_No10 = (Button) findViewById(R.id.妨礙自由);
        Button button_police_case2_No11 = (Button) findViewById(R.id.妨礙風化);
        Button button_police_case2_other = (Button) findViewById(R.id.其它);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");

        button_police_case2_No1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "傷害";
                activityJump();
            }
        });

        button_police_case2_No2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "毀損";
                activityJump();
            }
        });

        button_police_case2_No3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "猥褻";
                activityJump();
            }
        });

        button_police_case2_No4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "恐嚇取財";
                activityJump();
            }
        });

        button_police_case2_No5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "一般竊盜";
                activityJump();
            }
        });

        button_police_case2_No6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "汽機車竊盜";
                activityJump();
            }
        });

        button_police_case2_No7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "詐欺賭博";
                activityJump();
            }
        });

        button_police_case2_No8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "公共安全";
                activityJump();
            }
        });

        button_police_case2_No9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "煙毒";
                activityJump();
            }
        });

        button_police_case2_No10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "妨礙自由";
                activityJump();
            }
        });

        button_police_case2_No11.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "妨礙風化";
                activityJump();
            }
        });

        button_police_case2_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                unit_case_case = editText.getText().toString();
                activityJump();
            }
        });
    }

    public void activityJump() {
        Intent intent = new Intent();
        intent.setClass(police_unit_case2.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        police_unit_case2.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(police_unit_case2.this, police_unit.class);
            startActivity(intent);
            police_unit_case2.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

