package xyz.warringtons.daterandevu.Utility;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xyz.warringtons.daterandevu.Interfaces.YelpCallback;
import xyz.warringtons.daterandevu.Modules.YelpBusinesses;
import xyz.warringtons.daterandevu.Randevu;

/**
 * Created by Warrington on 2/19/18.
 */

public class YelpClient {

    private static String apiKey = "0I50tEjywhdKAs2DYLb8QBLvD9Esp6MV7ZpQw1EfkMZLbuH6F4f9ZGdUNwzGNA03ax0VSqTLZhPaLApTefrH9ytPEm3iUejycSnQVafSwxMxnZ4y5lceG3QCce6KWnYx";
    private static String BASE_URL = "https://api.yelp.com/v3/";
    private static String name;
    private static String image_url;
    private static boolean is_closed;
    private static String url;
    private static String rate;
    private static String price;
    public static YelpCallback yelpCallback;


    public static void makeRequest(String keyword, final YelpCallback yelpCallback){
        String longitude="40.8656555";
        String latitude="-73.9268035";

        String url = BASE_URL+"businesses/search?term="+keyword+"&latitude="+longitude+"&longitude="+latitude;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer "+ apiKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TestingTag", "onResponse: ");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TestingTag", "onResponse: ");

                final String responseData = response.body().string();

                Randevu.getDaoSession().getYelpBusinessesDao().deleteAll();
                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONArray temp = json.getJSONArray("businesses");
                    int length = temp.length();
                    //reduce list to only 5
                    if(length>5){
                        length=5;
                    }
                    for(int i=0; i<length; i++){
                        Log.d("Testing", "onResponse: ");
                        store(temp.getJSONObject(i));
                    }

                    yelpCallback.hasReturned();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void store(JSONObject jsonObject) {
        Log.d("TestingTag", "onResponse: ");
        try {
             name = jsonObject.getString("name");
             image_url = jsonObject.getString("image_url");
             is_closed = jsonObject.getBoolean("is_closed");
             url = jsonObject.getString("url");
             rate = jsonObject.getString("rating");
             price = jsonObject.getString("price");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        YelpBusinesses yepBusi = new YelpBusinesses();
        yepBusi.setName(name);
        yepBusi.setImage_url(image_url);
        yepBusi.setIs_closed(is_closed);
        yepBusi.setUrl(url);
        yepBusi.setRate(rate);
        yepBusi.setPrice(price);
        Randevu.getDaoSession().getYelpBusinessesDao().insert(yepBusi);
    }
}
