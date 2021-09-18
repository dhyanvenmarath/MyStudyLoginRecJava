package com.dhyan.mystudyloginrecjava.api;

import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainRetrofitClient {

    private static final String Base_url="https://dhyan/";
  //  private static final String Base_url="https://newsapi.org"




    private static MainRetrofitClient minstence;
    private Retrofit retrofit;


    public MainRetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Base_url)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized MainRetrofitClient getMinstence() {
        if (minstence == null) {
            minstence = new MainRetrofitClient();
        }
        return minstence;
    }

    //connection timeout
    private OkHttpClient getHttpClient() {
        int timeout = 30;
        return new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(chain -> {
                    okhttp3.Request original = chain.request();
                    okhttp3.Request request = original.newBuilder().headers(getHeaders()).build();
                    return chain.proceed(request);
                }).build();
    }

    private HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    private Headers getHeaders() {
        Headers.Builder headers = new Headers.Builder();
        headers.add("Authorization", "Basic fdgdhgfgvbctderfchcnncv=");
        return headers.build();
    }

    public Api getApi() {
        return retrofit.create(Api.class);
    }
}
