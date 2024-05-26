package com.ceid.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class addLicense extends AppCompatActivity implements postInterface {
    private ImageView image;
    protected byte[] bArray;
    private User user;
    private Bitmap bitmap;
    private Customer customer;
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_screen);
        image= findViewById(R.id.imageView2);
        text=findViewById(R.id.textView5);

    }
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


            text.setVisibility(View.GONE);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            image.setImageBitmap(bitmap);
            bArray = bos.toByteArray();
            System.out.println(Arrays.toString(bArray));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PhotoPicker", "Error saving image", e);
        }
    }
    public void attachPhoto(View view)
    {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    public void sendReview(View view)
    {
        Log.d("byte", Arrays.toString((bArray)));
        PostHelper lic=new PostHelper(this);
        ApiService api=ApiClient.getApiService();
        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> license=new LinkedHashMap<>();
        user= User.getCurrentUser();
        customer= (Customer) user;
        license.put("username",customer.getUsername());
        license.put("license",Arrays.toString((bArray)));
        values.add(license);
        String jsonString = jsonStringParser.createJsonString("insertLicense", values);
        lic.licenseCall(api,jsonString);
    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
        //ObjectMapper mapper = new ObjectMapper();
        //JsonNode jsonNode = mapper.readTree(response.body().string());
        //byte[] decodedString = Base64.decode(jsonNode.get(0).get(0).get("license_image").get("data").toString(), Base64.DEFAULT);
        //System.out.println(jsonNode.get(0).get(0).get("license_image").get("data"));
        //Blob blob= (Blob) jsonNode.get(0).get(0).get("license_image").get("data");
        //JsonNode licenseNode= jsonNode.get(0).get(0).get("license_image").get("data");
        //System.out.println(jsonNode.get(0).get(0).get("license_image").get("data"));
        //byte[] img = (jsonNode.get(0).get(0).get("license_image").get("data")).binaryValue();
         //System.out.println(Arrays.toString(img));

        //(jsonNode.get(0).get(0).get("license_image").get("data")).forEach(node -> dataList.add(node.intValue()));

        // Convert the list of integers to a byte array
        //byte[] img = bArray;

        //System.out.println(Arrays.toString(img));



        // Decode the byte array into a Bitmap
        //Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        // Set the bitmap to an ImageView
        //image.setImageBitmap(bitmap);
        // Inflate the popup window layout
        View popupView = LayoutInflater.from(this).inflate(R.layout.driverslicensepopup, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Show the popup window
        popupWindow.showAtLocation(
                findViewById(android.R.id.content),
                Gravity.CENTER,
                0,
                0
        );
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent(getApplicationContext(),MainScreen.class);
                startActivity(intent);
            }
        }, 5000);
    }
    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "Check test!",
                Toast.LENGTH_LONG).show();
    }
    public void givenUsingTimer_whenSchedulingTaskOnce_thenCorrect() throws InterruptedException {
        Thread thread;
        Thread.sleep(2000);
    }
}

