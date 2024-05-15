package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;

public class MainScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        ApiService api = ApiClient.getApiService();
        Call<String> call = api.getMessage();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String message = response.body();
                    Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("tete", response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String error = String.valueOf(t.fillInStackTrace());
                Log.d("tete", error);
            }
        });
    }

    public void inCity(View view)
    {
        Intent intent = new Intent(this, InCityScreen.class);
        startActivity(intent);
    }

    public void outCity(View view)
    {
        Intent intent = new Intent(this, OutCityScreen.class);
        startActivity(intent);
    }
}
