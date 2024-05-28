package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.service.TaxiRequest;
import com.ceid.model.users.Customer;
import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class Login extends AppCompatActivity implements postInterface{
    private String username,password;
    private EditText userText,pass;
    private ImageView visib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userText = (EditText) findViewById(R.id.username_email);
        pass = (EditText) findViewById(R.id.pass);
    }
    public void onClickVisib(View view) {
        int cursorPosition = pass.getSelectionStart();
        System.out.println(User.getCurrentUser());
        visib.setSelected(!visib.isSelected());
        if (visib.isSelected()) {

            visib.setImageResource(R.drawable.baseline_visibility_off_24);
            pass.setInputType(InputType.TYPE_CLASS_TEXT);
            pass.setSelection(cursorPosition);
        } else {
            visib.setImageResource(R.drawable.baseline_visibility_24);
            pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            pass.setSelection(cursorPosition);
        }
    }
    public void buttonLogin(View view)
    {
        ApiService api = ApiClient.getApiService();

        PostHelper postLogin = new PostHelper(this);

        username=userText.getText().toString().trim();
        password=pass.getText().toString().trim();

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        // Convert the map to a JSON string
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        Log.d("kort",jsonString);
        postLogin.login(api,jsonString);

    }
    public void signUp(View view)
    {
        Intent intent = new Intent(this,signUp.class);
        startActivity(intent);
    }

    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body().string());
        User user= jsonStringParser.parseJson(jsonNode);

        //App app = (App) getApplicationContext();
        //app.setUser(user);
        User.setCurrentUser(user);

        if( user instanceof TaxiDriver){
            Log.d("taxi","yessirTaxi");
            user.printUser();
            Intent intent= new Intent(getApplicationContext(), MainScreenTaxi.class);
            intent.putExtra("taxi driver",user);
            startActivity(intent);
        } else if (user instanceof Customer) {
            Log.d("Customer","yessirCustomer");
            user.printUser();
            Intent intent= new Intent(getApplicationContext(), MainScreen.class);
            intent.putExtra("customer",user);
            startActivity(intent);
        }
        finish();

    }

    @Override
    public void onResponseFailure(Throwable t) {
        Log.d("kort","no");

        Toast.makeText(getApplicationContext(), "Invalid Credentials!",
                Toast.LENGTH_LONG).show();
    }

}
