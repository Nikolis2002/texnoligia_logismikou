package com.ceid.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class LicenseScreen extends AppCompatActivity implements postInterface {
    private ImageView image;
    protected byte[] bArray;
    private User user;
    private Customer customer;
    private TextView text;

    private long fileSize;
    private String fileExtension = null; //Use this for both file type check AND empty field check

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        user= User.getCurrentUser();

        setContentView(R.layout.license_screen);
        image= findViewById(R.id.imageView2);
        text=findViewById(R.id.textView5);

        if (((Customer)user).hasLicense())
            showImage(((Customer)user).getLicenseImg());
    }

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                // Callback is invoked after the user selects a media item or closes the
                // photo picker.
                if (uri != null)
                {
                    fileSize = getFileSize(uri);
                    fileExtension = getFileExtension(uri);

                    Log.d("FILETEST", String.format("Size: %.02f MB Extension: %s", fileSize/1048576.0f, fileExtension));

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

    public void showImage(byte[] img)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        image.setImageBitmap(bitmap);
        text.setVisibility(View.GONE);
    }

    public void attachPhoto(View view)
    {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    public void onSubmit(View view)
    {
        //Check if user has uploaded file
        if (fileExtension == null)
        {
            //mandatoryFieldMsg
            Toast.makeText(getApplicationContext(), "Please select an image",
                    Toast.LENGTH_LONG).show();

            return;
        }

        //Check file type
        if (!isImageExtension(fileExtension))
        {
            //fileExtensionMsg
            Toast.makeText(getApplicationContext(), "Invalid file type",
                    Toast.LENGTH_LONG).show();

            return;
        }

        //Check file size (10 MB)
        if (fileSize > 10*1048576)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Image too big");
            builder.setMessage(String.format("Image is too big, must be at most 10 MB (size was %.02f MB)", fileSize/1048576.0f));
            builder.setCancelable(false);

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });

            AlertDialog imageSizeErrorMsg = builder.create();
            imageSizeErrorMsg.setCanceledOnTouchOutside(false);
            imageSizeErrorMsg.show();

            return;
        }

        Log.d("byte", Arrays.toString((bArray)));

        customer= (Customer) user;

        //Add license to customer profile
        customer.setLicenseImg(bArray);

        //Save to database
        //======================================================================================

        PostHelper lic=new PostHelper(this);
        ApiService api=ApiClient.getApiService();

        List<Map<String, Object>> values = new ArrayList<>();
        Map<String, Object> license=new LinkedHashMap<>();

        license.put("username",customer.getUsername());
        license.put("license",Arrays.toString((bArray)));

        values.add(license);

        String jsonString = jsonStringParser.createJsonString("insertLicense", values);
        lic.licenseCall(api,jsonString);
    }

    @Override
    public void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException
    {
        View popupView = LayoutInflater.from(this).inflate(R.layout.driverslicensepopup, null);

        // Create the popup window
        PopupWindow infoPopup = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        // Show the popup window
        infoPopup.showAtLocation(
                findViewById(android.R.id.content),
                Gravity.CENTER,
                0,
                0
        );
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }
    @Override
    public void onResponseFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), "Check test!",
                Toast.LENGTH_LONG).show();
    }

    //Get file size
    //=========================================================================================
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


    //Get file extension
    //=========================================================================================

    private String getFileExtension(Uri uri)
    {
        String extension = null;
        String mimeType = getContentResolver().getType(uri);

        if (mimeType != null)
        {
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        }
        else
        {
            //Fallback to using the file name if MIME type is not available
            String path = getPathFromUri(uri);

            if (path != null)
            {
                extension = MimeTypeMap.getFileExtensionFromUrl(path);
            }
        }

        return extension;
    }

    // Helper method to get the file path from URI
    private String getPathFromUri(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null)
        {
            cursor.moveToFirst();
            String documentId = cursor.getString(0);
            documentId = documentId.substring(documentId.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{documentId}, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();

                return path;
            }
        }

        return null;
    }

    private boolean isImageExtension(String extension)
    {
        String[] validExtensions = {"jpg", "jpeg", "png"};
        for (String validExtension : validExtensions)
        {
            if (extension.equalsIgnoreCase(validExtension))
            {
                return true;
            }
        }

        return false;
    }
}

