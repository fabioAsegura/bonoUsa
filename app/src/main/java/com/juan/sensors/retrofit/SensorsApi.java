package com.juan.sensors.retrofit;

import com.juan.sensors.model.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by tinashe on 2017/11/02.
 */

public interface SensorsApi {

    @GET("BonoUsa/Add")
    Observable<Response<BaseResponse>> sendData(@QueryMap Map<String, Double> dataMap);
}
