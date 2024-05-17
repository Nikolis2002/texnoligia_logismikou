package com.ceid.Network;

import android.util.Log;

import androidx.annotation.NonNull;

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
        Call<Void> call= api.checkUser(userParams);
        Log.d("kort","kort was send successfully!!");
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    if(callback!=null)
                        callback.onResponseSuccess("Success Data");
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }
}
