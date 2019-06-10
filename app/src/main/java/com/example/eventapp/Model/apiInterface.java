package com.example.eventapp.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by Hp on 8/2/2017.
 */

public interface apiInterface {


    @GET("product")
    Call<List<Item>> getItem();

    @GET("product/epaper/{link}")
    Call<String> getCount(@Path(value = "link") String link);


    @GET("/product/detail/{link}")
    Call<Item> getDetail(@Path(value = "link") String link);


    @FormUrlEncoded
    @POST("product")
    Call<ArrayList<Item>> getJenis(@Field("type") String jenis, @Field("page") int offset);

    @FormUrlEncoded
    @POST("product")
    Call<ArrayList<Item>> getSearch (@Field("type") String jenis, @Field("title") String title,@Field("page") int offset);



    class AppSetup{



        @SerializedName(value="status")
        String status;

        @SerializedName(value="version")
        String version;


        public String getStatus() { return status; }
        public String getVersion() { return version; }
    }
}