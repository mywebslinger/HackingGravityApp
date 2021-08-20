package com.matt.hacking_gravity.common;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClassRetrofit {
    Context context;

    public ClassRetrofit(Context context) {
        this.context = context;
    }

    public Apiclass myresponse(String url) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        Apiclass service = retrofit.create(Apiclass.class);
        return service;
    }

    public Apiclass categoryres(String url, final String auth_token) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("auth-token", auth_token).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(url)
                .client(httpClient.build())
                .build();
        Apiclass service = retrofit.create(Apiclass.class);
        return service;
    }
}
