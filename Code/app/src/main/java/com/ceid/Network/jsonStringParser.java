package com.ceid.Network;
import android.util.Log;

import com.ceid.model.payment_methods.Card;
import com.ceid.model.payment_methods.Wallet;
import com.ceid.model.users.Customer;
import com.ceid.model.users.User;
import com.ceid.util.DateFormat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
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
    public static User parseJson(JsonNode data) throws IOException {
        User user=null;
        JsonNode userNode = data.get(0).get(0);
        String checker=userNode.get("type").asText();

        if(checker.equals("customer")) {

            Wallet wallet = new Wallet(userNode.get("wallet_balance").asDouble());

            // Extract card information
            JsonNode cardsNode = data.get(1);
            for (JsonNode card : cardsNode) {
                Card newCard = new Card(
                        card.get("card_number").asText(),
                        card.get("card_holder").asText(),
                        card.get("expiration_date").asText(),
                        card.get("cvv").asText(),
                        card.get("card_type").asText()
                );
                wallet.addCard(newCard); // Assuming you have a method to add a card to the wallet
            }

            // Extract customer information
            byte[] img = userNode.get("img").binaryValue(); // Get binary data directly

            return new Customer(
                    userNode.get("cus_username").asText(),
                    userNode.get("cus_password").asText(),
                    userNode.get("cus_name").asText(),
                    userNode.get("cus_lname").asText(),
                    userNode.get("email").asText(),
                    img,
                    wallet,
                    userNode.get("cus_licence").asText(),

                    userNode.get("cus_points").asInt()
            );
        }
        else{
            JsonNode taxiNode = data.get(1);


        }

        return  user;
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
