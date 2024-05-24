package com.ceid.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ceid.model.service.GasStation;
import com.ceid.model.transport.VehicleTracker;
import com.ceid.util.GenericCallback;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostHelper {
    private postInterface callback;

    public PostHelper(postInterface callback){
        this.callback=callback;
    }
    public void insertUser(ApiService api,String userString){

        Call<Void> call= api.postUser(userString);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                   System.out.println("success message");
                } else {
                    System.out.println("Error message");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }

    public void login(ApiService api,String userParams){
        Call<ResponseBody> call= api.checkUser(userParams);
        Log.d("kort","kort was send successfully!!");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(callback!=null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }
    public void card(ApiService api,String cardParams){

            Call<ResponseBody> call= api.getFunction(cardParams);
            Log.d("kort","kort was send successfully!!");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        if(callback!=null) {
                            try {
                                callback.onResponseSuccess(response);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
                        if (callback != null)
                            callback.onResponseFailure(new Throwable("Request failed"));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    System.out.println("Error message");
                }
            });

    }

    public static void getGasStations(ApiService api, String params, GenericCallback<ArrayList<GasStation>> callback){
        Call<ResponseBody> call=api.getTableData(params);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(callback!=null) {
                        ArrayList<GasStation> array= null;
                        try {
                            array = jsonStringParser.parseGarage(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(array);
                    }
                } else {
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    /*
    public static void getTackerOfRental(ApiService api, String params, GenericCallback<VehicleTracker> callback){
        Call<ResponseBody> call=api.getTracker(params);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(callback!=null) {
                        VehicleTracker tracker=null;
                        try {
                            tracker = jsonStringParser.parseTracker(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(tracker);
                    }
                } else {
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });


    }
    public void charge(ApiService api,String value){

        Call<ResponseBody> call= api.getFunction(value);
        Log.d("kort","kort was send successfully!!");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if(callback!=null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });

    }*/
}

