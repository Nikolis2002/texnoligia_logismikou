package com.ceid.Network;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ceid.model.payment_methods.Card;
import com.ceid.model.payment_methods.Wallet;
import com.ceid.model.transport.Taxi;
import com.ceid.model.users.Customer;
import com.ceid.model.users.TaxiDriver;
import com.ceid.model.users.User;
import com.ceid.util.Coordinates;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Response;

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
            JsonNode taxiNode = data.get(0).get(0);
            Coordinates coords;

            Wallet wallet = new Wallet(taxiNode.get("wallet_balance").asDouble());

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

            Taxi taxi = new Taxi(
                    taxiNode.get("taxi_id").asInt(),
                    taxiNode.get("taxi_model").asText(),
                    taxiNode.get("taxi_year").asText(),
                    taxiNode.get("manuf").asText(),
                    taxiNode.get("licensePlate").asText(),
                    coords= new Coordinates(taxiNode.get("lat").asDouble(),taxiNode.get("lng").asDouble())
            );


            return new TaxiDriver(
                    taxiNode.get("username").asText(),
                    taxiNode.get("password").asText(),
                    taxiNode.get("name").asText(),
                    taxiNode.get("lname").asText(),
                    taxiNode.get("email").asText(),
                    wallet,
                    Boolean.parseBoolean(taxiNode.get("taxi_status").asText()),
                    taxi
            );

        }

    }

    public static boolean getbooleanFromJson(@NonNull Response<ResponseBody> response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body().string());
        String booleanString=jsonNode.get(0).get(0).get("result").asText();

        return Boolean.parseBoolean(booleanString);
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


    public static String createJsonString(String tableName, List<Map<String, Object>> values) {
        ObjectMapper mapper = new ObjectMapper();

        // Data to insert
        ArrayNode dataArray = mapper.createArrayNode();
        for (Map<String, Object> value : values) {
            ObjectNode jsonObject = mapper.createObjectNode();
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();

                // Explicitly cast values to their corresponding types
                if (val instanceof String) {
                    jsonObject.put(key, (String) val);
                } else if (val instanceof Integer) {
                    jsonObject.put(key, (Integer) val);
                } else if (val instanceof Long) {
                    jsonObject.put(key, (Long) val);
                } else if (val instanceof Double) {
                    jsonObject.put(key, (Double) val);
                } else if (val instanceof Boolean) {
                    jsonObject.put(key, (Boolean) val);

                }
                else if (val == null) {
                    // Handle null values
                    jsonObject.putNull(key);}
                else {
                    // Handle other types or convert to string
                    jsonObject.put(key, val.toString());
                }
            }
            dataArray.add(jsonObject);
        }

        // Create JSON object
        ObjectNode jsonObject = mapper.createObjectNode();
        jsonObject.put("table", tableName);
        jsonObject.set("values", dataArray);

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] extractInsertIds(Response<ResponseBody> response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body().string());
            JsonNode insertIdsNode = rootNode.get("insertIds");

            if (insertIdsNode != null && insertIdsNode.isArray() && insertIdsNode.size() > 0) {
                int[] insertIds = new int[insertIdsNode.size()];
                for (int i = 0; i < insertIdsNode.size(); i++) {
                    insertIds[i] = insertIdsNode.get(i).asInt();
                }
                return insertIds;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return an empty array if insertIds are not found or if there's an error
        return new int[0];
    }

}
