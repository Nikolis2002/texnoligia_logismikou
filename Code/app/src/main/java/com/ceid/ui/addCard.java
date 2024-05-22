package com.ceid.ui;

import static com.ceid.model.users.User.currentUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.users.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class addCard extends AppCompatActivity implements postInterface {
    private User user;
    private EditText cardNum,expDate,owner,ccv;
    private ApiService api;
    private PostHelper addc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addremovecard);
        cardNum = findViewById(R.id.cardNum);
        expDate = findViewById(R.id.expDate);
        owner = findViewById(R.id.owner);
        ccv = findViewById(R.id.ccv);


    }
    public void addCardButton(View view)
    {
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> cardCred = new HashMap<>();
        user= currentUser();
        cardCred.put("username",user.getUsername());
        //data.put("username","bill");
        cardCred.put("cardNum",cardNum.getText().toString());
        cardCred.put("expDate",expDate.getText().toString());
        cardCred.put("owner",owner.getText().toString());
        cardCred.put("ccv",ccv.getText().toString());
        values.add(cardCred);
        String jsonString = jsonStringParser.createJsonString("card", values);
        PostHelper addc = new PostHelper(this);
        ApiService api = ApiClient.getApiService();
        addc.card(api,jsonString);

    }
    public void remCard(View view)
    {

    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        Toast.makeText(getApplicationContext(), "test!",
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "test2!",
                Toast.LENGTH_LONG).show();
    }
}
