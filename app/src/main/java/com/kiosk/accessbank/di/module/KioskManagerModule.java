package com.kiosk.accessbank.di.module;


import android.content.Context;

import com.kiosk.accessbank.util.KioskManager;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class KioskManagerModule {

    @Provides
    public KioskManager provideKioskManager(@ApplicationContext Context context){
        return new KioskManager(context);
    }
}
