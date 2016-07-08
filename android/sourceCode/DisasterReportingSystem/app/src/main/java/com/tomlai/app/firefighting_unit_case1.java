package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//消防單位->災情類別(天然災害)->選擇災情的頁面
public class firefighting_unit_case1 extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firefighting_unit_case1);
        Button button_firefighting_case1_No1 = (Button) findViewById(R.id.積水地區);
        Button button_firefighting_case1_No2 = (Button) findViewById(R.id.天然災害);
        Button button_firefighting_case1_No3 = (Button) findViewById(R.id.路樹傾倒);
        Button button_firefighting_case1_No4 = (Button) findViewById(R.id.房屋淹水);
        Button button_firefighting_case1_No5 = (Button) findViewById(R.id.招牌掉落);
        Button button_firefighting_case1_No6 = (Button) findViewById(R.id.土石);
        Button button_firefighting_case1_other = (Button) findViewById(R.id.其它);


        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");

        button_firefighting_case1_No1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "積水地區";
                activityJump();
            }
        });

        button_firefighting_case1_No2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "天然災害";
                activityJump();
            }
        });

        button_firefighting_case1_No3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "路樹傾倒";
                activityJump();
            }
        });

        button_firefighting_case1_No4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "房屋淹水";
                activityJump();
            }
        });

        button_firefighting_case1_No5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "招牌掉落";
                activityJump();
            }
        });


        button_firefighting_case1_No6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unit_case_case = "土石";
                activityJump();
            }
        });

        button_firefighting_case1_other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText editText = (EditText)findViewById(R.id.editText);
                unit_case_case = editText.getText().toString();
                activityJump();

            }
        });
    }

    public void activityJump() {
        Intent intent = new Intent();
        intent.setClass(firefighting_unit_case1.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        firefighting_unit_case1.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(firefighting_unit_case1.this, firefighting_unit.class);
            startActivity(intent);
            firefighting_unit_case1.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

