package com.kiosk.accessbank.fingerprint;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kiosk.accessbank.R;
import com.zkteco.biometric.FingerprintCaptureListener;
import com.zkteco.biometric.FingerprintSensor;

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;

    private FingerprintSensor fingerprintSensor;
    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
//        fingerprintSensor = new FingerprintSensor();
    }


    public void listener(FingerprintCaptureListener listener){
        if (fingerprintSensor ==null) return;

        fingerprintSensor.setFingerprintCaptureListener(listener);
    }



    public void openDevice(){
        if (fingerprintSensor ==null) return;
        fingerprintSensor.openDevice(0);
    }

    public void closeDevice(){
        if (fingerprintSensor ==null) return;
        fingerprintSensor.closeDevice();

    }
    public boolean isDeviceOnline(){
        if (fingerprintSensor ==null) return false ;
        return fingerprintSensor.getDeviceCount()  > 0;
    }

    public void startScanning(){
        if (fingerprintSensor ==null) return;
        fingerprintSensor.startCapture();
    }

    public void stopScanning(){
        if (fingerprintSensor ==null) return;
        fingerprintSensor.stopCapture();
    }

    public void acquireFingerprint(){
        if (fingerprintSensor ==null) return;
        fingerprintSensor.destroy();
    }



}