package xyz.warringtons.daterandevu.Utility;

import android.util.Log;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.warringtons.daterandevu.Modules.Weather;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;

/**
 * Created by Warrington on 8/12/17.
 */

public class WeatherHttpClient {

    private static String apiKey = "dcd8bfd8f86e3c64655c7a1059efbc0c";
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static String mainWeather;
    private static String description;
    private static int correctTemp;
    private static String icon;
    //http://api.openweathermap.org/data/2.5/weather?zip=10034,us&appid=dcd8bfd8f86e3c64655c7a1059efbc0c

    public static void makeRequest(String location){
        String zipcode ="zip=";

        switch (location){
            case "New York":
                zipcode+="10034";
                break;
            case "Florida":
                zipcode+="10921";
                break;
            case "Los Angeles":
                zipcode+="90009";
                break;
        }

        String url = BASE_URL+zipcode+",us&appid="+apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TestingTag", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TestingTag", "onResponse: ");
                final String responseData = response.body().string();
                try {
                    JSONObject json = new JSONObject(responseData);
                    Log.d("TestingTag", "onResponse: ");

                    store(json);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("TestingTag", "onResponse: ");

            }
        });
    }

    private static void store(JSONObject json) {

        try {
            JSONArray jsonArray = json.getJSONArray("weather");
            JSONObject jsonObject =jsonArray.getJSONObject(0);
             mainWeather = jsonObject.getString("main");
             description = jsonObject.getString("description");
             icon = jsonObject.getString("icon");
            Log.d("IconStored", "store: "+ icon);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject tempObject = json.getJSONObject("main");
            int temperature = tempObject.getInt("temp");
            correctTemp = 9/5 * (temperature - 273) + 32; //change from K to F
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Weather weather = new Weather();
        weather.setId((long) 1);
        weather.setWeather(mainWeather);
        weather.setDescription(description);
        weather.setTemperature(String.valueOf(correctTemp));
        weather.setWeatherIcon(icon);
        Randevu.getDaoSession().getWeatherDao().insertOrReplace(weather);

    }
}
