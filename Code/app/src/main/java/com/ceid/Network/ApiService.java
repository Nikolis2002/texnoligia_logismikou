package com.ceid.Network;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/test")
    Call<String> getMessage();

    @GET("/getTableData")
    Call<ArrayList<ResponseBody>> getTableData(@Query("tableName") String tableName);
}
