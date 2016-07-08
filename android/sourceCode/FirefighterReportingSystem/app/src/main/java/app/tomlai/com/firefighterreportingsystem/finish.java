package app.tomlai.com.firefighterreportingsystem;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class finish extends AppCompatActivity {
    private String major;
    private String trace;
    private String fireID;
    private String fireSTID;
    private String time;
    private EditText Note;
    private String noteInfo;
    private String reportID;
    private String responseURL = "http://140.113.72.125/lab01230322/response_firestation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Button complete = (Button) findViewById(R.id.回報);
        Intent intents = new Intent(finish.this, GetLocation.class);
        stopService(intents);
        Intent intent = this.getIntent();
        fireID = intent.getStringExtra("fireID");
        fireSTID = intent.getStringExtra("fireSTID");
        reportID = intent.getStringExtra("reportID");
        major = intent.getStringExtra("major");
        trace = intent.getStringExtra("trace");

        complete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Note = (EditText)findViewById(R.id.備註);
                noteInfo = Note.getText().toString();
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                time = sDateFormat.format(new java.util.Date());
                reportCompleteTime();
                ConfirmExit();
            }
        });
    }

    public void reportCompleteTime(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, responseURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(finish.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("complete", "Complete");
                map.put("username", fireSTID);
                map.put("password", fireID);
                map.put("time", time);
                map.put("reportID", reportID);
                map.put("major", major);
                map.put("trace", trace);
                map.put("note", noteInfo);
                return map;
            }
        };
        requestQueue.add(request);
    }
    public void ConfirmExit(){
        AlertDialog.Builder ad=new AlertDialog.Builder(finish.this); //創建訊息方塊
        ad.setTitle("通報完成");
        ad.setMessage("通報完成");
        ad.setPositiveButton("是", new DialogInterface.OnClickListener() { //按"是",則退出應用程式
            public void onClick(DialogInterface dialog, int i) {
                finish.this.finish();//關閉activity
            }
        });
        ad.show();//顯示訊息視窗
    }
}
