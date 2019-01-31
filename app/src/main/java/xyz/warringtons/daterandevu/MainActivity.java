package xyz.warringtons.daterandevu;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


//import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.auth.ErrorCodes;
//import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Fragments.CategoryFragment;
import xyz.warringtons.daterandevu.Fragments.InitialScreen;
import xyz.warringtons.daterandevu.Fragments.LocationFragment;
import xyz.warringtons.daterandevu.Fragments.ProfileFragment;
import xyz.warringtons.daterandevu.Fragments.SelectedIdeasFragment;
import xyz.warringtons.daterandevu.Fragments.WelcomeFragment;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.Modules.ActivitiesDao;
import xyz.warringtons.daterandevu.Modules.User;
import xyz.warringtons.daterandevu.Modules.UserDao;
import xyz.warringtons.daterandevu.database.Category;
import xyz.warringtons.daterandevu.database.RandevuUser;


public class MainActivity extends AppCompatActivity {



    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;

    private static final String TAG ="MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    private ActivitiesDao activitiesDao;
    private Toolbar toolbar;
    public ActionBar mActionBar;
    private FragmentTransaction fragmentTransaction;
    private LocationFragment locationFragment;
    private CategoryFragment fragment;
    private User currentUser;
    private UserDao userDao;
    public int PERMISSION_ACCESS_COARSE_LOCATION = 123;

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mDatabase.child("condition");
    DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseActivities;
    private InitialScreen initialFragment;

//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.EmailBuilder().build(),
//            new AuthUI.IdpConfig.GoogleBuilder().build());

