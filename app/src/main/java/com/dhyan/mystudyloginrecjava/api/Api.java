package com.dhyan.mystudyloginrecjava.api;

import com.dhyan.mystudyloginrecjava.models.MainBody;
import com.dhyan.mystudyloginrecjava.models.MainResponseValues;
import com.dhyan.mystudyloginrecjava.models.CustomerMainRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    @POST("api/login")
    Call<MainResponseValues> signIn(@Body MainBody c);


    @GET("api/customers")
    Call<CustomerMainRes> getCustomerDetails(@Query("company_id") String id, @Query("search")
            String search_id, @Query("page") int page_no, @Query("user_id")String userId);


}
