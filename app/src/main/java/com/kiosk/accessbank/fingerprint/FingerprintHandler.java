package com.kiosk.accessbank.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintCaptureListener;
import com.zkteco.android.biometric.module.fingerprintreader.FingerprintSensor;
import com.zkteco.android.biometric.module.fingerprintreader.FingprintFactory;
import com.zkteco.android.biometric.module.fingerprintreader.ZKFingerService;
import com.zkteco.android.biometric.module.fingerprintreader.exception.FingerprintException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class FingerprintHandler {

    private static final String TAG = "fingerprint_handler";
    private final Context context;

    private boolean bstart = false;
    private boolean isRegister = false;
    private int uid = 1;
    private byte[][] regtemparray = new byte[3][2048];  //register template buffer array
    private int enrollidx = 0;
    private byte[] lastRegTemp = new byte[2048];

    private FingerprintSensor fingerprintSensor = null;

    private static final int VID = 6997;
    private static final int PID = 288;

    public FingerprintHandler(Context mContext,FingerprintListener listener ) {
        context = mContext;
        this.listener = listener;
        LogHelper.setLevel(Log.ASSERT);
        // Start fingerprint sensor
        Map fingerprintParams = new HashMap();
        //set vid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //set pid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        fingerprintSensor = FingprintFactory.createFingerprintSensor(context, TransportType.USB, fingerprintParams);
    }

    private FingerprintListener listener ;

    public void saveBitmap(Bitmap bm) {
        File f = new File("/sdcard/fingerprint", "test.bmp");
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void OnBnBegin()
    {
        try {
            if (bstart) return;
            fingerprintSensor.open(0);
            final FingerprintCaptureListener captureListener = new FingerprintCaptureListener() {
                @Override
                public void captureOK(final byte[] fpImage) {
                    final int width = fingerprintSensor.getImageWidth();
                    final int height = fingerprintSensor.getImageHeight();
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSuccess(fpImage,"success");

                        }
                    });
//                    listener.captureOK(fpImage);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(null != fpImage)
//                            {
//                                ToolUtils.outputHexString(fpImage);
//                                LogHelper.i("width=" + width + "\nHeight=" + height);
//                                Bitmap bitmapFp = ToolUtils.renderCroppedGreyScaleBitmap(fpImage, width, height);
//                                //saveBitmap(bitmapFp);
//                                imageView.setImageBitmap(bitmapFp);
//                            }
//                            //Log.d(TAG,"FakeStatus:" + fingerprintSensor.getFakeStatus());
//                        }
//                    });
                }
                public void captureError(FingerprintException e) {
                    final FingerprintException exp = e;
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailed(e.getMessage());

                        }
                    });

//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            LogHelper.d("captureError  errno=" + exp.getErrorCode() +
//                                    ",Internal error code: " + exp.getInternalErrorCode() + ",message=" + exp.getMessage());
//                        }
//                    });
                }
                public void extractError(final int err)
                {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailed("error code");

                        }
                    });

//                    listener.onSuccess();
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.d(TAG,"extract fail, errorcode:" + err);
//                        }
//                    });
                }

                public void extractOK(final byte[] fpTemplate)
                {
                    final byte[] tmpBuffer = fpTemplate;
                    if (isRegister) {
                        byte[] bufids = new byte[256];
                        int ret = ZKFingerService.identify(tmpBuffer, bufids, 55, 1);
                        if (ret > 0)
                        {
                            String strRes[] = new String(bufids).split("\t");
                            Log.d(TAG,"the finger already enroll by " + strRes[0] + ",cancel enroll");
                            isRegister = false;
                            enrollidx = 0;
                            return;
                        }

                        if (enrollidx > 0 && ZKFingerService.verify(regtemparray[enrollidx-1], tmpBuffer) <= 0)
                        {
                            Log.d(TAG,"please press the same finger 3 times for the enrollment");
                            return;
                        }
                        System.arraycopy(tmpBuffer, 0, regtemparray[enrollidx], 0, 2048);
                        enrollidx++;
                        if (enrollidx == 3) {
                            byte[] regTemp = new byte[2048];
                            if (0 < (ret = ZKFingerService.merge(regtemparray[0], regtemparray[1], regtemparray[2], regTemp))) {
                                ZKFingerService.save(regTemp, "test" + uid++);

                                System.arraycopy(regTemp, 0, lastRegTemp, 0, ret);
                                //Base64 Template
                                String strBase64 = Base64.encodeToString(regTemp, 0, ret, Base64.NO_WRAP);
                                Log.d(TAG,"enroll succ, uid:" + uid + "count:" + ZKFingerService.count());
                            } else {
                                Log.d(TAG,"enroll fail");
                            }
                            isRegister = false;
                        } else {
                            Log.d(TAG,"You need to press the " + (3 - enrollidx) + "time fingerprint");
                        }
                    } else {
                        byte[] bufids = new byte[256];
                        int ret = ZKFingerService.identify(tmpBuffer, bufids, 70, 1);
                        if (ret > 0) {
                            String strRes[] = new String(bufids).split("\t");
                            Log.d(TAG,"identify succ, userid:" + strRes[0] + ", score:" + strRes[1]);
                        } else {
                            Log.d(TAG,"identify fail");
                        }
                        //Base64 Template
                        //String strBase64 = Base64.encodeToString(tmpBuffer, 0, fingerprintSensor.getLastTempLen(), Base64.NO_WRAP);
                    }
                }


            };
            fingerprintSensor.setFingerprintCaptureListener(0, captureListener);
            fingerprintSensor.startCapture(0);
            bstart = true;
            Log.d(TAG,"start capture succ");
        }catch (FingerprintException e)
        {
            Log.d(TAG,"begin capture fail.errorcode:"+ e.getErrorCode() + "err message:" + e.getMessage() + "inner code:" + e.getInternalErrorCode());
        }
    }

    public void OnBnStop() {
        try {
            if (bstart)
            {
                //stop capture
                fingerprintSensor.stopCapture(0);
                bstart = false;
                fingerprintSensor.close(0);
                Log.d(TAG,"stop capture succ");
            }
            else
            {
                Log.d(TAG,"already stop");
            }
        } catch (FingerprintException e) {
            Log.d(TAG,"stop fail, errno=" + e.getErrorCode() + "\nmessage=" + e.getMessage());
        }
    }

    public void OnBnEnroll(View view) {
        if (bstart) {
            isRegister = true;
            enrollidx = 0;
            Log.d(TAG,"You need to press the 3 time fingerprint");
        }
        else
        {
            Log.d(TAG,"please begin capture first");
        }
    }

    public void OnBnVerify(View view) {
        if (bstart) {
            isRegister = false;
            enrollidx = 0;
        }else {
            Log.d(TAG,"please begin capture first");
        }
    }


    public interface FingerprintListener {
        void onSuccess(byte[] value,String result);

        void onFailed(String message);
    }

}