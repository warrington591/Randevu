package xyz.warringtons.daterandevu.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Adapters.ActivitiesAdapter;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.database.Category;
import xyz.warringtons.daterandevu.database.RandevuUser;

/**
 * Created by Warrington on 8/12/17.
 */

public class CategoryFragment extends BaseFragment {

    @BindView(R.id.activitesRV)
    RecyclerView activitesRV;

    @BindView(R.id.beginDating)
    Button beginDating;

    @BindView(R.id.categoryProgressBar)
    ProgressBar progressBar;

    List<String> activityTypes = new ArrayList<>();
    List<Category> categoriesListed;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activites_selection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        activityTypes.clear();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Randevu.getContext(),2);
        activitesRV.setLayoutManager(layoutManager);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser!=null){
            final String userId = firebaseUser.getUid();
            DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users");
            final DatabaseReference categoriesRef = databaseForUser.child(userId).child("categories");
            databaseForUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot.child("categories").exists()){
                        Log.d("AlreadyExists", "onDataChange: ");
                    }else{

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("Now", "onCancelled: ");
                }
            });


            categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    categoriesListed = new ArrayList<>();

                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        Category currentCategory = postSnapshot.getValue(Category.class);
                        currentCategory.setKey(postSnapshot.getKey());
                        categoriesListed.add(currentCategory);
                    }

                    createAdapter(categoriesRef);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            if(categoriesListed!=null){
                createAdapter(categoriesRef);
            }


            beginDating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Boolean found = false;
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                int foundValue = ds.child("value").getValue(Integer.class);

                                if(foundValue==1){
                                    found = true;
                                    break;
                                }
                            }

                            if(found){
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                DateGeneratorFragment fragment = new DateGeneratorFragment();
                                ft.replace(R.id.mainContent,  fragment);
                                ft.addToBackStack("DateGeneratorFragment");
                                ft.commit();
                            }else{
                                Toast.makeText(Randevu.getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };


                    categoriesRef.addListenerForSingleValueEvent(eventListener);
                }
            });
        }

    }

    private void createAdapter(final DatabaseReference categoriesRef) {
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(categoriesListed, new ActivityCallBack() {
            @Override
            public void perform(String value) {

            }

            @Override
            public void perform(String value, boolean status) {

            }

            @Override
            public void perform(Category category, boolean status) {
                String key = category.getKey();
                setCategorySelection(category, status);
                categoriesRef.child(key).setValue(category);
                String name = category.getName();

                //Todo: Remove in favor of using firebase for categories
                //add to shared preferences
                switch (name){
                    case "Casual":
                        Randevu.getEditor().putBoolean("Casual", status);
                        break;
                    case "Adventure":
                        Randevu.getEditor().putBoolean("Adventure", status);
                        break;
                    case "Home":
                        Randevu.getEditor().putBoolean("Home", status);
                        break;
                    case "Crafts":
                        Randevu.getEditor().putBoolean("Crafts", status);
                        break;
                    case "Fancy":
                        Randevu.getEditor().putBoolean("Fancy", status);
                        break;
                    case "Volunteering":
                        Randevu.getEditor().putBoolean("Volunteering", status);
                        break;
                }
                Randevu.getEditor().apply();
            }

            @Override
            public void perform(Activities activities, String value, boolean status) {

            }
        });
        activitesRV.setAdapter(activitiesAdapter);
        progressBar.setVisibility(View.GONE);
    }

    private void setCategorySelection(Category category, boolean status) {
        if(status){
            category.setValue(1);
        }else{
            category.setValue(0);
        }
    }


}
