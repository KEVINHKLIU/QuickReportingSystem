package app.tomlai.com.ambulancereportingsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class hospitalInfo extends AppCompatActivity {
    private String jumpGPS;
    private String destAddr;
    private String ambulanceID;
    private String ambulanceSTID;
    private String reportID;
    private String term_child;
    private String injuredType;
    private String injuredInfo_case1 = "0";
    private String injuredInfo_case2 = "0";
    private String injuredInfo_case3 = "0";
    private String injuredInfo_case4 = "0";
    public String responseURL = "http://140.113.72.125/lab01230322/response_ambulance.php";
    public String getHospitalURL = "http://140.113.72.125/lab01230322/response_hospitallist.php";
    private  String[] response;
    private Button hospital_1;
    private Button hospital_2;
    private Button hospital_3;
    private Button hospital_4;
    private Button hospital_5;
    private ProgressDialog  dialog;
    private Timer mTimer;
    private String major;
    private String trace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info);

        Intent intent = this.getIntent();
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");
        reportID= intent.getStringExtra("reportID");
        term_child = intent.getStringExtra("term_child");
        injuredType = intent.getStringExtra("injuredType");
        injuredInfo_case1 = intent.getStringExtra("injuredInfo_case1");
        injuredInfo_case2 = intent.getStringExtra("injuredInfo_case2");
        injuredInfo_case3 = intent.getStringExtra("injuredInfo_case3");
        injuredInfo_case4 = intent.getStringExtra("injuredInfo_case4");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");


        reportHospital(); //通報Server傷者概況進行篩選
        wait5();
        // init timer
        mTimer = new Timer();
        // start timer task
        setTimerTask();  //等待5秒再進雄呼叫


        hospital_1 = (Button) findViewById(R.id.H1);
        hospital_2 = (Button) findViewById(R.id.H2);
        hospital_3 = (Button) findViewById(R.id.H3);
        hospital_4 = (Button) findViewById(R.id.H4);
        hospital_5 = (Button) findViewById(R.id.H5);


        hospital_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destAddr = response[1];
                AskGPS();
            }
        });

        hospital_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destAddr = response[6];
                AskGPS();
            }
        });

        hospital_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destAddr = response[11];
                AskGPS();
            }
        });

        hospital_4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destAddr = response[16];
                AskGPS();
            }
        });

        hospital_5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destAddr = response[21];
                AskGPS();
            }
        });
    }

    public void reportHospital(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(hospitalInfo.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("condition", "condition");
                map.put("password", ambulanceID);
                map.put("reportID", reportID);
                map.put("term_child", term_child);
                map.put("term_none", injuredInfo_case4);
                map.put("category", injuredType);
                map.put("term_one", injuredInfo_case1);
                map.put("term_two", injuredInfo_case2);
                map.put("term_three", injuredInfo_case3);


                return map;
            }
        };
        requestQueue.add(request);
    }

    public void getHospitalRequest(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, getHospitalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!response.equals("0")) {  //Server更新
                            mTimer.cancel();
                            getHospital();
                        }
                        else
                            Toast.makeText(hospitalInfo.this, "Server篩選中,請稍候...", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(hospitalInfo.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> maps = new HashMap<>();
                maps.put("request", "request");
                maps.put("password", ambulanceID);
                maps.put("reportID", reportID);

                return maps;
            }
        };
        requestQueue.add(request);
    }

    public void getHospital(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, getHospitalURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responses) {
                        splitHospitalInfo(responses);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(hospitalInfo.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> mapss = new HashMap<>();
                mapss.put("getHosp", "getHosp");
                mapss.put("password", ambulanceID);
                mapss.put("reportID", reportID);

                return mapss;
            }
        };
        requestQueue.add(request);
    }








    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(hospitalInfo.this, child.class);
            intent.putExtra("ambulanceSTID", ambulanceSTID);
            intent.putExtra("ambulanceID", ambulanceID);
            intent.putExtra("reportID", reportID);
            intent.putExtra("major", major);
            intent.putExtra("trace", trace);
            startActivity(intent);
            hospitalInfo.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setTimerTask() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 5000, 2000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    /**
     * do some action
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    getHospitalRequest(); //取得醫院資訊
                    break;
                default:
                    break;
            }
        }
    };

    public void AskGPS()
    {
        AlertDialog.Builder ad=new AlertDialog.Builder(hospitalInfo.this); //創建訊息方塊
        ad.setTitle("導航功能");
        ad.setMessage("是否進行導航功能?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                jumpGPS = "是";
                activityJump();
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                jumpGPS = "否";
                activityJump();
            }
        });
        ad.show();//顯示訊息視窗
    }

    private void activityJump()
    {
        Intent intents = new Intent();
        intents.setClass(hospitalInfo.this, arrivalHospital.class);
        intents.putExtra("jumpGPS", jumpGPS);
        intents.putExtra("destAddr", destAddr);
        intents.putExtra("ambulanceSTID", ambulanceSTID);
        intents.putExtra("ambulanceID", ambulanceID);
        intents.putExtra("reportID", reportID);
        intents.putExtra("major", major);
        intents.putExtra("trace", trace);
        startActivity(intents);
        hospitalInfo.this.finish();
    }

    public void wait5() {
        dialog = ProgressDialog.show(hospitalInfo.this,
                "讀取中", "Server篩選中,請稍後",true);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    Thread.sleep(6000);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                finally{
                    dialog.dismiss();
                }
            }
        }).start();
    }

    private void splitHospitalInfo(String ServerResponse)
    {

        String str;
        response = ServerResponse.split("#");
        String hospitalNumStr = response[0];
        int hospitalNum = Integer.valueOf(hospitalNumStr);
        if(hospitalNum == 0)
            Toast.makeText(hospitalInfo.this, "目前無符合之醫院", Toast.LENGTH_SHORT).show();
        else
        {
            int countNum = 1;
            if(countNum <= hospitalNum)
            {
                str ="";
                for(int i = 2; i < 6; i++) {
                    if(i == 3)
                        str = str + "限制條件: ";
                    else if(i == 4)
                        str = str + "距離: ";
                    else if(i == 5) {
                        str = str + "病床資訊: ";
                        response[i] = bedsInfo(response[i]);
                    }
                    str = str + response[i] + "\n";
                }
                hospital_1.setVisibility(View.VISIBLE);
                hospital_1.setText(str);
                countNum++;
            }

            if(countNum <= hospitalNum)
            {
                str ="";
                for(int i = 7; i < 11; i++) {
                    if(i == 8)
                        str = str + "限制條件: ";
                    else if(i == 9)
                        str = str + "距離: ";
                    else if(i == 10) {
                        str = str + "病床資訊: ";
                        response[i] = bedsInfo(response[i]);
                    }
                    str = str + response[i] + "\n";
                }
                hospital_2.setVisibility(View.VISIBLE);
                hospital_2.setText(str);
                countNum++;
            }

            if(countNum <= hospitalNum)
            {
                str ="";
                for(int i = 12; i < 16; i++) {
                    if(i == 13)
                        str = str + "限制條件: ";
                    else if(i == 14)
                        str = str + "距離: ";
                    else if(i == 15) {
                        str = str + "病床資訊: ";
                        response[i] = bedsInfo(response[i]);
                    }
                    str = str + response[i] + "\n";
                }
                hospital_3.setVisibility(View.VISIBLE);
                hospital_3.setText(str);
                countNum++;
            }

            if(countNum <= hospitalNum)
            {
                str ="";
                for(int i = 17; i < 21; i++) {
                    if(i == 18)
                        str = str + "限制條件: ";
                    else if(i == 19)
                        str = str + "距離: ";
                    else if(i == 20) {
                        str = str + "病床資訊: ";
                        response[i] = bedsInfo(response[i]);
                    }
                    str = str + response[i] + "\n";
                }
                hospital_4.setVisibility(View.VISIBLE);
                hospital_4.setText(str);
                countNum++;
            }

            if(countNum <= hospitalNum)
            {
                str ="";
                for(int i = 22; i < 26; i++) {
                    if(i == 23)
                        str = str + "限制條件: ";
                    else if(i == 24)
                        str = str + "距離: ";
                    else if(i == 25) {
                        str = str + "病床資訊: ";
                        response[i] = bedsInfo(response[i]);
                    }
                    str = str + response[i] + "\n";
                }
                hospital_5.setVisibility(View.VISIBLE);
                hospital_5.setText(str);
                countNum++;
            }


        }

    }

    private static String bedsInfo(String bedsInfo)
    {
        if(bedsInfo.equals("not_full"))
            bedsInfo = "通報未滿,但無床位Info";
        else if(bedsInfo.equals("full"))
            bedsInfo = "通報已滿床";
        else if(bedsInfo.equals("no_child_bed"))
            bedsInfo = "無兒童病床,但有一般";
        else if(bedsInfo.equals("no_bed"))
            bedsInfo = "無兒童且無一般病床";
        else if(bedsInfo.equals("no_burn_bed"))
            bedsInfo = "未提供燒傷病床資訊";
        else if(bedsInfo.equals("no_OBS_bed"))
            bedsInfo = "未提供嬰兒床資訊";
        else
            bedsInfo = "尚有床位";
        return bedsInfo;
    }


}
