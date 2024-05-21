package com.ceid.ui;

import static com.ceid.model.users.User.currentUser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.postInterface;
import com.ceid.model.users.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
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
        ApiService api = ApiClient.getApiService();
        PostHelper addc = new PostHelper(this);
    }
    public void addCardButton(View view)
    {
        Map<String, String> data = new HashMap<>();
        user= User.currentUser();
        data.put("username",user.usernameGetter());
        data.put("cardNum",cardNum.getText().toString());
        data.put("expDate",expDate.getText().toString());
        data.put("owner",owner.getText().toString());
        data.put("ccv",ccv.getText().toString());
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        Log.d("kort",jsonString);
        addc.card(api,jsonString);

    }
    public void remCard(View view)
    {

    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {


    }

    @Override
    public void onResponseFailure(Throwable t) {

    }
}
