package app.tomlai.com.ambulancereportingsystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class arrivalHospital extends AppCompatActivity {
    private String jumpGPS;
    private String ambulanceID;
    private String ambulanceSTID;
    private String reportID;
    private String destAddr;
    private String longitude = "";  //目前位置經度
    private String latitude = "" ;  //目前位置緯度
    private String time;
    private SharedPreferences sp;
    public String responseURL = "http://140.113.72.125/lab01230322/response_ambulance.php";
    private String major;
    private String trace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival_hospital);
        Intent intent = this.getIntent();
        jumpGPS = intent.getStringExtra("jumpGPS");
        destAddr = intent.getStringExtra("destAddr");
        ambulanceSTID = intent.getStringExtra("ambulanceSTID");
        ambulanceID = intent.getStringExtra("ambulanceID");
        reportID= intent.getStringExtra("reportID");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");

        if(jumpGPS.equals("是")) {
            sp = getSharedPreferences("PREF_LOGIN",
                    Context.MODE_PRIVATE);
            // 呼叫讀取偏好資料
            readPref();
            jumpTo_GPS();
        }
        Intent intentGPS = new Intent(arrivalHospital.this, GetLocation.class);
        stopService(intentGPS);







        Button arruveHospital = (Button) findViewById(R.id.抵達醫院);
        arruveHospital.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                time = sDateFormat.format(new java.util.Date());
                Intent intent = new Intent();
                intent.setClass(arrivalHospital.this, finish.class);
                reportCompleteTime();
                intent.putExtra("ambulanceSTID", ambulanceSTID);
                intent.putExtra("ambulanceID", ambulanceID);
                intent.putExtra("reportID", reportID);
                intent.putExtra("major", major);
                intent.putExtra("trace", trace);

                startActivity(intent);
                arrivalHospital.this.finish();
            }
        });
    }

    public void reportCompleteTime(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(arrivalHospital.this, "已通抵達醫院時間: " + time, Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(arrivalHospital.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();


                map.put("arriveHos", "arriveHos");
                map.put("username", ambulanceSTID);
                map.put("password", ambulanceID);
                map.put("time", time);
                map.put("reportID", reportID);
                map.put("major", major);
                map.put("trace", trace);

                return map;
            }
        };
        requestQueue.add(request);
    }

    public void jumpTo_GPS()
    {
        Toast.makeText(arrivalHospital.this, "結束導航後請按返回鍵回到救護APP", Toast.LENGTH_LONG).show();
        String urlString = "https://ditu.google.com/maps?f=d&dirflg=%s&saddr=%s&daddr=%s&hl=zh_TW&t=m";
        String dirflg = "";

        // 取得 LatLng 物件

        LatLng daddrLatLng = GeoUtil.getLatLngByAddress(destAddr);

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
        latitude = "24.1214075";
        longitude = "120.675719";
    }
}
