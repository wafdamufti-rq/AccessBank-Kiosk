package com.kiosk.accessbank.source.repository;

import com.google.android.gms.common.api.Api;
import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.api.ServiceService;
import com.kiosk.accessbank.source.model.Service;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ServiceRepositoryImpl extends BaseRepository implements ServiceRepository {

    public ServiceRepositoryImpl(ServiceService serviceService) {

    }

    @Override
    public Single<ApiResponse<ArrayList<Service>>> getAllServices(String id) {
        ArrayList<Service> list = new ArrayList<>();
        list.add(new Service(1, "Update Info"));
        return Single.just(new ApiResponse<ArrayList<Service>>(200, "success", list
        ));
    }
}
