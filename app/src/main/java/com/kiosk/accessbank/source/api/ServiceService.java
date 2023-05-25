package com.kiosk.accessbank.source.api;

import com.kiosk.accessbank.source.model.Service;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface ServiceService {

    @GET("/wad")
    public Single<ApiResponse<List<Service>>> getAllService();
}
