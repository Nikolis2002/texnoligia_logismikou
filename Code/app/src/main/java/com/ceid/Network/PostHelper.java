package com.ceid.Network;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ceid.model.service.GasStation;
import com.ceid.model.transport.VehicleTracker;
import com.ceid.util.GenericCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostHelper {
    private postInterface callback;

    public PostHelper(postInterface callback) {
        this.callback = callback;
    }

    public void signUp(ApiService api, String userString) {

        Call<ResponseBody> call = api.callProcedure(userString);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    System.out.println("success message");
                } else {
                    System.out.println("Error message");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }

    public void login(ApiService api, String userParams) {
        Call<ResponseBody> call = api.checkUser(userParams);
        Log.d("kort", "kort was send successfully!!");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }

    public void card(ApiService api, String cardParams) {

        Call<ResponseBody> call = api.callProcedure(cardParams);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });

    }

    public static void getGasStations(ApiService api, String params, GenericCallback<ArrayList<GasStation>> callback) {
        Call<ResponseBody> call = api.getTableData(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        ArrayList<GasStation> array = null;
                        try {
                            array = jsonStringParser.parseGarage(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(array);
                    }
                } else {
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    //Communication with the tracker
    //======================================================================================
    public static void getTrackerOfRental(ApiService api, String params,String ten, GenericCallback<VehicleTracker> callback){
        Call<ResponseBody> call=api.getTracker(params,ten);
        call.enqueue(new Callback<ResponseBody>(){
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        VehicleTracker tracker = null;
                        try {
                            tracker = jsonStringParser.parseTracker(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        callback.onSuccess(tracker);
                    }
                } else {
                    callback.onFailure(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                callback.onFailure(new Exception(t));
            }
        });
    }

    public static void withdraw(ApiService api, String username, double amount)
    {
        List<Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> checkVehicle = new LinkedHashMap<>();

        checkVehicle.put("username", username);
        checkVehicle.put("amount", amount);

        values.add(checkVehicle);

        String jsonString = jsonStringParser.createJsonString("withdraw_from_wallet",values);

        Call<ResponseBody> call = api.callProcedure(jsonString);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response)
            {
                if (!response.isSuccessful())
                    throw new RuntimeException("Withdrawal error");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    public static void addToWallet(ApiService api, String username, double amount)
    {
        List<Map<String,Object>> values = new ArrayList<>();
        java.util.Map<String, Object> checkVehicle = new LinkedHashMap<>();

        checkVehicle.put("username", username);
        checkVehicle.put("amount", amount);

        values.add(checkVehicle);

        String jsonString = jsonStringParser.createJsonString("add_to_wallet",values);

        Call<ResponseBody> call = api.callProcedure(jsonString);
        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response)
            {
                if (!response.isSuccessful())
                    throw new RuntimeException("Wallet error");
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }

    public void charge(ApiService api, String value) {

        Call<ResponseBody> call = api.callProcedure(value);
        Log.d("kort", "kort was send successfully!!");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });


    }

    public void licenseCall(ApiService api, String license) {

        Call<ResponseBody> call = api.callProcedure(license);
        Log.d("kort", "kort was send successfully!!");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }
    public void finalRental(ApiService api,String params)
    {
        Call<ResponseBody> call = api.callProcedure(params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (callback != null) {
                        try {
                            callback.onResponseSuccess(response);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (callback != null)
                        callback.onResponseFailure(new Throwable("Request failed"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                System.out.println("Error message");
            }
        });
    }
}

