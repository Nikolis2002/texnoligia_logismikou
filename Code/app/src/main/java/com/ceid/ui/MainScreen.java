package com.ceid.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.jsonStringParser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.JsonArray;

public class MainScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        ApiService api = ApiClient.getApiService();

        Call<String> call = api.getTableData("user");

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBodyString = response.body();
                    Log.d("ez",responseBodyString);
                    JsonArray array=jsonStringParser.extractResult(responseBodyString);
                    jsonStringParser.printJsonArray(array);
                } else {
                    Log.d("TAG", "Response not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("TAG", "onFailure: " + t.getMessage(), t);
            }
        });

        //Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                return true; //successfully handled
            }
        });


        /*
        //TEST
        //==================================================================

        String input = "Mon-Fri 08:00-20:00";
        String patternString = "(?<d1>[A-Z][a-z]{2})-(?<d2>[A-Z][a-z]{2})\\s(?<h1>\\d{2}):(?<m1>\\d{2})-(?<h2>\\d{2}):(?<m2>\\d{2})";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find())
        {
            Log.d("REGEXTEST", matcher.group("d1"));
        }
        */
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
