package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
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
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        userText = (EditText) findViewById(R.id.username_email);
        pass = (EditText) findViewById(R.id.pass);
        visib = (ImageView) findViewById(R.id.visibility);
        apiService = ApiClient.getApiService();

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

       // PostHelper requestHandler = new PostHelper(this);
        //requestHandler.login(apiService,"");
        /*if(username.equals(userText.getText().toString())) {
            if (password.equals(pass.getText().toString()))
            {
                Intent intent = new Intent(this,MainScreen.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Wrong Password!",
                        Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Wrong Username!",
                    Toast.LENGTH_LONG).show();
        }*/
    }
    public void signUp(View view)
    {
        Intent intent = new Intent(this,signUp.class);
        startActivity(intent);
    }

    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body().string());
        User ca= jsonStringParser.parseJson(jsonNode);

        if( ca instanceof TaxiDriver){
            Log.d("taxi","yessirTaxi");
            ca.printUser();
            Intent intent= new Intent(this,)
        } else if (ca instanceof Customer) {
            Log.d("Customer","yessirCustomer");
            ca.printUser();
            Customer c= (Customer)ca;
        }

    }

    @Override
    public void onResponseFailure(Throwable t) {
        Log.d("kort","no");
    }

}
