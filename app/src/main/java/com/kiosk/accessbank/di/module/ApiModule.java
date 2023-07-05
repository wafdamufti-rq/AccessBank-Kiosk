package com.kiosk.accessbank.di.module;

import com.kiosk.accessbank.source.api.UserService;
import com.kiosk.accessbank.source.api.ServiceService;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Provides
    OkHttpClient provideOkhttpClient() {
        return new OkHttpClient.Builder()
                .callTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().client(client).baseUrl("https://59cbe56d-9479-4d09-93fd-665d3a88127e.mock.pstmn.io/").addCallAdapterFactory(RxJava3CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
    }


    @Provides
    UserService provideLoginService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Provides
    ServiceService provideServiceService(Retrofit retrofit) {
        return retrofit.create(ServiceService.class);
    }
}
