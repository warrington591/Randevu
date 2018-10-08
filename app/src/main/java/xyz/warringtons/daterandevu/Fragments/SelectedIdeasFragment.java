package xyz.warringtons.daterandevu.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Adapters.DateIdeasAdapter;
import xyz.warringtons.daterandevu.Adapters.SelectedIdeasAdapter;
import xyz.warringtons.daterandevu.Adapters.YelpBusinessAdapter;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.Interfaces.SelectedCallback;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.Modules.ActivitiesDao;
import xyz.warringtons.daterandevu.Modules.Weather;
import xyz.warringtons.daterandevu.Modules.YelpBusinesses;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;

import static android.R.attr.handle;

/**
 * Created by Warrington on 8/12/17.
 */

public class SelectedIdeasFragment extends BaseFragment {

    @BindView(R.id.dateIdeasRV)
    RecyclerView dateIdeasRV;

    @BindView(R.id.weatherDescTv)
    TextView weatherDescTv;

    @BindView(R.id.temperatureTv)
    TextView temperatureTv;

    @BindView(R.id.weatherImage)
    ImageView weatherImage;

    @BindView(R.id.diceRandomize)
    FloatingActionButton diceRandomize;

    @BindView(R.id.dateIdeas)
    TextView dateIdeas;

    @BindView(R.id.dateIdeasRL)
    RelativeLayout dateIdeasRL;

    @BindView(R.id.datesCompletedRL)
    RelativeLayout datesCompletedRL;

    @BindView(R.id.datesCompleted)
    TextView dateCompleted;

    @BindView(R.id.leftLineSeperator)
    View leftLine;

    @BindView(R.id.rightLineSeperator)
    View rightLine;

    @BindView(R.id.noDatesCompleted)
    RelativeLayout noDatesCompleted;


    private List<Activities> allActivites, firebaseActivites;
    private List<Activities> incompleteActivites;
    private AlertDialog.Builder mBuilder;
    private SelectedIdeasAdapter dateAdapter;
    private String weatherText;
    private String categoryText;
    private ProgressDialog progressDoalog;
    Handler handle;
    Handler runOnMain = new Handler(Looper.myLooper());
    private String randomResultMessage;
    private Runnable showRandom;
    private ProgressDialog foundDialog;
    private Random rand;
    private int randomNumber;
    private Activities choosenAtRandom;
    private Thread dialogThread;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String categoryId;
    private Boolean showCompleted= false;
    private Handler mainHandler;
    private int amountOfActivities;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selected_date_ideas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final Weather weather = Randevu.getDaoSession().getWeatherDao().load((long) 1);
        if(weather!=null){
            updateWeather(weather);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Randevu.getContext());
        dateIdeasRV.setLayoutManager(layoutManager);
        initiateAdapter();

        dateIdeasRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected_color));
                rightLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                showCompleted= false;
                initiateAdapter();
            }
        });


        datesCompletedRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.selected_color));
                leftLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
                showCompleted = true;
                initiateAdapter();
            }
        });

        diceRandomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateRandomizer();
            }
        });

        handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDoalog.incrementProgressBy(1);
            }
        };

        hideRandomizerOnScroll();
    }

    private void activateRandomizer() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference activitiesRef = databaseForUser.child("activities");
        activitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                incompleteActivites = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Activities currentActivity = postSnapshot.getValue(Activities.class);
                    String currentCategory = currentActivity.getCategoryId();

                    if(currentActivity.getSelected()==true
                            && currentActivity.getComplete()==false
                            && Randevu.getmSettings().getBoolean(currentCategory,false)){
                        incompleteActivites.add(currentActivity);
                    }
                }


                if (incompleteActivites.size()==0){
                    Toast.makeText(Randevu.getContext(), "All dates have been done", Toast.LENGTH_LONG).show();
                    return;
                }

                randomizeActivitySelection();

                if(getActivity()!=null){
                    progressDoalog = new ProgressDialog(getActivity(), R.style.loadingDialog);
                    progressDoalog.setMax(100);
                    progressDoalog.setTitle("Loading random date");
                    progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDoalog.show();

                    dialogThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (progressDoalog.getProgress() <= progressDoalog.getMax()) {
                                    Thread.sleep(15);
                                    handle.sendMessage(handle.obtainMessage());
                                    if (progressDoalog.getProgress() == progressDoalog.getMax()) {
                                        progressDoalog.setProgress(0);
                                        progressDoalog.cancel();
                                        progressDoalog.dismiss();
                                        Log.d("CurrentTag", "run: ");
                                        break;
                                    }
                                }

                                runOnMain.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        builder = new AlertDialog.Builder(getActivity(), R.style.foundDialog);
                                        builder.setMessage("How about .... " +randomResultMessage + " ?");
                                        builder.setTitle("Date Idea");
                                        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.setNegativeButton("Nah", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog = builder.create();
                                        dialog.show();
                                    }
                                }, 500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    dialogThread.start();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void randomizeActivitySelection() {
        if(incompleteActivites.size()==1){
            choosenAtRandom = incompleteActivites.get(0);
        }else{
            rand = new Random();
            randomNumber = rand.nextInt(incompleteActivites.size()-1)+1;
            choosenAtRandom = incompleteActivites.get(randomNumber);
        }

        randomResultMessage = choosenAtRandom.getActivityName();
    }

    private void hideRandomizerOnScroll() {
        dateIdeasRV.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 && diceRandomize.isShown())
                    diceRandomize.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    diceRandomize.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void updateWeather(Weather weather) {
        weatherDescTv.setText(weather.getWeather());
        temperatureTv.setText(weather.getTemperature());
        String url ="http://openweathermap.org/img/w/"+weather.getWeatherIcon()+".png";
        Glide.with(this).load(url).into(weatherImage);
    }

    private void initiateAdapter() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference activitiesRef = databaseForUser.child("activities");

        activitiesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allActivites = new ArrayList<>();
                collectAllActivities(dataSnapshot);

                if(amountOfActivities!=0){
                    dateIdeasRV.setVisibility(View.VISIBLE);
                    noDatesCompleted.setVisibility(View.GONE);
                    dateIdeasRV.setAdapter(dateAdapter);
                    initSwipe();
                }else{
                    dateIdeasRV.setVisibility(View.GONE);
                    noDatesCompleted.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    private void collectAllActivities(DataSnapshot dataSnapshot) {
        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            Activities currentActivity = postSnapshot.getValue(Activities.class);
            currentActivity.setFirebaseId(postSnapshot.getKey());
            categoryId = currentActivity.getCategoryId();

            if(currentActivity.getSelected() == true && currentActivity.getComplete()==showCompleted && Randevu.getmSettings().getBoolean(categoryId,false)){
                allActivites.add(currentActivity);
            }
        }

        amountOfActivities = allActivites.size();

        dateAdapter = new SelectedIdeasAdapter(allActivites, new SelectedCallback() {

            @Override
            public void perform(final Activities currentActivity) {
                setCompleteOrNot(currentActivity);
            }

            @Override
            public void showYelp(Activities activities) {
                //Create dialog messsage
                final View mView = getLayoutInflater(null).inflate(R.layout.yelp_dialogbox, null);
                RecyclerView recyclerView = (RecyclerView)  mView.findViewById(R.id.yelpRecyclerView);
                List<YelpBusinesses> yelpBusinesses = Randevu.getDaoSession().getYelpBusinessesDao().loadAll();
                RecyclerView.LayoutManager manager =  new LinearLayoutManager(Randevu.getContext());
                recyclerView.setLayoutManager(manager);

                YelpBusinessAdapter yelpAdapter = new YelpBusinessAdapter(yelpBusinesses);
                recyclerView.setAdapter(yelpAdapter);

                mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mBuilder = new AlertDialog.Builder(getActivity());

                        mBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                        mBuilder.setView(mView);
                        AlertDialog dialog = mBuilder.create();
                        dialog.show();
                    }
                });

            }

        });
    }

    private void setCompleteOrNot(final Activities currentActivity) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        final DatabaseReference activitiesRef = databaseForUser.child("activities");

        mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Complete Date");
        mBuilder.setMessage("Have you completed this date?");
        mBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                activitiesRef.child(currentActivity.getFirebaseId()).child("complete").setValue(true);
                dialog.dismiss();
            }
        });

        mBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                activitiesRef.child(currentActivity.getFirebaseId()).child("complete").setValue(false);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Activities currentActivity = allActivites.get(position);
                String firebaseActivityId = currentActivity.getFirebaseId();

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = firebaseUser.getUid();
                DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
                DatabaseReference activitiesRef = databaseForUser.child("activities");
                activitiesRef.child(firebaseActivityId).child("selected").setValue(false);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(dateIdeasRV);
    }

}
