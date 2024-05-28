package com.ceid.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class signUp extends AppCompatActivity implements postInterface {
    private byte[] bArray;
    private EditText username,password,name,surname,phoneNumber,email;
    private CheckBox check;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    saveImage(uri);
                } else {
                    Toast.makeText(getApplicationContext(), "No media selected!",
                            Toast.LENGTH_LONG).show();
                }
            });
    public void saveImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e("PhotoPicker", "InputStream is null for URI: " + uri);
                return;
            }
            // PostHelper lic=new PostHelper(this);
            // ApiService api=ApiClient.getApiService();
            // List<Map<String, Object>> values = new ArrayList<>();
            //Map<String, Object> license=new LinkedHashMap<>();
            //user= User.getCurrentUser();
            //customer= (Customer) user;
            //license.put("username",customer.getUsername());
            //values.add(license);
            //String jsonString = jsonStringParser.createJsonString("getLicense", values);
            //lic.getLicense(api,jsonString);

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bArray = bos.toByteArray();
            //System.out.println(Arrays.toString(bArray));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PhotoPicker", "Error saving image", e);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        check = findViewById(R.id.checkBox);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        email=findViewById(R.id.email);
        surname = findViewById(R.id.surname);
        phoneNumber = findViewById(R.id.phoneNumber);


    }
    public void signUpButton(View view)
    {
        if(check.isChecked())
        {
            if (username.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Username cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (password.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Password cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (name.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Name cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (surname.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Surname cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (phoneNumber.getText().toString().isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Phone number cannot be empty!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            if (phoneNumber.getText().toString().length()!=10)
            {
                Toast.makeText(getApplicationContext(), "Not a valid phone number!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            PostHelper license = new PostHelper(this);
            ApiService api = ApiClient.getApiService();
            List<Map<String, Object>> values = new ArrayList<>();
            Map<String, Object> signUpUser=new LinkedHashMap<>();
            signUpUser.put("username",username.getText().toString());
            signUpUser.put("password",password.getText().toString());
            signUpUser.put("name",name.getText().toString());
            signUpUser.put("lname",surname.getText().toString());
            signUpUser.put("email",email.getText().toString());
            signUpUser.put("phone",phoneNumber.getText().toString());
            signUpUser.put("license",Arrays.toString(bArray));
            values.add(signUpUser);
            String jsonString = jsonStringParser.createJsonString("signUp", values);
            license.signUp(api,jsonString);

            finish();

        }
        else
        {
            Toast.makeText(getApplicationContext(), "You must agree to the Terms of Service and Privacy Policy!",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void signIn(View view)
    {
        finish();
    }
    public void uploadLicense(View view)
    {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {

        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }


    @Override
    public void onResponseFailure(Throwable t) {

    }
}