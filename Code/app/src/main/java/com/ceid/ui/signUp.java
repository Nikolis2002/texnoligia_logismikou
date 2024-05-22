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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class signUp extends AppCompatActivity implements postInterface {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    protected Uri photoDir;
    private static final int PICK_IMAGE = 1;
    private EditText username,password,name,surname,phoneNumber;
    private CheckBox check;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    photoDir=uri;
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
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            String fileName = "selected_image_" + System.currentTimeMillis() + ".jpg";
            File directory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MovFast");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Log.e("PhotoPicker", "Failed to create directory: " + directory.getAbsolutePath());
                    return;
                }
            }
            File file = new File(directory, fileName);

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Log.d("PhotoPicker", "Image saved: " + file.getAbsolutePath());
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
            Map<String, Object> signUpUser = new HashMap<>();
            signUpUser.put("username",username);
            signUpUser.put("password",password);
            signUpUser.put("name",name);
            signUpUser.put("lname",surname);
            signUpUser.put("phoneNumber",phoneNumber);
            signUpUser.put("license","insert license here");




        }
        else
        {
            Toast.makeText(getApplicationContext(), "You must agree to the Terms of Service and Privacy Policy!",
                    Toast.LENGTH_LONG).show();
        }
    }
    public void signIn(View view)
    {
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
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