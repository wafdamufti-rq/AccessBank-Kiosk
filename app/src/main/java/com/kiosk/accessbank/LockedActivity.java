package com.kiosk.accessbank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.UserManager;
import android.provider.Settings;
import android.widget.Toast;

import com.kiosk.accessbank.receiver.DeviceAdminReceiver;

public class LockedActivity extends AppCompatActivity {

    public static final String LOCK_ACTIVITY_KEY = "lock_activity";
    public static final int FROM_LOCK_ACTIVITY = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked);

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}