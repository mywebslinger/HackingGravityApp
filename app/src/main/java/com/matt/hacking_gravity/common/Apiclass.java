package com.matt.hacking_gravity.common;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Apiclass {
    //http://yoga.codeinfluencer.com/api/subscribe live

    String VideoUrl="http://app.hackinggravity.com/public/";
    String url = "http://app.hackinggravity.com/api/";
    String imgUrl="http://app.hackinggravity.com/public/";

    @FormUrlEncoded
    @POST("registration")
    Call<String> getRegistration(@Field("name") String name, @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);


    @FormUrlEncoded
    @POST("login")
    Call<String> getLogin(@Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);

    @GET("category")
    Call<String> getcategory(@Query("user_id") String user_id);

    @GET("profile")
    Call<String> getprofile(@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("dailyvibe")
    Call<String> getdailyvobs(@Field("user_id") String user_id);

    @GET("userSession")
    Call<String> getusersession(@Query("user_id") String user_id);

    @POST("userSession")
    Call<String> postusersession(@Query("user_id") String user_id,@Query("class_id") String class_id,@Query("date") String date,@Query("duration") String duration);

    @FormUrlEncoded
    @POST("forgotpassword")
    Call<String> getforgotpassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("page")
    Call<String> getpage(@Field("page") String page);

    @FormUrlEncoded
    @POST("sociallogin")
    Call<String> getSocialLogin(@Field("name") String name,
                                @Field("email") String email,
                                @Field("provider") String provider,
                                @Field("device_token") String device_token,
                                @Field("device_type") String device_type,
                                @Field("password") String password,
                                @Field("uid") String uid);


    @GET("subscription")
    Call<String> getsubscription();

    @GET("userSetting")
    Call<String> getusersetting(@Query("user_id") String user_id);

    @FormUrlEncoded
    @POST("userSetting")
    Call<String> postusersetting(@Field("user_id") String user_id,
                                 @Field("reminder") String reminder,
                                 @Field("day") String day,
                                 @Field("time") String time,
                                 @Field("notification") String notification);



    @FormUrlEncoded
    @POST("subscribe")
    Call<String> postsubscribe(@Field("user_id") String user_id,
                                 @Field("subscription_type") String subscription_type);
}


