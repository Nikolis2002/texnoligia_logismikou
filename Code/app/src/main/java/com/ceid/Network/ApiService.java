package com.ceid.Network;
import java.util.ArrayList;

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
    Call<String> getTableData(@Query("tableName") String tableName);

    @POST("/getFunctionWithParams")
    Call<ResponseBody> getFunction(@Body String jsonfunction);

    @POST("/getFunctionNoParams")
    Call<String> getFuncNoParams(@Body String jsonfunction);

    @POST("/insertTable")
    Call<Void> insertTable(@Body String jsonTable);

    @POST("/insert_user")
    Call<Void> postUser(@Body String userString);

    @POST("/check_user")
    Call<ResponseBody> checkUser(@Body String userParams);
}
