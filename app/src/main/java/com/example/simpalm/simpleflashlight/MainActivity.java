package com.example.simpalm.simpleflashlight;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.security.Policy;

public class MainActivity extends Activity {

    private Camera camera;
    private boolean isFlashon;
    private boolean hasFlash;
    Camera.Parameters param;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {

            // device doesn;t support flash
            // show alert message and close the application

            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("You device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application

                    finish();
                }
            });

            alert.show();
        }

        getCamera();

        ToggleButton flashSwitch = (ToggleButton) findViewById(R.id.flash_switch);
        flashSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {

                    turnOnFlash();
                } else {

                    turnOffFlash();
                }
            }
        });

    }

    private void getCamera() {

        if (camera == null) {

            try {

                camera = Camera.open();
                param = camera.getParameters();

            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }

// Turning On Flash

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    private void turnOnFlash() {

        if (!isFlashon) {
            if (camera == null || param == null) {

                return;
            }

            param = camera.getParameters();

            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(param);
            camera.startPreview();
            isFlashon = true;

            Log.v("AndroidATC", "Flash has been turned on ...");

        }
    }

    // Turning off Flash

    private void turnOffFlash() {

        if (isFlashon) {

            if (camera == null || param == null) {

                return;
            }

            param = camera.getParameters();
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(param);
            camera.stopPreview();
            isFlashon = false;
            Log.v("AndroidATC", "Flash has been turned off ...");

        }
    }
}
