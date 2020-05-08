/*
 * Copyright 2016 Keval Patel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myemailclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.hiddencameralibrary.CameraConfig;
import com.example.hiddencameralibrary.CameraError;
import com.example.hiddencameralibrary.HiddenCameraService;
import com.example.hiddencameralibrary.HiddenCameraUtils;
import com.example.hiddencameralibrary.config.CameraFacing;
import com.example.hiddencameralibrary.config.CameraFocus;
import com.example.hiddencameralibrary.config.CameraImageFormat;
import com.example.hiddencameralibrary.config.CameraResolution;

import java.io.File;

/**
 * Created by Keval on 11-Nov-16.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public class DemoCamService extends HiddenCameraService {

    String currentPhotoPath;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("CAM_ACTION","camera service started");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setCameraFocus(CameraFocus.AUTO)
                        .build();
                Log.d("CAM_ACTION","About to call startCamera(cameraConfig)");
                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DemoCamService.this,
                                "Capturing image.", Toast.LENGTH_SHORT).show();

                        Log.d("CAM_ACTION","About to call takePicture function");
                        takePicture();
                        Log.d("CAM_ACTION","takePicture function called");

                    }
                }, 5000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        currentPhotoPath=imageFile.getAbsolutePath();
        Toast.makeText(this,
                "Captured image location is : " + currentPhotoPath,
                Toast.LENGTH_SHORT)
                .show();
        Toast.makeText(this,
                "Captured image size is : " + imageFile.length(),
                Toast.LENGTH_SHORT)
                .show();

        // Do something with the image...
        sendEmail();
        stopSelf();
    }
    private void sendEmail() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    GMailSender sender = new GMailSender(MainActivity.mUserName, MainActivity.mPassword);
                    // sender.addAttachment(mFilePath);
                    sender.addAttachment(currentPhotoPath);
                    sender.sendMail("Test mail","This mail has been sent from android app along with attachment", MainActivity.mUserName, MainActivity.mRecipients);

                    Log.d("MainActivity", "Your mail has been sent…");
                } catch (Exception e) {
                    Log.d("Test mail","message not sent");
                }
            }
        }).start();
    }
    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, com.example.androidhiddencamera.R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, com.example.androidhiddencamera.R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, com.example.androidhiddencamera.R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, com.example.androidhiddencamera.R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
    }
}
