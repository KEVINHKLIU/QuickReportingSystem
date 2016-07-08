package app.tomlai.com.ambulancereportingsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class success extends AppCompatActivity  {
    private String major = "0";
    private String trace;
    private String longitude;  //目前位置經度
    private String latitude;  //目前位置緯度
    private String address = "none"; //目的地位置(案件位置)
    private String ambulanceID;
    private String ambulanceSTID;
    private String time;
    private String reportID = "0";
    private String chkReportID = "0";
    private String[] report= new String[8];
    private String[] reports= new String[8];
    private int attendanceInt = 0;
    public String responseURL = "http://140.113.72.125/lab01230322/response_ambulance.php";
    private SharedPreferences sp;
    private SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);


        Intent intent = this.getIntent();
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");

        Button receive = (Button) findViewById(R.id.接收案件);
        receive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                receiveReport();// 按下接收案件（傳救護車ID, 收案件 + 案件ID）

            }
        });

        Button GPS = (Button) findViewById(R.id.導航);
        GPS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(reportID.equals("0")) { //沒按下接收案件按鈕或是目前無分配之任務
                    noCaseNotice();
                }
                else if(attendanceInt == 0){ //有案件但還未按下出勤按鈕通報
                    noAttendNotice();
                }
                else {
                    // 建立與設定偏好資訊
                    sp = getSharedPreferences("PREF_LOGIN",
                            Context.MODE_PRIVATE);
                    // 呼叫讀取偏好資料
                    readPref();
                    Toast.makeText(success.this, "結束導航後請按返回鍵回到救護APP", Toast.LENGTH_LONG).show();
                    jumpTo_GPS();

                }
            }
        });

        Button attendance = (Button) findViewById(R.id.出勤);
        attendance.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!reportID.equals("0") && attendanceInt == 0){
                    time = sDateFormat.format(new java.util.Date());
                    reportAttendanceTime();
                    attendanceInt++;
                }
                else if(attendanceInt > 0){ //表示重複案出勤案件
                    repeatNotice();
                }
                else
                    noCaseNotice();
            }
        });

        Button arrive = (Button) findViewById(R.id.抵達現場);
        arrive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentss = new Intent(success.this, GetLocation.class);
                stopService(intentss);

                Intent intents = new Intent(success.this, GetLocation.class);  //啟動Service定位
                startService(intents);
                if(attendanceInt > 0){
                    time = sDateFormat.format(new java.util.Date());
                    reportArriveTime();
                    Intent intent = new Intent();
                    intent.setClass(success.this, child.class);
                    intent.putExtra("ambulanceSTID", ambulanceSTID);
                    intent.putExtra("ambulanceID", ambulanceID);
                    intent.putExtra("reportID", reportID);
                    intent.putExtra("major", major);
                    intent.putExtra("trace", trace);
                    startActivity(intent);
                    success.this.finish();
                } else
                    noAttendNotice();
            }
        });
    }



    public void receiveReport(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        TextView tv1 = (TextView)findViewById(R.id.textView);
                        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());

                        if(response.equals("目前無任務")) {
                            tv1.setText("目前無分配之任務且無重大事件發生");
                            reportID = "0";
                        }
                        else
                        {
                            String str = "";
                            reports = response.split("#");
                            if(reports[0].equals("01"))  //表示只有一般案件
                            {
                                report = reports[1].split("\n");
                                reportID = report[0];
                                address = report[7];
                                for(int i = 0; i < 8; i++)
                                    str = str + report[i] + "\n";
                                tv1.setText(str);
                            }
                            else if(reports[0].equals("10"))  //表示有重大無一般
                            {
                                major = "1";
                                report = reports[1].split("\n");
                                reportID = report[0];
                                address = report[7];
                                for(int i = 0; i < 8; i++)
                                    str = str + report[i] + "\n";
                                tv1.setText("此案件為重大案件\n" + str);

                            }
                            else
                            {
                                noticeBigCase();
                                Button 接收重大案件 = (Button) findViewById(R.id.接收重大案件);
                                接收重大案件.setVisibility(View.VISIBLE);
                                接收重大案件.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        major = "1";
                                        TextView tv1 = (TextView)findViewById(R.id.textView);
                                        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
                                        String str = "";
                                        report = reports[1].split("\n");
                                        reportID = report[0];

                                        address = report[7];
                                        for(int i = 0; i < 8; i++)
                                            str = str + report[i] + "\n";
                                        tv1.setText("此案件為重大案件\n" + str);
                                    }
                                });

                                Button 接收一般案件 = (Button) findViewById(R.id.接收一般案件);
                                接收一般案件.setVisibility(View.VISIBLE);
                                接收一般案件.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        report = reports[1].split("\n");
                                        chkReportID = report[0];

                                        TextView tv1 = (TextView)findViewById(R.id.textView);
                                        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
                                        String str = "";
                                        report = reports[2].split("\n");
                                        reportID = report[0];
                                        if(chkReportID.equals(reportID))
                                            major = "2";
                                        else
                                            major = "0";
                                        address = report[7];
                                        for(int i = 0; i < 8; i++)
                                            str = str + report[i] + "\n";
                                        tv1.setText(str);
                                    }
                                });
                            }

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(success.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("getCase", "getCase");
                map.put("username", ambulanceSTID);
                map.put("password", ambulanceID);
                return map;
            }
        };
        requestQueue.add(request);
    }

    public void reportArriveTime(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(success.this, "已通報抵達現場時間: " + time, Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(success.this,error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();

                map.put("arrive", "Arrive");
                map.put("time", time);
                map.put("password", ambulanceID);
                map.put("reportID", reportID);
                map.put("major", major);
                map.put("trace", trace);
                return map;
            }
        };
        requestQueue.add(request);
    }

    public void reportAttendanceTime(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       Toast.makeText(success.this, "已通報出勤時間: " + time, Toast.LENGTH_SHORT).show();
                        trace = response.toString();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(success.this,error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("attendance", "Attendance");
                map.put("username", ambulanceSTID);
                map.put("password", ambulanceID);
                map.put("time", time);
                map.put("reportID", reportID);
                map.put("major", major);
                return map;
            }
        };
        requestQueue.add(request);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            ConfirmExit(); //呼叫ConfirmExit()函數
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(success.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                success.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    public void repeatNotice(){
        AlertDialog.Builder ad=new AlertDialog.Builder(success.this); //創建訊息方塊
        ad.setTitle("重複按出勤按鈕");
        ad.setMessage("您已按過出勤案件, 請勿重按");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則沒動作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    public void noCaseNotice(){
        AlertDialog.Builder ad=new AlertDialog.Builder(success.this); //創建訊息方塊
        ad.setTitle("目前沒有案件通知");
        ad.setMessage("目前未分配到任何任務");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則沒動作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    public void noAttendNotice(){
        AlertDialog.Builder ad=new AlertDialog.Builder(success.this); //創建訊息方塊
        ad.setTitle("還未按出勤按鍵通知");
        ad.setMessage("您還未按下出勤按鍵, 通報出勤時間");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則沒動作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    public void jumpTo_GPS()
    {
        String urlString = "https://ditu.google.com/maps?f=d&dirflg=%s&saddr=%s&daddr=%s&hl=zh_TW&t=m";
        String dirflg = "";


        LatLng daddrLatLng = GeoUtil.getLatLngByAddress(address);

        // 組合緯經度參數
        String saddr = latitude + "," + longitude;
        String daddr = daddrLatLng.latitude + "," + daddrLatLng.longitude;
        dirflg = "d";


        urlString = urlString.format(urlString, dirflg, saddr, daddr);

        Uri uri = Uri.parse(urlString);
        Intent intentGPS = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intentGPS);
    }

    // 讀取偏好資料
    private void readPref() {
        longitude= sp.getString("longitude", "");
        latitude = sp.getString("latitude", "");
    }

    public void noticeBigCase(){
        AlertDialog.Builder ad=new AlertDialog.Builder(success.this); //創建訊息方塊
        ad.setTitle("目前發生重大案件");
        ad.setMessage("目前有發生重大案件，可以選擇接收重大案件進行支援，或是選擇接收一般案件進行受理案件處理。");
        ad.setPositiveButton("了解", new DialogInterface.OnClickListener() { //按"是",則沒動作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }


}
