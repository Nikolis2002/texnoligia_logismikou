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
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class MainScreen extends AppCompatActivity implements postInterface
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        ApiService api = ApiClient.getApiService();

        PostHelper postLogin = new PostHelper(this);
        String username="kort";
        String password="123";

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        // Convert the map to a JSON string
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        Log.d("kort",jsonString);
        postLogin.login(api,jsonString);


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

    @Override
    public void onResponseSuccess(String Data) {
        Log.d("kort","yes");
    }

    @Override
    public void onResponseFailure(Throwable t) {
        Log.d("kort","no");
    }
}
