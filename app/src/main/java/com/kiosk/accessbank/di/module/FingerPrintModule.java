package com.kiosk.accessbank.di.module;


import android.content.Context;

import com.kiosk.accessbank.fingerprint.FingerprintHandler;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Module
@InstallIn(ActivityComponent.class)
public class FingerPrintModule {

    @Provides
    public FingerprintHandler provideFingerPrint(@ApplicationContext Context context){
        return  new FingerprintHandler(context);
    }
}
