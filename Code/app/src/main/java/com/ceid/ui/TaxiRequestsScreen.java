package com.ceid.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ceid.model.service.TaxiRequest;

import java.util.Arrays;
import java.util.List;

public class TaxiRequestsScreen extends AppCompatActivity {
    private RecyclerView requestView;
    private TaxiRequestAdapter requestAdapter;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taxi_request_screen);

        requestView = findViewById(R.id.requestView);
        requestView.setLayoutManager(new LinearLayoutManager(this));
        List<String> taxiRequestList = Arrays.asList("test","test2","test3","test4","test5","test6","test7","test8","test9");
        requestAdapter = new TaxiRequestAdapter(taxiRequestList);
        requestView.setAdapter(requestAdapter);
    }


}
