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
        list.add(new Service(1,"Electricity Token/ Data/Airitme Transfer Purchase"));
        list.add(new Service(2,"Dispense Error"));
        list.add(new Service(3,"Balance Enquiry"));
        list.add(new Service(4,"Local Fund Transfer"));
        list.add(new Service(5,"Statement of Account"));
        list.add(new Service(6,"Card Request, Card Blok/ Unblock, PIN Activation"));
        list.add(new Service(7,"Foreign Fund Transfer"));
        list.add(new Service(8,"Pay Direct(Cable TV,Lottery, FIRS"));
        list.add(new Service(9, "Update Info"));
        return Single.just(new ApiResponse<>(200, "success", list
        ));
    }
}
