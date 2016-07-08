package com.tomlai.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class emergy extends AppCompatActivity {
    private String inform_unit;
    private String injuries_number;
    private String unit_case;
    private String unit_case_case;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergy);

        Button emergy_case1 = (Button) findViewById(R.id.單純救護);
        Button emergy_case2 = (Button) findViewById(R.id.警察);
        Button emergy_case3 = (Button) findViewById(R.id.消防);
        Button emergy_case4 = (Button) findViewById(R.id.警察和消防);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        unit_case = "None";
        unit_case_case = "None";

        emergy_case1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inform_unit = "救護";
                activityJump();
            }
        });

        emergy_case2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inform_unit = "救護,警察";
                activityJump();
            }
        });

        emergy_case3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inform_unit = "救護,消防";
                activityJump();
            }
        });

        emergy_case4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inform_unit = "救護,消防,警察";
                activityJump();
            }
        });
}

    public void activityJump()
    {
        Intent intent = new Intent();
        intent.setClass(emergy.this, camera.class);
        intent.putExtra("injuries_number", injuries_number);
        intent.putExtra("inform_unit", inform_unit);
        intent.putExtra("unit_case", unit_case);
        intent.putExtra("unit_case_case", unit_case_case);
        startActivity(intent);
        emergy.this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(emergy.this, MainActivity.class);
            startActivity(intent);
            emergy.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
