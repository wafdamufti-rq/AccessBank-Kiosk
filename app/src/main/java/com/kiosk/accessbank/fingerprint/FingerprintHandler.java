package com.kiosk.accessbank.fingerprint;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kiosk.accessbank.fingerprint.ZKUSBManager.ZKUSBManager;
import com.kiosk.accessbank.fingerprint.ZKUSBManager.ZKUSBManagerListener;
import com.kiosk.accessbank.util.PermissionUtils;
import com.zkteco.android.biometric.FingerprintExceptionListener;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintCaptureListener;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;
import com.zkteco.android.biometric.module.fingerprintreader.FingprintFactory;
import com.zkteco.android.biometric.module.fingerprintreader.ZKFingerService;
import com.zkteco.android.biometric.module.fingerprintreader.exception.FingerprintException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private final Context context;
    private static final int ZKTECO_VID =   0x1b55;
    private static final int LIVE20R_PID =   0x0120;
    private static final int LIVE10R_PID =   0x0124;
    private static final String TAG = "MainActivity";
    private final int REQUEST_PERMISSION_CODE = 9;
    private ZKUSBManager zkusbManager = null;
    private FingerprintSensor fingerprintSensor = null;

    private final int usb_vid = ZKTECO_VID;
    private int usb_pid = 0;
    private boolean bStarted = false;
    private final int deviceIndex = 0;
    private boolean isReseted = false;
    private String strUid = null;
    private final static int ENROLL_COUNT   =   3;
    private int enroll_index = 0;
    private final byte[][] regtemparray = new byte[3][2048];  //register template buffer array
    private boolean bRegister = false;
    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
        zkusbManager = new ZKUSBManager(mContext, zkusbManagerListener);
    }

    private FingerprintListener listener = new FingerprintListener() {
        @Override
        public void onSuccess(byte[] value, String result) {

        }

        @Override
        public void onFailed(String message) {

        }
    };
    public FingerprintHandler addListener(FingerprintListener listener){
        this.listener = listener;
        return this;
    }


    void doRegister(byte[] template)
    {
        byte[] bufids = new byte[256];
        int ret = ZKFingerService.identify(template, bufids, 70, 1);
        if (ret > 0)
        {
            String strRes[] = new String(bufids).split("\t");
            listener.onFailed("the finger already enroll by " + strRes[0] + ",cancel enroll");
            setResult("the finger already enroll by " + strRes[0] + ",cancel enroll");
            bRegister = false;
            enroll_index = 0;
            return;
        }
        if (enroll_index > 0 && (ret = ZKFingerService.verify(regtemparray[enroll_index-1], template)) <= 0)
        {
            listener.onFailed("please press the same finger 3 times for the enrollment, cancel enroll, socre=\" + ret");
            setResult("please press the same finger 3 times for the enrollment, cancel enroll, socre=" + ret);
            bRegister = false;
            enroll_index = 0;
            return;
        }
        System.arraycopy(template, 0, regtemparray[enroll_index], 0, 2048);
        enroll_index++;
        if (enroll_index == ENROLL_COUNT) {
            bRegister = false;
            enroll_index = 0;
            byte[] regTemp = new byte[2048];
            if (0 < (ret = ZKFingerService.merge(regtemparray[0], regtemparray[1], regtemparray[2], regTemp))) {
                int retVal = 0;
                retVal = ZKFingerService.save(regTemp, strUid);
                if (0 == retVal)
                {
                    String strFeature = Base64.encodeToString(regTemp, 0, ret, Base64.NO_WRAP);
                    setResult("enroll succ");
                }
                else
                {
                    setResult("enroll fail, add template fail, ret=" + retVal);
                }
            } else {
                setResult("enroll fail");
            }
            bRegister = false;
        } else {
            setResult("You need to press the " + (3 - enroll_index) + " times fingerprint");
        }
    }

    void doIdentify(byte[] template)
    {
        byte[] bufids = new byte[256];
        int ret = ZKFingerService.identify(template, bufids, 70, 1);
        if (ret > 0) {
            String strRes[] = new String(bufids).split("\t");
            listener.onSuccess(bufids,"identify succ, userid:" + strRes[0].trim() + ", score:" + strRes[1].trim());
            setResult("identify succ, userid:" + strRes[0].trim() + ", score:" + strRes[1].trim());
        } else {
            setResult("identify fail, ret=" + ret);
        }
    }


    private FingerprintCaptureListener fingerprintCaptureListener = new FingerprintCaptureListener() {
        @Override
        public void captureOK(byte[] fpImage) {
            final Bitmap bitmap = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, fingerprintSensor.getImageWidth(), fingerprintSensor.getImageHeight());
        }

        @Override
        public void captureError(FingerprintException e) {
            listener.onFailed(e.getMessage());
            // nothing to do
        }

        @Override
        public void extractOK(byte[] fpTemplate) {
            listener.onSuccess(fpTemplate, "fingerprint success scanning");
//            if (bRegister)
//            {
//                doRegister(fpTemplate);
//            }
//            else
//            {
//                doIdentify(fpTemplate);
//            }
        }

        @Override
        public void extractError(int i) {
            // nothing to do
        }
    };

    private FingerprintExceptionListener fingerprintExceptionListener = () -> {
        LogHelper.e("usb exception!!!");
        if (!isReseted) {
            try {
                fingerprintSensor.openAndReboot(deviceIndex);
            } catch (FingerprintException e) {
                e.printStackTrace();
            }
            isReseted = true;
        }
    };

    private ZKUSBManagerListener zkusbManagerListener = new ZKUSBManagerListener() {
        @Override
        public void onCheckPermission(int result) {
            afterGetUsbPermission();
        }

        @Override
        public void onUSBArrived(UsbDevice device) {
            if (bStarted)
            {
                closeDevice();
                tryGetUSBPermission();
            }
        }

        @Override
        public void onUSBRemoved(UsbDevice device) {
            LogHelper.d("usb removed!");
        }
    };


    private void checkStoragePermission(Activity activity) {
        String[] permission = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ArrayList<String> deniedPermissions = PermissionUtils.checkPermissions(activity, permission);
        if (deniedPermissions.isEmpty()) {
            //permission all granted
            Log.i(TAG, "[checkStoragePermission]: all granted");
        } else {
            int size = deniedPermissions.size();
            String[] deniedPermissionArray = deniedPermissions.toArray(new String[size]);
            PermissionUtils.requestPermission(activity, deniedPermissionArray, REQUEST_PERMISSION_CODE);
        }
    }


    private void createFingerprintSensor()
    {
        if (null != fingerprintSensor)
        {
            FingprintFactory.destroy(fingerprintSensor);
            fingerprintSensor = null;
        }
        // Define output log level
        LogHelper.setLevel(Log.VERBOSE);
        LogHelper.setNDKLogLevel(Log.ASSERT);
        // Start fingerprint sensor
        Map deviceParams = new HashMap();
        //set vid
        deviceParams.put(ParameterHelper.PARAM_KEY_VID, usb_vid);
        //set pid
        deviceParams.put(ParameterHelper.PARAM_KEY_PID, usb_pid);
        fingerprintSensor = FingprintFactory.createFingerprintSensor(context, TransportType.USB, deviceParams);
    }

    private boolean enumSensor()
    {
        UsbManager usbManager = (UsbManager)context.getSystemService(Context.USB_SERVICE);
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            int device_vid = device.getVendorId();
            int device_pid = device.getProductId();
            if (device_vid == ZKTECO_VID && (device_pid == LIVE20R_PID || device_pid == LIVE10R_PID))
            {
                usb_pid = device_pid;
                return true;
            }
        }
        return false;
    }


    private void tryGetUSBPermission() {
        zkusbManager.initUSBPermission(usb_vid, usb_pid);
    }

    private void afterGetUsbPermission()
    {
        openDevice();
    }

    private void openDevice()
    {
        createFingerprintSensor();
        bRegister = false;
        enroll_index = 0;
        isReseted = false;
        try {
            //fingerprintSensor.setCaptureMode(1);
            fingerprintSensor.open(deviceIndex);
            //load all templates form db
            {
                // device parameter
                LogHelper.d("sdk version" + fingerprintSensor.getSDK_Version());
                LogHelper.d("firmware version" + fingerprintSensor.getFirmwareVersion());
                LogHelper.d("serial:" + fingerprintSensor.getStrSerialNumber());
                LogHelper.d("width=" + fingerprintSensor.getImageWidth() + ", height=" + fingerprintSensor.getImageHeight());
            }
            fingerprintSensor.setFingerprintCaptureListener(deviceIndex, fingerprintCaptureListener);
            fingerprintSensor.SetFingerprintExceptionListener(fingerprintExceptionListener);
            fingerprintSensor.startCapture(deviceIndex);
            bStarted = true;
//            textView.setText("connect success!");
        } catch (FingerprintException e) {
            e.printStackTrace();
            // try to  reboot the sensor
            try {
                fingerprintSensor.openAndReboot(deviceIndex);
            } catch (FingerprintException ex) {
                ex.printStackTrace();
            }
//            textView.setText("connect failed!");
        }
    }

    private void closeDevice()
    {
        if (bStarted)
        {
            try {
                fingerprintSensor.stopCapture(deviceIndex);
                fingerprintSensor.close(deviceIndex);
            } catch (FingerprintException e) {
                e.printStackTrace();
            }
            bStarted = false;
        }
    }

    public void onBnStart()
    {
        if (bStarted)
        {
//            textView.setText("Device already connected!");
            return;
        }
        if (!enumSensor())
        {
//            textView.setText("Device not found!");
            return;
        }
        tryGetUSBPermission();
    }

    public void onBnStop()
    {
        if (!bStarted)
        {
//            textView.setText("Device not connected!");
            return;
        }
        closeDevice();
//        textView.setText("Device closed!");
    }

    public void onBnRegister(String id)
    {
        if (bStarted) {
            strUid = id;
//            if (null == strUid || strUid.isEmpty()) {
//                textView.setText("Please input your user id");
//                bRegister = false;
//                return;
//            }

            bRegister = true;
            enroll_index = 0;
//            textView.setText("Please press your finger 3 times.");
        } else {
//            textView.setText("Please start capture first");
        }
    }

    public void onBnIdentify()
    {
        if (bStarted) {
            bRegister = false;
            enroll_index = 0;
        } else {
        }
    }

    private void setResult(String result)
    {
        final String mStrText = result;

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textView.setText(mStrText);
//            }
//        });
    }

    public void onBnDelete()
    {
        if (bStarted) {
            ZKFingerService.del(strUid);
        }
    }

    public void onBnClear()
    {
        if (bStarted) {
            ZKFingerService.clear();
        }
    }


    public interface FingerprintListener {
        void onSuccess(byte[] value,String result);

        void onFailed(String message);
    }

}