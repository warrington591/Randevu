package xyz.warringtons.daterandevu.Fragments;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Adapters.LocationAdapter;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.MainActivity;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.Modules.Weather;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.Utility.WeatherHttpClient;
import xyz.warringtons.daterandevu.database.Category;

/**
 * Created by Warrington on 8/12/17.
 */

public class LocationFragment extends BaseFragment {

    String apiKey = "dcd8bfd8f86e3c64655c7a1059efbc0c";

    @BindView(R.id.locationRecyclerView)
    RecyclerView locationRV;


    List<String> locationTypes = new ArrayList<>();

    private MainActivity mainActivity;
    private Handler mainHandler;
    private Weather weather;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.start_dating, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        mainActivity = (MainActivity) getActivity();
        mainActivity.drawableDisable();
        locationTypes.clear();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Randevu.getContext());
        locationRV.setLayoutManager(layoutManager);


        locationTypes.add("New York");
        locationTypes.add("Florida");
        locationTypes.add("Los Angeles");



        LocationAdapter locationAdapter = new LocationAdapter(locationTypes, new ActivityCallBack() {
            @Override
            public void perform(String value) {

                //Sets location in phone storage
                Randevu.getEditor().putString("location", value);
                Randevu.getEditor().apply();


                WeatherHttpClient.makeRequest(value);

                weather = Randevu.getDaoSession().getWeatherDao().load((long) 1);

                //Changes to main fragment to choose the users categories
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                CategoryFragment fragment = new CategoryFragment();
                ft.replace(R.id.mainContent,  fragment);
                ft.addToBackStack("CategoryFragment");
                ft.commit();

                Log.d("calledTag", "ft has been called: ");

                mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if(firebaseUser!=null){
                            String userId = firebaseUser.getUid();
                            DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
                            DatabaseReference conditionRef = databaseForUser.child("condition");
                            conditionRef.setValue(weather);
                        }
                    }
                }, 4000);

            }

            @Override
            public void perform(String value, boolean status) {

            }

            @Override
            public void perform(Category value, boolean status) {

            }


            @Override
            public void perform(Activities activities, String value, boolean status) {

            }
        });

        locationRV.setAdapter(locationAdapter);
    }

}
