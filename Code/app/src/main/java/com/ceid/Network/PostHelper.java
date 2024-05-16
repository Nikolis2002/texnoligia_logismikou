package com.ceid.Network;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostHelper {

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
}
