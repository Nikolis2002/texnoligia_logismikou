package com.ceid.ui;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ChargeWalletScreen extends AppCompatActivity implements postInterface {
    private User user;
    protected Customer customer;
    private TextView money;
    private List<Card> cards;
    private Wallet wallet;
    private String value;
    ArrayAdapter<String> adapter;
    ArrayList<String> mycards;
    private MaterialAutoCompleteTextView materialSpinner;
    private List<String> cardSpinner;
    private Spinner arrayCards;
    private EditText amount;

    protected void onResume()
    {
        super.onResume();
        cards = user.getWallet().getCards();

        if (cards.isEmpty())
        {
            noPaymentAlert();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_wallet_screen);

        money = findViewById(R.id.cardNum);
        //arrayCards = findViewById(R.id.spinner);
        amount = findViewById(R.id.amount);
        user = User.getCurrentUser();

        Wallet wallet = user.getWallet();
        money.setText(String.format("Balance: %.02f€", user.getWallet().getBalance()));

        //Check if customer has cards
        //=======================================================================================================
        cards = user.getWallet().getCards();

        if (cards.isEmpty())
        {
            noPaymentAlert();
        }

        //Initialize card spinner
        //=======================================================================================================

        materialSpinner = findViewById(R.id.cardSpinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        materialSpinner.setAdapter(adapter);

        ArrayList<String> mycards = new ArrayList<>();

        for (Card card :cards ) {
            mycards.add(card.getCardnumber());
        }

        adapter.addAll(mycards);
        adapter.notifyDataSetChanged();
        Log.d("test",materialSpinner.getText().toString());

        /*
        ArrayAdapter ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cardSpinner);
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);
        arrayCards.setAdapter(ad);*/
    }


    public void chargeWalletButton(View view){
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> charge=new LinkedHashMap<>();
        charge.put("username",user.getUsername());
        charge.put("amount",amount.getText().toString());
        charge.put("value",materialSpinner.getText().toString());

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
            user.getWallet().addToWallet(Double.parseDouble(amount.getText().toString()));
            Intent intent = new Intent(getApplicationContext(), MainScreen.class);
            startActivity(intent);
            finish();

        }
        else{
            Toast.makeText(getApplicationContext(), "No money!",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "error",
                Toast.LENGTH_LONG).show();
    }

    //ERRORS
    //===================================================================================================

    public void noPaymentAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No Payment Methods");
        builder.setMessage("There are no payment methods in your account. Would you like to insert one?");

        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(ChargeWalletScreen.this, addCard.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Maybe Later", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
