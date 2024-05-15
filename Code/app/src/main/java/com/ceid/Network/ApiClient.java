package com.ceid.Network;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

    public class ApiClient {

        private static final String BASE_URL = "http://192.168.35.170:3000"; // Replace with your server's IP address

        private static Retrofit retrofit = null;

        private static Retrofit getRetrofit() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

        public static ApiService getApiService() {
            return getRetrofit().create(ApiService.class);
        }
    }

