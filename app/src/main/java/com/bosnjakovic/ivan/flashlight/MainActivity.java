package com.bosnjakovic.ivan.flashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.security.Policy;

public class MainActivity extends AppCompatActivity {

    private ToggleButton tbFlash;
    private android.hardware.Camera mCamera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkCameraSupport();
        initWidgets();
        setUpListeners();
    }

    private void checkCameraSupport() {
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("No flash available");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }
        getCamera();
    }

    private void getCamera() {
        if (mCamera == null) {
            mCamera = android.hardware.Camera.open();
            param = mCamera.getParameters();
        }
    }

    private void initWidgets() {
        tbFlash = (ToggleButton) findViewById(R.id.tbFlash);

    }

    private void setUpListeners() {
        tbFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnCamera();
                } else {
                    turnOfCamera();
                }
            }
        });
    }


    private void turnOnCamera() {
        if (mCamera == null || param == null) {
            return;
        }
        param = mCamera.getParameters();
        param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
        mCamera.setParameters(param);
        mCamera.startPreview();
        isFlashOn = true;

    }

    private void turnOfCamera() {
        if (mCamera == null || param == null) {
            return;
        }
        param = mCamera.getParameters();
        param.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(param);
        mCamera.stopPreview();
        isFlashOn = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOfCamera();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }
}