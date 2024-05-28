package com.ceid.ui;

import static com.ceid.model.payment_methods.Payment.Method.WALLET;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.ceid.Network.ApiClient;
import com.ceid.Network.ApiService;
import com.ceid.Network.PostHelper;
import com.ceid.Network.jsonStringParser;
import com.ceid.Network.postInterface;
import com.ceid.model.payment_methods.Payment;
import com.ceid.model.service.GasStation;
import com.ceid.model.service.Rating;
import com.ceid.model.service.Refill;
import com.ceid.model.service.RentalService;
import com.ceid.model.transport.CityCar;
import com.ceid.model.transport.Rental;
import com.ceid.model.transport.SpecializedTracker;
import com.ceid.model.transport.Transport;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.ceid.util.PositiveInteger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class EndRide extends AppCompatActivity implements postInterface {


    private Bundle bundle;
    private double time;
    private User user;
    protected byte[] bArray1,bArray2,bArray3,bArray4;
    private RentalService service;
    private TextView duration,cost,points;
    private CheckBox check1,check2,check3,check4;
    private Bitmap bitmap;
    private List<Map<String, Object>> values = new ArrayList<>();
    private Map<String, Object> sides=new LinkedHashMap<>();
    private boolean checked1,checked2,checked3,checked4;

    private  double pValue;
    private Button photoButton1,photoButton2,photoButton3,photoButton4;
    /*private GasStation gas=new GasStation(100,new Coordinates(38.256422,21.743256),1.80);
    private Refill refill=new Refill(LocalDateTime.now(),gas,new PositiveInteger(50),new PositiveInteger(60));
    private CityCar rental=new CityCar("abc",true,1,
            "MONDEO","FORD","2002",4.9,
            new Coordinates(38.256422,21.743256),new PositiveInteger(50));

    private RentalService service=new RentalService(1, LocalDateTime.now(),
            new Payment(WALLET), null, 100,rental);*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_ride_screen);
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
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //do nothing stay on the same screen
            }
        });




        bundle= getIntent().getExtras();
        time=getIntent().getDoubleExtra("time",0);
        String timeString=getIntent().getStringExtra("timestring");

        service= (RentalService) bundle.getSerializable("service");
        duration=findViewById(R.id.time);
        cost=findViewById(R.id.cost);

        points=findViewById(R.id.points);
        duration.setText(timeString);

        points.setText(String.valueOf(service.getPoints()));
        Rental rental=(Rental) service.getTransport();
        pValue=rental.getRate()*time;
        cost.setText(String.format("%.02f€",pValue));
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
    public void attach1(View view)
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



    public void saveImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                Log.e("PhotoPicker", "InputStream is null for URI: " + uri);
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos); //if the image is too big,it is compressed automatically

            if(checked1){
                checked1=false;
                check1.setChecked(true);
                bArray1 = bos.toByteArray();
                Log.d("test",Arrays.toString(bArray1));
            }
            if(checked2){
                checked2=false;
                check2.setChecked(true);
                bArray2 = bos.toByteArray();
            }

            if(checked3){
                checked3=false;
                check3.setChecked(true);
                bArray3 = bos.toByteArray();
            }
            if(checked4){
                checked4=false;
                check4.setChecked(true);
                bArray4 = bos.toByteArray();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PhotoPicker", "Error saving image", e);
        }
    }
    public void sendPhotos(View view)
    {
        if(check1.isChecked()&&check2.isChecked()&&check3.isChecked()&&check4.isChecked()){
            PostHelper end=new PostHelper(this);
            ApiService api= ApiClient.getApiService();
            //service.setRefill(refill);
            user=User.getCurrentUser();

            sides.put("points",service.getPoints());
            sides.put("userName",user.getUsername());
            sides.put("pValue",pValue);
            sides.put("pMethod","WALLET");
            sides.put("rentalId",service.getId());
            sides.put("stationId",service.getRefill().getGasStation().getid());
            sides.put("initGas",service.getRefill().getStartGas().getValue());
            int diff=service.getRefill().getEndGas().posDiff(service.getRefill().getStartGas());
            sides.put("addedGas",diff);
            sides.put("success",service.getRefill().getSuccess());
            sides.put("refillDate",service.getRefill().getDate());
            Log.d("testinside",Arrays.toString(bArray1));
            sides.put("leftimg",Arrays.toString(bArray3));
            sides.put("rightimg",Arrays.toString(bArray4));
            sides.put("frontimg",Arrays.toString(bArray1));
            sides.put("backimg",Arrays.toString(bArray2));

            values.add(sides);
            String jsonString = jsonStringParser.createJsonString("insertFinalRentalService", values);
            end.finalRental(api,jsonString);
        }
        else{
            Toast.makeText(getApplicationContext(), "Send all photos first!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException {
            Customer customer=(Customer)user;
            customer.getWallet().withdraw(pValue);
            customer.getPoints().addPoints(service.getPoints());

            Intent intent=new Intent(getApplicationContext(),MainScreen.class);
            startActivity(intent);
            finish();
    }

    @Override
    public void onResponseFailure(Throwable t) {
            //do nothing!!
    }
}


