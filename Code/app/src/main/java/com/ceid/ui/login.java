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
import com.ceid.model.users.Customer;
import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class login extends AppCompatActivity implements postInterface{
    private String username,password;
    private EditText userText,pass;
    private ImageView visib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userText = (EditText) findViewById(R.id.username_email);
        pass = (EditText) findViewById(R.id.pass);
        visib = (ImageView) findViewById(R.id.visibility);

        ApiService api=ApiClient.getApiService();

        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> user1 = new HashMap<>();
        user1.put("username", "john_doe");
        user1.put("password", "password1");
        user1.put("name", "John");
        user1.put("lname", "Doe");
        user1.put("email", "john@example.com");
        user1.put("phone", "1234567890");
        values.add(user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("username", "jane_doe");
        user2.put("password", "password2");
        user2.put("name", "Jane");
        user2.put("lname", "Doe");
        user2.put("email", "jane@example.com");
        user2.put("phone", "9876543210");
        values.add(user2);

        String jsonString = jsonStringParser.createJsonString("user", values);

        Call<Void> call =api.insertTable(jsonString);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                   Log.d("test","yes");

                } else {
                    //test2
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });


    }
    public void onClickVisib(View view) {
        int cursorPosition = pass.getSelectionStart();
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

        username=userText.getText().toString();
        password=pass.getText().toString();

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

    }

    @Override
    public void onResponseFailure(Throwable t) {
        Log.d("kort","no");

        Toast.makeText(getApplicationContext(), "Invalid Credentials!",
                Toast.LENGTH_LONG).show();
    }

}
