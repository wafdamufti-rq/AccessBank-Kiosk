package com.kiosk.accessbank.util;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;

import com.kiosk.accessbank.receiver.DeviceAdminReceiver;

public class KioskManager {

    private boolean lockedMode = false;

    private Context context;

    private ComponentName adminComponentName;
    private DevicePolicyManager mDpm;

    private View mDecorView;

    private Activity activity;

    public KioskManager(Context context) {
        this.context = context;
    }

    public void setUpAdmin(Activity activity) {
        this.activity = activity;
        if (!isLockedMode()) {
            ComponentName deviceAdmin = new ComponentName(activity, DeviceAdminReceiver.class);
            mDpm = (DevicePolicyManager) activity.getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (!mDpm.isAdminActive(deviceAdmin)) {
//                Log.e("Kiosk Mode Error", activity.getString(R.string.not_device_admin));
            }

            if (mDpm.isDeviceOwnerApp(activity.getPackageName())) {
                mDpm.setLockTaskPackages(deviceAdmin, new String[]{activity.getPackageName()});
            } else {
//                Log.e("Kiosk Mode Error", getString(R.string.not_device_owner));
            }

            enableKioskMode(true);
            //TODO : for clear device Owner
//        } else {
//            mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
//            mDpm.clearDeviceOwnerApp(getPackageName());
        }

        mDecorView = activity.getWindow().getDecorView();
        hideSystemUI();
    }

    public void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                if (mDpm.isLockTaskPermitted(activity.getPackageName())) {
                    lockedMode = true;
                    activity.startLockTask();
                } else {
                    lockedMode = false;
//                    Log.e("Kiosk Mode Error", getString(R.string.kiosk_not_permitted));
                }
            } else {
                lockedMode = false;
                activity.stopLockTask();
            }
        } catch (Exception e) {
            lockedMode = false;
            // TODO: Log and handle appropriately
//            Log.e("Kiosk Mode Error", e.getMessage());
        }
    }

    protected void hideSystemUI() {
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public boolean isLockedMode() {
        return lockedMode;
    }
}
