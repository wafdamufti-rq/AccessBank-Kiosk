package com.kiosk.accessbank.di.module;

import com.kiosk.accessbank.source.api.LoginService;
import com.kiosk.accessbank.source.api.ServiceService;
import com.kiosk.accessbank.source.repository.ServiceRepository;
import com.kiosk.accessbank.source.repository.ServiceRepositoryImpl;
import com.kiosk.accessbank.source.repository.UserRepository;
import com.kiosk.accessbank.source.repository.UserRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class RepositoryModule {

    @Provides
    UserRepository provideUserRepository(LoginService loginService) {
        return new UserRepositoryImpl(loginService);
    }

    @Provides
    ServiceRepository provideServiceRepository(ServiceService serviceService){
        return new ServiceRepositoryImpl(serviceService);
    }
}
