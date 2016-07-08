package app.tomlai.com.ambulancereportingsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    private String username;
    private String password;
    private EditText user;
    private EditText pass ;
    private CheckBox cb; //顯示密碼
    private CheckBox cb2;//記住帳密
    private int defaultIType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkGPS();
        Intent intent = new Intent(MainActivity.this, GetLocation.class);  //啟動Service定位
        startService(intent);

        user = (EditText) findViewById(R.id.EditText01);
        pass = (EditText)findViewById(R.id.EditText02);
        defaultIType = pass.getInputType();
        pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | defaultIType);


        SharedPreferences rememberName = getPreferences(Activity.MODE_PRIVATE);
        String name_str = rememberName.getString("name", "");
        String pass_str = rememberName.getString("pass", "");
        user.setText(name_str);
        pass.setText(pass_str);


        Button login = (Button) findViewById(R.id.ButtonLogin);
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                SharedPreferences rememberName = getPreferences(Activity.MODE_PRIVATE);
                SharedPreferences.Editor edit=rememberName.edit();
                edit.putString("name", username);
                edit.putString("pass", password);
                edit.commit();

               run();
            }
        });

        cb = (CheckBox) findViewById(R.id.checkBox); //顯示密碼
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked())
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | defaultIType);
                else
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | defaultIType);
                pass.setSelection(pass.getText().length());
            }
        });

        cb2  = (CheckBox) findViewById(R.id.checkBox2); //記住帳密
        cb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2.isChecked())
                {
                    SharedPreferences rememberName = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = rememberName.edit();
                    edit.putString("name", user.getText().toString());
                    edit.putString("pass", pass.getText().toString());
                    password = pass.getText().toString();
                    edit.commit();
                }
                if(!cb2.isChecked())
                {

                    SharedPreferences rememberName = getPreferences(Activity.MODE_PRIVATE);
                    SharedPreferences.Editor edit = rememberName.edit();
                    edit.putString("name", "");
                    edit.putString("pass", "");
                    edit.commit();
                }
            }
        });


    }


    public void run(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, "http://140.113.72.125/lab01230322/response_login.php",    // http://140.120.13.252:8081/fuck/catchapp.php
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.toString().equals("success"))
                        {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, success.class);
                            intent.putExtra("ambulanceSTID", username);
                            intent.putExtra("ambulanceID", password);
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                        else {
                            Toast toast=Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, -5);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("auth", "2");
                map.put("username", username);
                map.put("password", password);
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
        AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊
        ad.setTitle("離開");
        ad.setMessage("確定要離開?");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                MainActivity.this.finish();//關閉activity
            }
        });
        ad.setNegativeButton("否", new DialogInterface.OnClickListener() { //按"否",則不執行任何操作
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad.show();//顯示訊息視窗
    }

    private void checkGPS()
    {
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if(!(status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)))
        {
            // 無提供者, 顯示提示訊息
            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this); //創建訊息方塊
            ad.setTitle("偵測到未開啟GPS");
            ad.setMessage("請先開啟GPS?");
            ad.setPositiveButton("前往開啟", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
                public void onClick(DialogInterface dialog, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            ad.show();//顯示訊息視窗
        }
    }

}

