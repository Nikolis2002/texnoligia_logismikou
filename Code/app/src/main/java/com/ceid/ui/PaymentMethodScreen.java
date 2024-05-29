package com.ceid.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class PaymentMethodScreen extends AppCompatActivity implements postInterface {
    private User user;
    private EditText cardNum,expDate,owner,ccv;

    ArrayAdapter<String> adapter;
    private MaterialAutoCompleteTextView materialSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_screen);

        user = User.getCurrentUser();

        //Get all the fields
        //=======================================================================================================
        cardNum = findViewById(R.id.cardNum);
        expDate = findViewById(R.id.expDate);
        owner = findViewById(R.id.owner);
        ccv = findViewById(R.id.ccv);

        //Initialize card spinner
        //=======================================================================================================

        materialSpinner = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        materialSpinner.setAdapter(adapter);

        ArrayList<String> mycards = new ArrayList<>();

        for (Card card : user.getWallet().getCards())
        {
            mycards.add(card.getCardnumber());
        }

        adapter.addAll(mycards);
        adapter.notifyDataSetChanged();
    }

    public boolean checkFields(Map<String, Object> fields)
    {
        //Check if fields are empty
        //===============================================================================

        for (Map.Entry<String, Object> entry : fields.entrySet())
        {
            if (Objects.equals(entry.getValue(), ""))
            {
                emptyFieldsAlert(entry.getKey().replace("_"," "));
                return false;
            }

        }

        //Check if fields have valid formatting
        //===============================================================================

        String regex = "^(\\d{4}[- ]?){3}\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((String)fields.get("Card_Number"));

        if (!matcher.matches())
        {
            formattingError("Card does not have the correct format");
            return false;
        }

        if (((String)fields.get("Card_Owner")).length() > 32)
        {
            formattingError("Card Owner name must be at most 32 characters long");
            return false;
        }

        regex = "^(0[1-9]|1[0-2])/(\\d{2})$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher((String)fields.get("Expiration_Date"));

        if (!matcher.matches())
        {
            formattingError("Expiration Date does not have the correct format (must be MM/YY)");
            return false;
        }

        regex = "^\\d{3}$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher((String)fields.get("CVV"));

        if (!matcher.matches())
        {
            formattingError("CVV does not have the correct format");
            return false;
        }

        return true;
    }

    //Add new card
    public void submit(View view)
    {
        ApiService api= ApiClient.getApiService();
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> cardCred = new LinkedHashMap<>();
        user = User.currentUser();

        cardCred.put("Username", user.getUsername());
        cardCred.put("Card_Number", cardNum.getText().toString());
        cardCred.put("Expiration_Date", expDate.getText().toString());
        cardCred.put("Card_Owner", owner.getText().toString());
        cardCred.put("CVV", ccv.getText().toString());

        if (checkFields(cardCred))
        {
            values.add(cardCred);

            String jsonString = jsonStringParser.createJsonString("insertCard", values);
            PostHelper addc = new PostHelper(this);
            addc.card(api, jsonString);
        }
    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException
    {
        boolean bool=jsonStringParser.getbooleanFromJson(response);
        if(bool)
        {
            Toast.makeText(getApplicationContext(), "Card added successfully!",Toast.LENGTH_LONG).show();

            Card card=new Card(cardNum.getText().toString(),owner.getText().toString(),expDate.getText().toString(),ccv.getText().toString());
            user.getWallet().addPaymentMethod(card);

            finish();
        }
        else //Bank could not verify this card
        {
            invalidAccountMsg();
        }
    }

    //Connection error
    @Override
    public void onResponseFailure(Throwable t)
    {
        Toast.makeText(getApplicationContext(), "Failed to reach bank", Toast.LENGTH_LONG).show();
    }

    //ERRORS
    //===================================================================================================
    public void emptyFieldsAlert(String field)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Empty field");
        builder.setMessage(field + " cannot be empty");
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

    public void invalidAccountMsg()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Could not find account");
        builder.setMessage("Card not found. Please check your card details and try again.");
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
