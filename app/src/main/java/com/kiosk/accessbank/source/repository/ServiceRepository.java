package com.kiosk.accessbank.source.repository;

import com.kiosk.accessbank.source.api.ApiResponse;
import com.kiosk.accessbank.source.model.Service;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface ServiceRepository {

    public Single<ApiResponse<ArrayList<Service>>> getAllServices(String id);
}
