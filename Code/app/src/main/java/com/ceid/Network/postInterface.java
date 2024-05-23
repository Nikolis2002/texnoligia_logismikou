package com.ceid.Network;

import androidx.annotation.NonNull;

import com.ceid.model.transport.Garage;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public interface postInterface {
    void onResponseSuccess(@NonNull Response<ResponseBody> response) throws IOException;
    void onResponseFailure(Throwable t);
}
