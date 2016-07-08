package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//消防單位->災情類別(為民服務)->選擇災情的頁面
public class firefighting_unit_case2 extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firefighting_unit_case2);
        Button button_firefighting_case2_No1 = (Button) findViewById(R.id.抓蜂);
        Button button_firefighting_case2_No2 = (Button) findViewById(R.id.捕蛇);
        Button button_firefighting_case2_No3 = (Button) findViewById(R.id.開鎖破門);
        Button button_firefighting_case2_No4 = (Button) findViewById(R.id.救貓狗);
        Button button_firefighting_case2_No5 = (Button) findViewById(R.id.受困案件);
        Button button_firefighting_case2_other = (Button) findViewById(R.id.其它);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");

        button_firefighting_case2_No1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "抓蜂";
                activityJump();
            }
        });

        button_firefighting_case2_No2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "捕蛇";
                activityJump();
            }
        });

        button_firefighting_case2_No3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "開鎖破門";
                activityJump();
            }
        });

        button_firefighting_case2_No4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "救貓狗";
                activityJump();
            }
        });

        button_firefighting_case2_No5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "受困案件";
                activityJump();
            }
        });

        button_firefighting_case2_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                unit_case_case = editText.getText().toString();
                activityJump();

            }
        });
    }

    public void activityJump() {
        Intent intent = new Intent();
        intent.setClass(firefighting_unit_case2.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        firefighting_unit_case2.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(firefighting_unit_case2.this, firefighting_unit.class);
            startActivity(intent);
            firefighting_unit_case2.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

