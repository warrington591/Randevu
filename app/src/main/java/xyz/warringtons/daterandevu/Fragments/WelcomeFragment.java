package xyz.warringtons.daterandevu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.firebase.ui.auth.AuthUI;
//import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.warringtons.daterandevu.MainActivity;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.database.Category;
import xyz.warringtons.daterandevu.database.RandevuUser;

import static android.app.Activity.RESULT_OK;

public class WelcomeFragment extends BaseFragment {

//    List<AuthUI.IdpConfig> providers = Arrays.asList(
//            new AuthUI.IdpConfig.EmailBuilder().build(),
//            new AuthUI.IdpConfig.GoogleBuilder().build());

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "WelcomeFragment";

    private FirebaseAuth auth;

    @BindView(R.id.emailET)
    EditText emailET;

    @BindView(R.id.passwordET)
    EditText passwordET;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private DatabaseReference categoriesRef;
    private DatabaseReference mDatabaseActivities;
    private FragmentTransaction fragmentTransaction;
    private LocationFragment locationFragment;
    private DatabaseReference specificUser;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        fragmentTransaction = getFragmentManager().beginTransaction();

    }

    @OnClick(R.id.logIn)
    public void LogIn() {



//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
//
//
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @OnClick(R.id.forgotPassword)
    public void forgotPassword() {
        Toast.makeText(Randevu.getContext(), "Forgot password works!", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.register)
    public void register(){

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Randevu.getContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Randevu.getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(Randevu.getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        //Creates a user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(Randevu.getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                } else {

                    if(auth == null){
                        auth = FirebaseAuth.getInstance();
                    }

                    if(auth!=null){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String username = firebaseUser.getDisplayName();
                        final String userId = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();
                        if(username== null){
                            String[] emailSplit = email.split("@");
                            username = emailSplit[0];
                        }
                        final RandevuUser randevuUser = new RandevuUser(username, email);

                        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
                        mDatabaseUsers.child(userId).setValue(randevuUser);
                        specificUser = mDatabaseUsers.child(userId);
                        specificUser.child("activities").setValue("0");
                        specificUser.child("categories").setValue("0");
                        mDatabaseActivities = specificUser.child("activities");
                        categoriesRef = specificUser.child("categories");
                        specificUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                setUpActivities();
                                categoriesRef.push().setValue(new Category("Casual", 0));
                                categoriesRef.push().setValue(new Category("Adventure", 0));
                                categoriesRef.push().setValue(new Category("Home", 0));
                                categoriesRef.push().setValue(new Category("Crafts", 0));
                                categoriesRef.push().setValue(new Category("Fancy", 0));
                                categoriesRef.push().setValue(new Category("Volunteering", 0));
                                saveToLocalStorage();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        moveToLocationsFragment();
                    }

                }
            }
        });

    }

    private void saveToLocalStorage() {
        Randevu.getEditor().putString("location","");
        Randevu.getEditor().putBoolean("Casual",false);
        Randevu.getEditor().putBoolean("Adventure", false);
        Randevu.getEditor().putBoolean("Home", false);
        Randevu.getEditor().putBoolean("Crafts", false);
        Randevu.getEditor().putBoolean("Fancy", false);
        Randevu.getEditor().putBoolean("Volunteering", false);
        Randevu.getEditor().apply();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

            // Successfully signed in
            if (resultCode == RESULT_OK) {


            }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            if (auth == null) {
                auth = FirebaseAuth.getInstance();
            }

            if (auth != null) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String username = account.getDisplayName();
//                String username = firebaseUser.getDisplayName();
                final String userId = account.getId();

//                final String userId = firebaseUser.getUid();
                String email = account.getEmail();
                final RandevuUser randevuUser = new RandevuUser(username, email);

                DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users");

                //Checks if account already exists
                databaseForUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.d("AlreadyExists", "onDataChange: ");
                        } else {
                            //creates an instance of the new user
                            mDatabaseUsers.child(userId).setValue(randevuUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                categoriesRef = databaseForUser.child(userId).child("categories");
                mDatabaseActivities = mDatabaseUsers.child(userId).child("activities");
                mDatabaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("activities").exists()) {
                            Log.d("calledTag", "activities child exits");
                        } else {
                            Log.d("calledTag", "activities doesn't exits");
                            setUpActivities();
                            categoriesRef.setValue("categories");
                            categoriesRef.push().setValue(new Category("Casual", 0));
                            categoriesRef.push().setValue(new Category("Adventure", 0));
                            categoriesRef.push().setValue(new Category("Home", 0));
                            categoriesRef.push().setValue(new Category("Crafts", 0));
                            categoriesRef.push().setValue(new Category("Fancy", 0));
                            categoriesRef.push().setValue(new Category("Volunteering", 0));
                            saveToLocalStorage();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                moveToLocationsFragment();
            }

            return;
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void moveToLocationsFragment() {
        LocationFragment locationFragment = new LocationFragment();
        try {
            fragmentTransaction.replace(R.id.mainContent, locationFragment);
            fragmentTransaction.commit();
        }catch (Exception ex){ }
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

}
