package com.example.flashlightapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

    ImageView bulbOnOff, btnOnOff;
    boolean state;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        bulbOnOff = findViewById(R.id.light_off);
        btnOnOff = findViewById(R.id.btn_off);
        constraintLayout = findViewById(R.id.constraint_layout);

        Dexter.withContext(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                onOffFlashlight();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                Toast.makeText(MainActivity.this, "Camera Permission required", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    private void onOffFlashlight() {
        btnOnOff.setOnClickListener(view -> {
            if(!state){
                CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

                try{
                    String cameraID = cameraManager.getCameraIdList()[0];
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        cameraManager.setTorchMode(cameraID, true);
                    }

                    state = true;
                    btnOnOff.setImageResource(R.drawable.btnon);
                    bulbOnOff.setImageResource(R.drawable.lighton);
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
                } catch(CameraAccessException e){
                    e.printStackTrace();
                }
            } else {
                CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

                try{
                    String cameraID = cameraManager.getCameraIdList()[0];
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        cameraManager.setTorchMode(cameraID, false);
                    }

                    state = false;
                    btnOnOff.setImageResource(R.drawable.btnoff);
                    bulbOnOff.setImageResource(R.drawable.lightoff);
                    constraintLayout.setBackgroundColor(getResources().getColor(R.color.gray));
                } catch(CameraAccessException e){
                    e.printStackTrace();
                }
            }
        });
    }
}