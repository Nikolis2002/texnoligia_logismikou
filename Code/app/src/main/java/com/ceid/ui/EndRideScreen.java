package com.ceid.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.GasStation;
import com.ceid.model.service.Refill;
import com.ceid.model.service.RentalService;
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

public class EndRideScreen extends AppCompatActivity implements postInterface {

    private Bundle bundle;
    private User user;

    //Images
    protected byte[] bArray1,bArray2,bArray3,bArray4;

    //Image size
    private long[] imageSize = new long[4];

    //Fields
    private TextView durationField, costField, pointsField;
    private CheckBox check1,check2,check3,check4;

    //Buttons
    private Button photoButton1,photoButton2,photoButton3,photoButton4;

    //Checks
    private boolean checked1,checked2,checked3,checked4;//check which button was pressed

    //From previous screen
    private RentalService service;
    private double cost;
    private int points;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_ride_screen);

        user=User.getCurrentUser();

        //Retrieve fields
        //===================================================================================
        photoButton1=findViewById(R.id.attach1);
        photoButton2=findViewById(R.id.attach2);
        photoButton3=findViewById(R.id.attach3);
        photoButton4=findViewById(R.id.attach4);

        check1=findViewById(R.id.checkBox1);
        check2=findViewById(R.id.checkBox2);
        check3=findViewById(R.id.checkBox3);
        check4=findViewById(R.id.checkBox4);

        check1.setEnabled(false);
        check2.setEnabled(false);
        check3.setEnabled(false);
        check4.setEnabled(false);

        durationField = findViewById(R.id.time);
        costField = findViewById(R.id.cost);
        pointsField = findViewById(R.id.points);

        //Prevent back press
        //===================================================================================
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });

        //Get data from previous screen
        //===================================================================================

        bundle= getIntent().getExtras();

        String timeString = getIntent().getStringExtra("timestring");
        service = (RentalService) bundle.getSerializable("service");
        points = bundle.getInt("points");
        cost = getIntent().getDoubleExtra("cost", 0);

        //Fill fields
        //===================================================================================

        durationField.setText(timeString);
        pointsField.setText(String.valueOf(service.getPoints()));
        costField.setText(String.format("%.02f€", cost));
    }


    //Activity Result Launcher for when the user wants to get images from the filesystem
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null) {
                    long fileSize = getFileSize(uri);
                    Log.d("FILESIZE", String.format("%.02f MB", fileSize/1048576.0f));

                    saveImage(uri, getFileSize(uri));
                } else {
                    Toast.makeText(getApplicationContext(), "No media selected!",
                            Toast.LENGTH_LONG).show();
                }
            });


    private long getFileSize(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        long size = 0;

        if (cursor != null && cursor.moveToFirst())
        {
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            if (sizeIndex != -1)
                size = cursor.getLong(sizeIndex);

            cursor.close();
        }
        return size;
    }

    public void attachPhoto(View view)
    {
        if(photoButton1.isPressed()){
            checked1=true;
        }
        if(photoButton2.isPressed()){
            checked2=true;
        }

        if(photoButton3.isPressed()){
            checked3=true;
        }
        if(photoButton4.isPressed()){
            checked4=true;
        }

        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void saveImage(Uri uri, long byteCount) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e("PhotoPicker", "InputStream is null for URI: " + uri);
                return;
            }
            // First decode with inJustDecodeBounds=true to check dimensions
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close(); // Close the input stream to reopen it later

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 800, 800); // Adjust to desired dimensions
            options.inJustDecodeBounds = false;

            // Reopen the input stream and decode with inSampleSize set
            inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Compress the bitmap
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);

            long mb = 1048576;
            int temp = -1;

            if(checked1){
                checked1=false;
                check1.setChecked(true);
                bArray1 = bos.toByteArray();

                temp= 0;
            }
            if(checked2){
                checked2=false;
                check2.setChecked(true);
                bArray2 = bos.toByteArray();

                temp = 1;
            }

            if(checked3){
                checked3=false;
                check3.setChecked(true);
                bArray3 = bos.toByteArray();

                temp = 2;
            }
            if(checked4){
                checked4=false;
                check4.setChecked(true);
                bArray4 = bos.toByteArray();

                temp = 3;
            }

            if (temp != -1)
            {
                if (byteCount > 10*mb)
                    imageSize[temp] = byteCount;
                else
                    imageSize[temp] = -1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PhotoPicker", "Error saving image", e);
        }
    }

    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public void submit(View view)
    {
        //Check if all image fields are filled
        if(check1.isChecked()&&check2.isChecked()&&check3.isChecked()&&check4.isChecked())
        {

            //Check size of the images
            for (int i = 0; i < 4; i++)
            {
                if (imageSize[i] > 0)
                {
                    bigFileSize(i);
                    return;
                }
            }

            //Withdraw from wallet and add points to customer
            //===========================================================================

            user.getWallet().withdraw(cost);
            ((Customer)user).addPoints(points);

            service.setPayment(new Payment(cost, Payment.Method.WALLET));

            //Database
            //===========================================================================
            List<Map<String, Object>> values = new ArrayList<>();
            Map<String, Object> sides=new LinkedHashMap<>();

            sides.put("points",service.getPoints());
            sides.put("userName",user.getUsername());
            sides.put("pValue", cost);
            sides.put("pMethod","WALLET");
            sides.put("rentalId",service.getId());

            if (service.getRefill() != null)
            {
                if (service.getRefill().getSuccess())
                {
                    int diff=service.getRefill().getEndGas().posDiff(service.getRefill().getStartGas());

                    sides.put("stationId",service.getRefill().getGasStation().getid());
                    sides.put("initGas",service.getRefill().getStartGas().getValue());
                    sides.put("addedGas",diff);
                    sides.put("success",service.getRefill().getSuccess());
                    sides.put("refillDate",service.getRefill().getDate());
                }
                else
                {
                    sides.put("stationId",service.getRefill().getGasStation().getid());
                    sides.put("initGas",service.getRefill().getStartGas().getValue());
                    sides.put("addedGas",0);
                    sides.put("success",service.getRefill().getSuccess());
                    sides.put("refillDate",service.getRefill().getDate());
                }
            }
            else
            {
                sides.put("stationId",null);
                sides.put("initGas",null);
                sides.put("addedGas",null);
                sides.put("success",null);
                sides.put("refillDate",null);
            }

            sides.put("leftimg",Arrays.toString(bArray3));
            sides.put("rightimg",Arrays.toString(bArray4));
            sides.put("frontimg",Arrays.toString(bArray1));
            sides.put("backimg",Arrays.toString(bArray2));

            values.add(sides);

            PostHelper end=new PostHelper(this);
            ApiService api = ApiClient.getApiService();

            String jsonString = jsonStringParser.createJsonString("insertFinalRentalService", values);
            end.finalRental(api,jsonString);
        }
        else
        {
            //emptyFieldsMsg
            Toast.makeText(getApplicationContext(), "Send all photos first!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
            ((Customer)user).getHistory().add(service);

            Intent intent=new Intent(getApplicationContext(),MainScreen.class);
            startActivity(intent);
            finish();
    }

    @Override
    public void onResponseFailure(Throwable t) {
            //do nothing!!
    }

    //ERRORS
    //===================================================================================================
    public void bigFileSize(int i)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Image too big");
        builder.setMessage(String.format("Image %d is too big, must be at most 10 MB (size was %.02f MB)", i+1, imageSize[i]/1048576.0f));
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


