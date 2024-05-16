package com.ceid.Network;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Objects;

public class jsonStringParser {


    public static JsonArray extractResult(String jsonString) {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        // If the object contains the "result" key
        if (jsonObject.has("result")) {
            return jsonObject.getAsJsonArray("result");
        }

        return null;
    }


    public static void printJsonArray(JsonArray jsonArray) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(jsonArray);
        Log.d("JsonArray", jsonString);
    }

    public static <T> ArrayList<T> jsonArrayToArrayList(JsonArray jsonArray,Class<T> objectClass){
        ArrayList<T> array = new ArrayList<>();
        Gson gson= new Gson();

        for(JsonElement element : jsonArray){
            T object = gson.fromJson(element,objectClass);
            array.add(object);
        }

        return array;
    }

    public static <T> T jsonArraytoObj(JsonArray jsonArray, Class<T> objectClass) {
            ArrayList<T> array = jsonArrayToArrayList(jsonArray, objectClass);
        return array.get(0);
        }
    
}
