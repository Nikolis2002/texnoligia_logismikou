package com.ceid.Network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("/getTableData")
    Call<ResponseBody> getTableData(@Query("tableName") String tableName);

    @GET("/getTracker")
    Call<ResponseBody> getTracker(
            @Query("TrackerType") String TrackerType,
            @Query("addTen") String ten
    );

    @POST("/getFunctionWithParams")
    Call<ResponseBody> callProcedure(@Body String jsonfunction);

    @POST("/insertTaxiService")
    Call<String> insertTaxiService(@Body String jsonBody);

    @POST("/check_user")
    Call<ResponseBody> checkUser(@Body String userParams);

    @GET("/check_reservation")
    Call<String> checkReservation(@Query("vehicle") String id);

    @POST("/reserveRental")
    Call<ResponseBody> reserveRental(@Body String jsonfunction);

    @GET("/getGarages")
    Call<ResponseBody> getGarages();

    @GET("/history")
    Call<ResponseBody> getHistory(@Query("user") String user);
}
