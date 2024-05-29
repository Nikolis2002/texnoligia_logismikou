package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ChargeWalletScreen extends AppCompatActivity implements postInterface {
    private User user;
    protected Customer customer;
    private TextInputEditText currentBalance;
    private List<Card> cards;
    private Wallet wallet;
    private String value;
    ArrayAdapter<String> adapter;
    ArrayList<String> mycards;
    private MaterialAutoCompleteTextView materialSpinner;
    private List<String> cardSpinner;
    private Spinner arrayCards;
    private TextInputEditText amountToAdd;

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

        currentBalance = findViewById(R.id.balance);
        //arrayCards = findViewById(R.id.spinner);
        amountToAdd = findViewById(R.id.amount);
        user = User.getCurrentUser();

        Wallet wallet = user.getWallet();
        currentBalance.setText(String.format("Balance: %.02f€", user.getWallet().getBalance()));

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
    }

    public boolean checkFields(Map<String, Object> fields)
    {
        //Check if fields are empty
        //===============================================================================
        if (Objects.equals(fields.get("card"), ""))
        {
            emptyFieldsAlert("card");
            return false;
        }

        if (Objects.equals(fields.get("amount"), ""))
        {
            emptyFieldsAlert("amount");
            return false;
        }

        //Check if fields have valid formatting
        //===============================================================================
        String regex = "^(\\d{4}[- ]?){3}\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((String)fields.get("card"));

        if (!matcher.matches())
        {
            formattingError("Card does not have the correct format");
            return false;
        }

        regex = "^\\d+(\\.\\d{1,2})?$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher((String)fields.get("amount"));

        if (!matcher.matches())
        {
            formattingError("Amount does not have the correct format");
            return false;
        }

        return true;
    }

    public void getFields(View view)
    {
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> charge=new LinkedHashMap<>();
        charge.put("username",user.getUsername());
        charge.put("amount", amountToAdd.getText().toString());
        charge.put("card", materialSpinner.getText().toString());

        if (checkFields(charge))
        {
            Log.d("WALLETTEST", String.format("%s %s %s", user.getUsername(), amountToAdd.getText().toString(), materialSpinner.getText().toString()));

            values.add(charge);
            String jsonString = jsonStringParser.createJsonString("chargeWallet", values);

            //Contact the bank to see if amount exists
            //If bank verifies that the amount is ok, then update customer wallet in the database
            //Finally, update customer wallet on the app
            PostHelper chargeValue=new PostHelper(this);
            ApiService api=ApiClient.getApiService();
            chargeValue.charge(api,jsonString);
        }
    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        boolean bool=jsonStringParser.getbooleanFromJson(response);

        if(bool) //Amount exists
        {
            Toast.makeText(getApplicationContext(), "Value added successfully!", Toast.LENGTH_LONG).show();

            //Add money to customer wallet
            user.getWallet().addToWallet(Double.parseDouble(amountToAdd.getText().toString()));

            finish();
        }
        else //Bank says the amount doesn't exist
            amountAlert();
    }

    //Failed to reach bank
    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "Failed to reach bank", Toast.LENGTH_LONG).show();
    }

    //ERRORS
    //===================================================================================================

    public void noPaymentAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No Payment Methods");
        builder.setMessage("There are no payment methods in your account. Would you like to insert one?");
        builder.setCancelable(false);

        builder.setPositiveButton("Insert", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(ChargeWalletScreen.this, PaymentMethodScreen.class);
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

    public void emptyFieldsAlert(String field)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Empty field");
        builder.setMessage("Field " + field + " cannot be empty");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void amountAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Insufficient funds");
        builder.setMessage("The requested amount does not exist in the bank");
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void formattingError(String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Formatting Error");
        builder.setMessage(msg);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
}
