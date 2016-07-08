package app.tomlai.com.ambulancereportingsystem;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class InjuredInfo extends AppCompatActivity {
    private String ambulanceID;
    private String ambulanceSTID;
    private String reportID;
    private String term_child;
    private String major;
    private String trace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injured_info);

        Intent intent = this.getIntent();
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");
        reportID= intent.getStringExtra("reportID");
        term_child = intent.getStringExtra("term_child");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");

        Button burn = (Button) findViewById(R.id.燒傷);
        burn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InjuredInfo.this, burnInfo.class);
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("term_child", term_child);
                intent.putExtra("injuredType", "燒傷");
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);
                startActivity(intent);
                InjuredInfo.this.finish();
            }
        });


        Button birth = (Button) findViewById(R.id.生產);
        birth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(InjuredInfo.this, hospitalInfo.class);
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("term_child", term_child);
                intent.putExtra("injuredType", "生產");
                intent.putExtra("injuredInfo_case1", "0");
                intent.putExtra("injuredInfo_case2", "0");
                intent.putExtra("injuredInfo_case3", "0");
                intent.putExtra("injuredInfo_case4", "0");
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);
                startActivity(intent);
                InjuredInfo.this.finish();
            }
        });

        Button other = (Button) findViewById(R.id.其他);
        other.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InjuredInfo.this, otherInfo.class);
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("term_child", term_child);
                intent.putExtra("injuredType", "其他");
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);
                startActivity(intent);
                InjuredInfo.this.finish();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(InjuredInfo.this, child.class);
            intent.putExtra("ambulanceSTID", ambulanceSTID);
            intent.putExtra("ambulanceID", ambulanceID);
            intent.putExtra("reportID", reportID);
            intent.putExtra("major", major);
            intent.putExtra("trace", trace);
            startActivity(intent);
            InjuredInfo.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
