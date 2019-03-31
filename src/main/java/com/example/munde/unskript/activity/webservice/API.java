package com.example.munde.unskript.activity.webservice;

import com.example.munde.unskript.activity.response.TestResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by munde on 29/03/2019.
 */

public interface API {

    @GET("getUrl")
    Call<TestResponse> testResponse(@QueryMap Map<String, String> params);

}
