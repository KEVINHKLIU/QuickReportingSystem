package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//警察單位->災情類別(重大刑案)->選擇災情的頁面
public class police_unit_case1 extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_unit_case1);

        Button button_police_case1_No1 = (Button) findViewById(R.id.強盜);
        Button button_police_case1_No2 = (Button) findViewById(R.id.搶奪);
        Button button_police_case1_No3 = (Button) findViewById(R.id.槍擊);
        Button button_police_case1_No4 = (Button) findViewById(R.id.擄人勒贖);
        Button button_police_case1_No5 = (Button) findViewById(R.id.殺人未遂);
        Button button_police_case1_No6 = (Button) findViewById(R.id.故意殺人);
        Button button_police_case1_No7 = (Button) findViewById(R.id.強制性交);
        Button button_police_case1_No8 = (Button) findViewById(R.id.重大恐嚇取財);
        Button button_police_case1_No9 = (Button) findViewById(R.id.重傷害);
        Button button_police_case1_other = (Button) findViewById(R.id.其它);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");

        button_police_case1_No1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "強盜";
                activityJump();
            }
        });

        button_police_case1_No2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "搶奪";
                activityJump();
            }
        });

        button_police_case1_No3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "槍擊";
                activityJump();
            }
        });

        button_police_case1_No4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "擄人勒贖";
                activityJump();
            }
        });

        button_police_case1_No5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "殺人未遂";
                activityJump();
            }
        });

        button_police_case1_No6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "故意殺人";
                activityJump();
            }
        });

        button_police_case1_No7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "強制性交";
                activityJump();
            }
        });

        button_police_case1_No8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "重大恐嚇取財";
                activityJump();
            }
        });

        button_police_case1_No9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "重傷害";
                activityJump();
            }
        });

        button_police_case1_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                unit_case_case = editText.getText().toString();
                activityJump();

            }
        });
    }

    public void activityJump()
    {
        Intent intent = new Intent();
        intent.setClass(police_unit_case1.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        police_unit_case1.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(police_unit_case1.this, police_unit.class);
            startActivity(intent);
            police_unit_case1.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
