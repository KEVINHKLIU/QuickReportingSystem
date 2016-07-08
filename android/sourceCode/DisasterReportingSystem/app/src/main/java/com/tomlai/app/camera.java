package com.tomlai.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class camera extends AppCompatActivity {
    private String inform_unit;
    private String injuries_number;
    private String unit_case;
    private String unit_case_case;
    private Camera mCamera;
    private CameraPreview mPreview;
    private String[] image_name = new String[10];
    private String[] encoded_string = new String[10];
    private File file;
    private String[] fileAddress = new String[10];
    private String IMEI;
    int count = 0;
    Uri file_uri;
    private DisplayMetrics mPhone;
    int pictureNum = 1;
    private boolean openFlash = false;
    private Parameters p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//設定螢幕不隨手機旋轉
       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//設定螢幕直向顯示
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        safeCameraOpened(0);
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        IMEI = telManager.getDeviceId();

        //讀取手機解析度
        mPhone = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mPhone);

        // mImg = (ImageView) findViewById(R.id.img);
        final Button Camera = (Button) findViewById(R.id.camera);
        final Button finish = (Button) findViewById(R.id.完成);
        final Button flashlight = (Button) findViewById(R.id.flashlight);

        for(int i = 0; i < 10; i++) {
            encoded_string[i] = "None";
            image_name[i] = "None";
            fileAddress[i] = "None";
        }


        Intent intent = this.getIntent();
        injuries_number = intent.getStringExtra("injuries_number");
        inform_unit = intent.getStringExtra("inform_unit");
        unit_case = intent.getStringExtra("unit_case");
        unit_case_case = intent.getStringExtra("unit_case_case");

        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for(int i = 0; i <10; i++) {
                    if (!(image_name[i].equals("None"))) {
                        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image_name[i]); //取得系統的功用圖檔路徑
                        file_uri = Uri.fromFile(file);
                        fileAddress[i] = file_uri.getPath();
                            }
                }
                pictureNum--;
                pass();
            }
        });

        flashlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(openFlash == true) {
                    p = mCamera.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(p);
                    mCamera.startPreview();
                    flashlight.setText("開啟閃光");
                    openFlash = false;
                }
                else {
                    p = mCamera.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(p);
                    mCamera.startPreview();
                    flashlight.setText("關閉閃光");
                    openFlash = true;
                }
                    }
        });



        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*開啟相機功能，並將拍照後的圖片存入SD卡相片集內，須由startActivityForResult且
                帶入requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult*/
                Camera.setText("已拍了" + pictureNum + "張");
                mCamera.cancelAutoFocus();
                mCamera.autoFocus(null);
                mCamera.takePicture(null, null, mPicture);
                getFileUri();

                if(pictureNum == 10)
                {
                    Camera.setText("已拍了" + pictureNum + "張");
                    Camera.setVisibility(View.INVISIBLE); // 隱藏
                    finish.setText("按此扭傳送");

                }
                pictureNum++;
            }
        });
    }

    private boolean safeCameraOpened(int id) {
        boolean qOpened = false;
        try {
            releaseCameraAndPreview();
            mCamera = Camera.open(id);
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.e(getString(R.string.app_name), "failed to open Camera");
            e.printStackTrace();
        }
        return qOpened;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void getFileUri() {
        String dir = new SimpleDateFormat("yyyyMMdd").format(new Date());
        image_name[count] = "JPEG_" +  dir + "_" +  IMEI + "_" + System.currentTimeMillis() + ".jpg";
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + image_name[count]); //取得系統的功用圖檔路徑
        file_uri = Uri.fromFile(file);


        count++;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = file;
            if (pictureFile == null) {
                Log.d("Camera: ", "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("Camera: ", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Camera: ", "Error accessing file: " + e.getMessage());
            }
            mCamera.startPreview();
        }
    };

    public void pass()
    {
        Intent intentPass = new Intent();
        intentPass.setClass(camera.this, post.class);
        intentPass.putExtra("injuries_number", injuries_number);
        intentPass.putExtra("inform_unit", inform_unit);
        intentPass.putExtra("unit_case", unit_case);
        intentPass.putExtra("unit_case_case", unit_case_case);
        intentPass.putExtra("count", pictureNum);
        String picture ;
        String file;
        for(int i = 0; i <  10; i++){
            picture = "picture" + String.valueOf(i+1);
            file = "file"  + String.valueOf(i+1);
            intentPass.putExtra(picture, image_name[i]);
            intentPass.putExtra(file,fileAddress[i]);
        }
        startActivity(intentPass);
        camera.this.finish();
    }
}
