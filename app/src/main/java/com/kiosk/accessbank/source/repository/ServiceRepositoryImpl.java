package com.kiosk.accessbank.source.repository;

import com.google.android.gms.common.api.Api;
import com.kiosk.accessbank.R;
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
        list.add(new Service(1,"Electricity Token/ Data/Airitme Transfer Purchase", R.drawable.ic_phone));
        list.add(new Service(2,"Dispense Error",R.drawable.ic_dispense_error));
        list.add(new Service(3,"Local Fund Transfer",R.drawable.ic_transfer));
        list.add(new Service(4,"Statement of Account",R.drawable.ic_statement));
        list.add(new Service(5,"Card Request, Card Blok/ Unblock, PIN Activation",R.drawable.ic_card));
        list.add(new Service(6,"Foreign Fund Transfer",R.drawable.ic_foreign_fund_transfer));
        list.add(new Service(7,"Pay Direct(Cable TV,Lottery, FIRS",R.drawable.ic_payment_digital));
        list.add(new Service(8, "Update Info",R.drawable.ic_customer_info));
        return Single.just(new ApiResponse<>("00", "success", list
        ));
    }
}
