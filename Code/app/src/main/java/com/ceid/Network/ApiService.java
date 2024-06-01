package com.ceid.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/test")
    Call<String> getMessage();

    @GET("/getTableData")
    Call<ResponseBody> getTableData(@Query("tableName") String tableName);

    @GET("/getTracker")
    Call<ResponseBody> getTracker(
            @Query("TrackerType") String TrackerType,
            @Query("addTen") String ten
    );


    @POST("/getFunctionWithParams")
    Call<ResponseBody> callProcedure(@Body String jsonfunction);

    @POST("/getFunctionNoParams")
    Call<String> getFuncNoParams(@Body String jsonfunction);

    @POST("/insertTaxiService")
    Call<String> insertTaxiService(@Body String jsonBody);

    @POST("/insertTable")
    Call<ResponseBody> insertTable(@Body String jsonTable);

    @POST("/insert_user")
    Call<Void> postUser(@Body String userString);

    @POST("/check_user")
    Call<ResponseBody> checkUser(@Body String userParams);

    @POST("/add_card")
    Call<ResponseBody> addCard(@Body String cardParams);

    @GET("/check_reservation")
    Call<String> checkReservation(@Query("vehicle") String id);

    @POST("/reserveRental")
    Call<ResponseBody> reserveRental(@Body String jsonfunction);

    @GET("/getGarages")
    Call<ResponseBody> getGarages();

    @GET("/getLicense")
    Call<ResponseBody> getLicense(@Body String jsonfunction);

    @POST ("/savePhoto")
    Call<ResponseBody> savePhoto(@Body String  json);

    @GET("/history")
    Call<ResponseBody> getHistory(@Query("user") String user);

    @POST("/finalRentalService")
    Call<ResponseBody> finalRentalService(@Body String jsonfunction);
}
