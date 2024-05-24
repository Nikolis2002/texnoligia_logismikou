package com.ceid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.payment_methods.Card;
import com.ceid.model.payment_methods.Wallet;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChargeWallet extends AppCompatActivity implements postInterface {
    protected User user;
    protected Customer customer;
    private TextView money;
    private List<Card> cards;
    private Wallet wallet;
    private String value;
    private List<String> cardSpinner;
    private Spinner arrayCards;
    private EditText amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chargewallet);

        money=findViewById(R.id.cardNum);
        arrayCards=findViewById(R.id.spinner);
        amount = findViewById(R.id.amount);
        user=User.getCurrentUser();
        customer= (Customer) user;
        Log.d("CustomerMoney",String.valueOf(customer.getWallet().getBalance()));
        Log.d("UserMoney",String.valueOf(user.getWallet().getBalance()));
        money.setText(String.valueOf(customer.getWallet().getBalance()));
        cards=user.getWallet().getCards();
        cardSpinner= new ArrayList<>();
        for (Card card :cards ) {
            cardSpinner.add(card.getCardnumber());
        }
        //Log.d("test", String.valueOf(cards.get(0)));
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardSpinner);
        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner

        arrayCards.setAdapter(ad);


    }


    public void chargeWalletButton(View view){
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> charge=new LinkedHashMap<>();
        charge.put("username", customer.getUsername());
        charge.put("amount",amount.getText().toString());
        charge.put("value",arrayCards.getSelectedItem().toString());

        values.add(charge);
        String jsonString = jsonStringParser.createJsonString("chargeWallet", values);
        PostHelper chargeValue=new PostHelper(this);
        ApiService api=ApiClient.getApiService();
        chargeValue.charge(api,jsonString);
    }


    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        boolean bool=jsonStringParser.getbooleanFromJson(response);
        if(bool)
        {
            Toast.makeText(getApplicationContext(), "Value added successfully!",
                    Toast.LENGTH_LONG).show();
            customer.getWallet().addtoWallet(Double.parseDouble(amount.getText().toString()));
            Intent intent=new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "No money!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseFailure(Throwable t) {

    }
}
