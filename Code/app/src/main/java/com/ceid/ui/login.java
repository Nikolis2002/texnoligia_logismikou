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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.postInterface;

public class login extends AppCompatActivity{
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

    /*@Override
    public void onResponseSuccess(String data) {
        // Start the new activity on successful response
        Intent intent = new Intent(login.this, MainScreen.class);
        intent.putExtra("key", data);
        startActivity(intent);
    }*/

   /* @Override
    public void onResponseFailure(Throwable t) {
        // Handle the failure case
        Toast.makeText(login.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }*/
}