    private DatabaseReference categoriesRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);

        ButterKnife.bind(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        String s = getIntent().getStringExtra("navigateTo");
        if (s != null && s.equals("categoryScreen")) {
            navigateToSelectionScreen();
            return;
        } else if (s != null && s.equals("profileScreen")) {
            navigateToProfileScreen();
            return;
        }
        activitiesDao = Randevu.getDaoSession().getActivitiesDao();
        userDao = Randevu.getDaoSession().getUserDao();
        currentUser = Randevu.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.Id.eq(1)).build().unique();

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            moveToLocationsFragment();
        } else {

            navigateToWelcome();

//            signIn();
        }

    }

    public boolean isServicesOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //user can make map requests
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "Make requests not allowed for you", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void navigateToWelcome() {
        WelcomeFragment fragment = new WelcomeFragment();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.commit();
    }

    private void navigateToSelectionScreen() {
        CategoryFragment fragment = new CategoryFragment();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.commit();
    }

    private void navigateToProfileScreen() {
        ProfileFragment fragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.mainContent, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        conditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signIn() {


        // Create and launch sign-in intent
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            // Successfully signed in
//            if (resultCode == RESULT_OK) {
//
//                if(auth== null){
//                    auth = FirebaseAuth.getInstance();
//                }
//
//                if(auth!=null){
//                    FirebaseUser firebaseUser = auth.getCurrentUser();
//                    String username = firebaseUser.getDisplayName();
//                    final String userId = firebaseUser.getUid();
//                    String email = firebaseUser.getEmail();
//                    final RandevuUser randevuUser = new RandevuUser(username, email);
//
//                    DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users");
//
//                    //Checks if account already exists
//                    databaseForUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot snapshot) {
//                            if(snapshot.exists()){
//                                Log.d("AlreadyExists", "onDataChange: ");
//                            }else{
//                                //creates an instance of the new user
//                                mDatabaseUsers.child(userId).setValue(randevuUser);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    categoriesRef = databaseForUser.child(userId).child("categories");
//                    mDatabaseActivities = mDatabaseUsers.child(userId).child("activities");
//                    mDatabaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.child("activities").exists()) {
//                                Log.d("calledTag", "activities child exits");
//                            }else {
//                                Log.d("calledTag", "activities doesn't exits");
//                                setUpActivities();
//                                categoriesRef.setValue("categories");
//                                categoriesRef.push().setValue(new Category("Casual", 0));
//                                categoriesRef.push().setValue(new Category("Adventure", 0));
//                                categoriesRef.push().setValue(new Category("Home", 0));
//                                categoriesRef.push().setValue(new Category("Crafts", 0));
//                                categoriesRef.push().setValue(new Category("Fancy", 0));
//                                categoriesRef.push().setValue(new Category("Volunteering", 0));
//                                Randevu.getEditor().putString("location","");
//                                Randevu.getEditor().putBoolean("Casual",false);
//                                Randevu.getEditor().putBoolean("Adventure", false);
//                                Randevu.getEditor().putBoolean("Home", false);
//                                Randevu.getEditor().putBoolean("Crafts", false);
//                                Randevu.getEditor().putBoolean("Fancy", false);
//                                Randevu.getEditor().putBoolean("Volunteering", false);
//                                Randevu.getEditor().apply();
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//                    moveToLocationsFragment();
//                }
//
//                return;
//            } else {
//                // Sign in failed
//                if (response == null) {
//                    // RandevuUser pressed back button
//                    Toast.makeText(Randevu.getContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
////                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
////                    Toast.makeText(Randevu.getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
////                    return;
////                }
////
////                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
////                    Toast.makeText(Randevu.getContext(), "Unknown error", Toast.LENGTH_SHORT).show();
////                    return;
////                }
//            }
//        }
//    }


    private void moveToLocationsFragment() {
        locationFragment = new LocationFragment();
        try {
            fragmentTransaction.replace(R.id.mainContent, locationFragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
            Log.d("Testing fragment", "moveToLocationsFragment: ");

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }


    private void moveToSignUpScreen() {
        initialFragment = new InitialScreen();
        try {
            fragmentTransaction.replace(R.id.mainContent, initialFragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
            Log.d("Testing fragment", "move to initial screen ");
        }
    }

    private void moveToSelectedIdeasFragment(){
        SelectedIdeasFragment selectedIdeasFragment = new SelectedIdeasFragment();
        try{

            fragmentTransaction.replace(R.id.mainContent,  selectedIdeasFragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
            Log.d("Testing fragment", "moveToSelectedIdeasFragment: ");
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpActivities() {
        Activities activitie1 = new Activities();
        Activities activitie2 = new Activities();
        Activities activitie3 = new Activities();
        Activities activitie4 = new Activities();
        Activities activitie5 = new Activities();
        Activities activitie6 = new Activities();
        Activities activitie7 = new Activities();
        Activities activitie8 = new Activities();
        Activities activitie9 = new Activities();
        Activities activitie10 = new Activities();


        activitie1.setCategoryId("Casual");
        activitie1.setActivityName("Bicycle Riding In A Park");
        activitie1.setWeatherCondition("Sunny");
        activitie1.setSelected(false);
        activitie1.setDeleted(false);
        activitie1.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/bike_riding.jpg?alt=media&token=612c910d-c8e8-4bc1-8f5d-20e1bf902192");
        mDatabaseActivities.push().setValue(activitie1);

        activitie2.setCategoryId("Casual");
        activitie2.setActivityName("Read books at Barnes & Noble");
        activitie2.setWeatherCondition("Raining");
        activitie2.setDeleted(false);
        activitie2.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/book_reading.jpg?alt=media&token=f2d4f87e-8e36-47f7-a47d-b19199c59a95");
        activitie2.setYelpKeyword("barnesandnoble");
        activitie2.setSelected(false);
        mDatabaseActivities.push().setValue(activitie2);


        activitie3.setCategoryId("Casual");
        activitie3.setActivityName("Trip To Coney Island");
        activitie3.setWeatherCondition("Sunny");
        activitie3.setDeleted(false);
        activitie3.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/coney_island.jpg?alt=media&token=61a87106-72b8-47be-a446-28e39e8973f1");
        activitie3.setSelected(false);
        mDatabaseActivities.push().setValue(activitie3);


        activitie4.setCategoryId("Adventure");
        activitie4.setActivityName("Indoor Rock Climbing");
        activitie4.setWeatherCondition("Raining");
        activitie4.setDeleted(false);
        activitie4.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/rock_climb.jpg?alt=media&token=5e5ca463-5f4f-443e-a482-8ad3a1708d74");
        activitie4.setSelected(false);
        activitie4.setYelpKeyword("Indoor+Rock+Climbing");
        mDatabaseActivities.push().setValue(activitie4);

        activitie5.setCategoryId("Adventure");
        activitie5.setActivityName("Kayaking");
        activitie5.setWeatherCondition("Sunny");
        activitie5.setDeleted(false);
        activitie5.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/kayak.jpg?alt=media&token=7959d3a1-7019-4956-90fa-ec17e1866bd8");
        activitie5.setSelected(false);
        activitie5.setYelpKeyword("yelpKeyword");
        mDatabaseActivities.push().setValue(activitie5);

        activitie6.setCategoryId("Adventure");
        activitie6.setActivityName("Go Cart Racing");
        activitie6.setWeatherCondition("Sunny");
        activitie6.setDeleted(false);
        activitie6.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/gocart.jpg?alt=media&token=39c07fcf-ea30-4b60-a791-4423db0c0933");
        activitie6.setSelected(false);
        activitie6.setYelpKeyword("go+karts");
        mDatabaseActivities.push().setValue(activitie6);

        activitie7.setCategoryId("Home");
        activitie7.setActivityName("Turn Apartment Into A Club");
        activitie7.setWeatherCondition("Raining");
        activitie7.setDeleted(false);
        activitie7.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/aprt_party.jpg?alt=media&token=edabca3e-d2bd-4387-b7fd-a441f9fb89a2");
        activitie7.setSelected(false);
        mDatabaseActivities.push().setValue(activitie7);


        activitie8.setCategoryId("Crafts");
        activitie8.setActivityName("Do a DIY project together");
        activitie8.setWeatherCondition("Raining");
        activitie8.setDeleted(false);
        activitie8.setSelected(false);
        mDatabaseActivities.push().setValue(activitie8);


        activitie9.setCategoryId("Volunteering");
        activitie9.setActivityName("Feed the needy");
        activitie9.setWeatherCondition("Cloudy");
        activitie9.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/feed_v.png?alt=media&token=2ee309bc-d4d2-480f-bfcc-ece945f6a331");
        activitie9.setDeleted(false);
        activitie9.setSelected(false);
        mDatabaseActivities.push().setValue(activitie9);


        activitie10.setCategoryId("Fancy");
        activitie10.setActivityName("Wine Tasting");
        activitie10.setWeatherCondition("Raining");
        activitie10.setDeleted(false);
        activitie10.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/wine_tasting.jpg?alt=media&token=81979fe0-4b15-462d-9867-bed76b93c7b6");
        activitie10.setSelected(false);
        mDatabaseActivities.push().setValue(activitie10);



        //New York Based Activities
        Activities activitie11 = new Activities();
        activitie11.setCategoryId("Casual");
        activitie11.setActivityName("Jane's Carousel & Gran Electrica");
        activitie11.setWeatherCondition("Sunny");
        activitie11.setDeleted(false);
        activitie11.setSelected(false);
        activitie11.setActivityName("Jane's Carousel & Gran Electrica");
        activitie11.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/janes_caro.jpg?alt=media&token=00c7a762-240e-4b30-9689-316b8771b46c");
        activitie11.setLocation("New York");
        activitie11.setYelpKeyword("Jane's+Carousel+&+Gran+Electrica");
        mDatabaseActivities.push().setValue(activitie11);


        Activities activitie12 = new Activities();
        activitie12.setCategoryId("Fancy");
        activitie12.setActivityName("See A Broadway Show");
        activitie12.setWeatherCondition("Rainy");
        activitie12.setDeleted(false);
        activitie12.setSelected(false);
        activitie12.setPicDatabaseId("https://firebasestorage.googleapis.com/v0/b/randevu-a9421.appspot.com/o/broadway_show.jpg?alt=media&token=fd480908-cb4e-4efc-a7f1-1ebda997d88e");
        activitie12.setLocation("New York");
        mDatabaseActivities.push().setValue(activitie12);


    }

    private void setUser(UserDao userDao) {
        User user = new User();
        user.setId((long) 1);
        user.setName("Warrington");
        userDao.insertOrReplace(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent = getIntent();


        switch (item.getItemId()) {
            case R.id.action_categories:
                intent.putExtra("navigateTo", "categoryScreen");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                break;

            case R.id.action_profile:
                intent.putExtra("navigateTo", "profileScreen");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
                break;

            case R.id.action_reset:
                Randevu.getDaoSession().getUserDao().deleteAll();
                Randevu.getEditor().putString("location","");
                Randevu.getEditor().apply();
                intent.putExtra("navigateTo", "locationsScreen");
                finish();
                startActivity(intent);
                break;

            case R.id.action_sign_out:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(intent);
//                AuthUI.getInstance()
//                        .signOut(this)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            public void onComplete(@NonNull Task<Void> task) {
//                                Intent intent = getIntent();
//                                finish();
//                                startActivity(intent);
//                            }
//                        });

            case android.R.id.home:
                onBackPressed();
                break;

        }

        return true;
    }


    public void drawableEnable() {
        if(toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        }
    }

    public void drawableDisable() {
        if(toolbar != null){
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_home));
        }
    }


}
