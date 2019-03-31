package com.example.munde.unskript.activity.webservice;

import retrofit2.Retrofit;

/**
 * Created by munde on 29/03/2019.
 */

public class RestClient {
    public static final String baseUrl = "http://192.168.1.54:9000/controllers/ctrl-project/";
    public static Retrofit client = new Retrofit.Builder().baseUrl(baseUrl).build();

}
