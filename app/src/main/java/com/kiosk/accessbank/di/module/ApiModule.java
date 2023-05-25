package com.kiosk.accessbank.di.module;

import com.kiosk.accessbank.source.api.LoginService;
import com.kiosk.accessbank.source.api.ServiceService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    Retrofit provideRetrofit(){
        return new Retrofit.Builder().baseUrl("https://basreng").build();
    }


    @Provides
    LoginService provideLoginService(Retrofit retrofit){
        return retrofit.create(LoginService.class);
    }

    @Provides
    ServiceService provideServiceService(Retrofit retrofit){
        return retrofit.create(ServiceService.class);
    }
}
