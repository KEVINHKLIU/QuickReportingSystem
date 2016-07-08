package com.tomlai.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class post extends AppCompatActivity {
    private String injuries_number;
    private String inform_unit;
    private String unit_case;
    private String unit_case_case;
    private int count;
    private Bitmap bitmap;
    private String[] fileAddress = new String[10];
    private String[] image_name = new String[10];
    private String[] encoded_string = new String[10];
    private String countStr;
    private String longitude;  //經度
    private String latitude ;  //緯度
    private String strAddress;	// 用來建立所要顯示的訊息字串 (地址字串)
    private String IMEI;
    private String makeRequestURL = "http://140.113.72.125/lab01230322/get_case.php";
    Geocoder geocoder;
    private SharedPreferences sp;
    String picture;
    String file;
    String strTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        showMessage();
        geocoder = new Geocoder(this, Locale.getDefault());	// 建立 Geocoder 物件
        TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telManager.getDeviceId();

/***********************呼叫在service定位好且存在SharePreferance的位置*********************************/
        // 建立與設定偏好資訊
        sp = getSharedPreferences("PREF_LOGIN",
                Context.MODE_PRIVATE);
        // 呼叫讀取偏好資料
        readPref();
        onQuery();
/******************************************************************/

        Date d = new Date();
        CharSequence time = DateFormat.format("yyyy-MM-dd kk:mm:ss", d.getTime());    // kk:24小時制, hh:12小時制
        strTime = String.valueOf(time);

        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");
        unit_case_case = intent.getStringExtra("unit_case_case");
        count = getIntent().getExtras().getInt("count");


        for(int i = 0; i <  10; i++){
            picture = "picture" + String.valueOf(i+1);
            file = "file"  + String.valueOf(i+1);
            image_name[i]  = intent.getStringExtra(picture);
            fileAddress[i] = intent.getStringExtra(file);
        }

      for(int i = 0; i < 10; i++)          //將圖檔轉成字串
        {
            if(!(fileAddress[i].equals("None")))
            {
                bitmap = BitmapFactory.decodeFile(fileAddress[i]);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] array = stream.toByteArray();
                encoded_string[i] = Base64.encodeToString(array, 0);
            }
            else
                encoded_string[i] = "None";
        }



        String str = "";
        for(int i = 0; i < 10; i++)
           str = str + "picture" + String.valueOf(i+1) + "  " + image_name[i] + "\n" ;

        TextView tv1 = (TextView)findViewById(R.id.textView);
        tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv1.setText("------報案確認單------" + "\n手機ID: " + IMEI + "\n報案時間: " + strTime + "\n受傷人數: " + injuries_number + "\n通報單位: " + inform_unit + "\n案件類別: " + unit_case + "\n通報案件: " + unit_case_case + "\n相片數: " + count +
                "\n地址: " + "台中市南區大學路" + "\n經度: " + longitude + "\n緯度: " + latitude + "\n" + str);

        countStr = String.valueOf(count);
        Button transmit = (Button) findViewById(R.id.確認無誤並通報);
        transmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeRequest();

                ConfirmExit();
            }
        });
        Button call = (Button) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                makeRequest();
                call();

            }
        });

    }



    public void onQuery() {	// "用經緯度查地址
        String strLat = latitude;	// 取輸入的緯度字串
        String strLon =longitude ;	// 取輸入的經度字串
        if(strLat.length() == 0 || strLon.length() == 0) // 當字串為空白時
            return;										 // 結束處理

        double latitude = Double.parseDouble(strLat);  // 取得緯度值
        double longitude = Double.parseDouble(strLon); // 取得經度值

        strAddress = "";	// 用來建立所要顯示的訊息字串 (地址字串)
        try {
            List<Address> listAddr = geocoder.
                    getFromLocation(latitude, longitude,// 用經緯度查地址
                            1);	// 只需傳回1筆地址資料

            if (listAddr == null || listAddr.size() == 0) 	//檢查是否有取得地址
                strAddress += "無法取得地址資料!";
            else {
                Address addr = listAddr.get(0);	// 取 List 中的第一筆(也是唯一的一筆)
                for (int i = 0; i <= addr.getMaxAddressLineIndex(); i++)
                    strAddress += addr.getAddressLine(i);
            }
        } catch (Exception ex) {
            strAddress += "取得地址發生錯誤:" + ex.toString();
        }
    }

    private void makeRequest() {
        Intent intent = new Intent(post.this, GetLocation.class);
        stopService(intent);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, makeRequestURL,    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("IMEI", IMEI);                      //手機序號
                map.put("strTime", strTime);                        //時間
                map.put("strTime", strTime);                        //時間
                map.put("injuries_number", injuries_number);    //受傷人數
                map.put("inform_unit", inform_unit);            //通知單位
                map.put("unit_case", unit_case);                //案件類別
                map.put("unit_case_case", unit_case_case);      //案件項目
                map.put("countStr", countStr);                //拍照數量
                map.put("strAddress","台中市南區大學路" );                 //地址
                map.put("longitude", "120.6769386");                //經度
                map.put("latitude", "24.1214075");                  //緯度

                String image_name1,image_name2,image_name3,image_name4,image_name5,image_name6,image_name7,image_name8,image_name9,image_name10;
                image_name1 = image_name[0];
                image_name2 = image_name[1];
                image_name3 = image_name[2];
                image_name4 = image_name[3];
                image_name5 = image_name[4];
                image_name6 = image_name[5];
                image_name7 = image_name[6];
                image_name8 = image_name[7];
                image_name9 = image_name[8];
                image_name10 = image_name[9];

                String encoded_picture1,encoded_picture2,encoded_picture3,encoded_picture4,encoded_picture5,encoded_picture6,encoded_picture7,encoded_picture8,encoded_picture9,encoded_picture10;
                encoded_picture1 = encoded_string[0];
                encoded_picture2 = encoded_string[1];
                encoded_picture3 = encoded_string[2];
                encoded_picture4 = encoded_string[3];
                encoded_picture5 = encoded_string[4];
                encoded_picture6 = encoded_string[5];
                encoded_picture7 = encoded_string[6];
                encoded_picture8 = encoded_string[7];
                encoded_picture9 = encoded_string[8];
                encoded_picture10 = encoded_string[9];

                map.put("image_name1", image_name1);
                map.put("encoded_picture1", encoded_picture1);           //圖檔字串
                map.put("image_name2", image_name2);                      //照片名稱
                map.put("encoded_picture2", encoded_picture2);           //圖檔字串
                map.put("image_name3", image_name3);                      //照片名稱
                map.put("encoded_picture3", encoded_picture3);           //圖檔字串
                map.put("image_name4", image_name4);                      //照片名稱
                map.put("encoded_picture4", encoded_picture4);           //圖檔字串
                map.put("image_name5", image_name5);                      //照片名稱
                map.put("encoded_picture5", encoded_picture5);           //圖檔字串
                map.put("image_name6", image_name6);                      //照片名稱
                map.put("encoded_picture6", encoded_picture6);           //圖檔字串
                map.put("image_name7", image_name7);                      //照片名稱
                map.put("encoded_picture7", encoded_picture7);           //圖檔字串
                map.put("image_name8", image_name8);                      //照片名稱
                map.put("encoded_picture8", encoded_picture8);           //圖檔字串
                map.put("image_name9", image_name9);                      //照片名稱
                map.put("encoded_picture9", encoded_picture9);           //圖檔字串
                map.put("image_name10", image_name10);                      //照片名稱
                map.put("encoded_picture10", encoded_picture10);           //圖檔字串

                return map;
            }
        };
        requestQueue.add(request);
    }




    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(post.this); //創建訊息方塊
        ad.setTitle("通報完成");
        ad.setMessage("系統已將您的案件送出" + "\n通報單位將立即趕到");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                post.this.finish();//關閉activity
            }
        });
        ad.show();//顯示訊息視窗
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Intent intent = new Intent();
            intent.setClass(post.this, MainActivity.class);
            startActivity(intent);
            post.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showMessage(){
        // 無提供者, 顯示提示訊息
        AlertDialog.Builder ad= new AlertDialog.Builder(post.this); //創建訊息方塊
        ad.setTitle("取得手機ID以及所在位置通知");
        ad.setMessage("通報案件會取得您的手機ID以及所在位置作為案情通報的所需資訊");
        ad.setPositiveButton("確定", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i)
            {
                Toast.makeText(post.this,"按下返回鍵可重新通報", Toast.LENGTH_SHORT).show();
            }
        });
        ad.show();//顯示訊息視窗
    }

    public void call()
    {
        AlertDialog.Builder ad=new AlertDialog.Builder(post.this); //創建訊息方塊
        ad.setTitle("選擇要撥打的單位");
        ad.setMessage("按下按鈕即幫您立即撥打且傳送通報資料");
        ad.setPositiveButton("警察局", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Intent it = new Intent();
                it.setAction(Intent.ACTION_CALL);
                it.setData(Uri.parse("tel:0916743262"));
                startActivity(it);
                post.this.finish();

            }
        });
        ad.setNegativeButton("消防救護", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                Intent it = new Intent();
                it.setAction(Intent.ACTION_CALL);
                it.setData(Uri.parse("tel:0916743262"));
                startActivity(it);
                post.this.finish();
            }
        });
        ad.show();//顯示訊息視窗

    }

    // 讀取偏好資料
    private void readPref() {
        longitude= sp.getString("longitude", "");
        latitude = sp.getString("latitude", "");
    }


}
