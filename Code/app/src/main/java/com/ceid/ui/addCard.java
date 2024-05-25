package com.ceid.ui;

import static com.ceid.model.users.User.currentUser;

import android.content.Intent;
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
import com.ceid.model.payment_methods.Card;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class addCard extends AppCompatActivity implements postInterface {
    private Customer customer;
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
    public void addCardButton(View view) {
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> cardCred = new LinkedHashMap<>();
        user = User.currentUser();
        cardCred.put("username", user.getUsername());
        cardCred.put("cardNum", cardNum.getText().toString());
        cardCred.put("expDate", expDate.getText().toString());
        cardCred.put("owner", owner.getText().toString());
        cardCred.put("ccv", ccv.getText().toString());
        values.add(cardCred);
        String jsonString = jsonStringParser.createJsonString("insertCard", values);
        PostHelper addc = new PostHelper(this);

        addc.card(api, jsonString);

    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        boolean bool=jsonStringParser.getbooleanFromJson(response);
        if(bool)
        {
            Toast.makeText(getApplicationContext(), "Card added successfully!",
                    Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);

        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong card information!",
                    Toast.LENGTH_LONG).show();
        }
        /*List<Card> card=jsonStringParser.parseDataList(response.body().string(), Card.class);
        Log.d("card",card.get(0).printCard());
        Toast.makeText(getApplicationContext(), "Card added successfully!",
                Toast.LENGTH_LONG).show();
        //Intent intent= new Intent(getApplicationContext(), addCard.class);
        //startActivity(intent);
        */
    }

    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "Card already exists!",
                Toast.LENGTH_LONG).show();
    }
}
