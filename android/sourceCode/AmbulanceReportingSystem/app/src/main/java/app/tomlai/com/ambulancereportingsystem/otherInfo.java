package app.tomlai.com.ambulancereportingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

public class otherInfo extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private String ambulanceID;
    private String ambulanceSTID;
    private String reportID;
    private String term_child;
    private String injuredType;
    private String otherInfo_case1 = "0";
    private String otherInfo_case2 = "0";
    private String otherInfo_case3 = "0";
    private String otherInfo_case4 = "0";
    private ArrayList<CompoundButton> selected = new ArrayList<>();
    private String major;
    private String trace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_info);

        Intent intent = this.getIntent();
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");
        reportID= intent.getStringExtra("reportID");
        term_child = intent.getStringExtra("term_child");
        injuredType = intent.getStringExtra("injuredType");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");


        int[] chk_id = {R.id.精神異常, R.id.內外骨外, R.id.多重藥物過量或中毒};

        for(int id:chk_id){ // 用迴圈替所有核取方塊加上監聽物件
            CheckBox chk=(CheckBox) findViewById(id);
            chk.setOnCheckedChangeListener(this);}



        Button confirm = (Button) findViewById(R.id.確定);
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(CompoundButton i:selected)   // 以迴圈逐一將換行字元及
                {
                    if(i.getText().equals("精神異常"))   // 選項文字
                        otherInfo_case1 = "1";
                    else if(i.getText().equals("內外骨外"))
                        otherInfo_case2 = "1";
                    else
                        otherInfo_case3 = "1";
                }
                if(otherInfo_case1.equals("0") && otherInfo_case2.equals("0") && otherInfo_case3.equals("0"))
                    noChoiceNotice();
                else
                    activityJump();

            }
        });

        Button confirm_none = (Button) findViewById(R.id.無限制收治需求);
        confirm_none.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                otherInfo_case4 = "1";
                activityJump();
            }
        });

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked)   				// 若項目被選取
            selected.add(buttonView);   	// 加到集合之中
        else              					// 若項目被取消
            selected.remove(buttonView);	// 自集合中移除
    }

    private void activityJump()
    {
        Intent intent = new Intent();
        intent.setClass(otherInfo.this, hospitalInfo.class);
        intent.putExtra("ambulanceSTID", ambulanceSTID);
        intent.putExtra("ambulanceID", ambulanceID);
        intent.putExtra("reportID", reportID);
        intent.putExtra("term_child", term_child);
        intent.putExtra("injuredType", injuredType);
        intent.putExtra("injuredInfo_case1", otherInfo_case1);
        intent.putExtra("injuredInfo_case2", otherInfo_case2);
        intent.putExtra("injuredInfo_case3", otherInfo_case3);
        intent.putExtra("injuredInfo_case4", otherInfo_case4);
        intent.putExtra("major", major);
        intent.putExtra("trace", trace);
        startActivity(intent);
        otherInfo.this.finish();
    }


    public void noChoiceNotice(){  //checkBox 沒有選擇就按確定按鈕
        AlertDialog.Builder ad=new AlertDialog.Builder(otherInfo.this); //創建訊息方塊
        ad.setTitle("未選擇傷者概況通知");
        ad.setMessage("如未符合上述概況, 請按無限制收治需求按鍵");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則沒動作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(otherInfo.this, child.class);
            intent.putExtra("ambulanceSTID", ambulanceSTID);
            intent.putExtra("ambulanceID", ambulanceID);
            intent.putExtra("reportID", reportID);
            intent.putExtra("major", major);
            intent.putExtra("trace", trace);
            startActivity(intent);
            otherInfo.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
