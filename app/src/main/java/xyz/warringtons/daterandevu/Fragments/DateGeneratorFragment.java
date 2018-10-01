package xyz.warringtons.daterandevu.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Adapters.DateIdeasAdapter;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.Modules.ActivitiesDao;
import xyz.warringtons.daterandevu.Modules.Weather;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.database.Category;

/**
 * Created by Warrington on 8/12/17.
 */

public class DateGeneratorFragment extends BaseFragment {

    @BindView(R.id.dateIdeasRV)
    RecyclerView dateIdeasRV;

    @BindView(R.id.addDateIdea)
    FloatingActionButton addDateIdea;

    @BindView(R.id.nextBtnToSelected)
    Button nextBtn;

    private List<Activities> allActivites, firebaseActivites;
    private AlertDialog.Builder mBuilder;
    private DateIdeasAdapter dateAdapter;
    private String weatherText;
    private String categoryText;
    private String categoryId;
    private FirebaseUser firebaseUser;
    private Handler mainthread = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.date_ideas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            initiateAdapter();
        }
        addDateIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateDialogFragment();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                SelectedIdeasFragment fragment = new SelectedIdeasFragment();
                ft.replace(R.id.mainContent,  fragment);
                ft.addToBackStack("SelectedIdeasFragment");
                ft.commit();
            }
        });
    }

    private void generateDialogFragment() {
        createPopUp();
    }

    private void createPopUp() {
        mBuilder = new AlertDialog.Builder(getActivity());
        final View mView = getLayoutInflater(null).inflate(R.layout.add_custom_idea, null);
        final EditText ideaET = (EditText) mView.findViewById(R.id.ideaET);
        final RadioGroup radioGroupWeather = (RadioGroup) mView.findViewById(R.id.radioGroupWeather);
        final RadioGroup radioGroupCategory = (RadioGroup) mView.findViewById(R.id.radioGroupCategory);

        mBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                saveIdea(dialog, ideaET, radioGroupWeather, mView, radioGroupCategory);
            }
        });

        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void saveIdea(DialogInterface dialog, EditText ideaET, RadioGroup radioGroupWeather, View mView, RadioGroup radioGroupCategory) {
        Boolean wantToCloseDialog = true;

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference activitiesRef = databaseForUser.child("activities");

        Activities activity = new Activities();

        //Name
        String idea = String.valueOf(ideaET.getText());
        if(idea.equals("")){
            Toast.makeText(Randevu.getContext(), "Please feel out your idea", Toast.LENGTH_SHORT).show();
            wantToCloseDialog= false;
        }else{
            activity.setActivityName(idea);
        }

        //Weather
        int weatherId = radioGroupWeather.getCheckedRadioButtonId();
        if(weatherId==-1){
            Toast.makeText(Randevu.getContext(), "Please select weather type", Toast.LENGTH_SHORT).show();
            wantToCloseDialog= false;
        }else{
            RadioButton weatherSelected = (RadioButton) mView.findViewById(weatherId);
            weatherText = (String) weatherSelected.getText();
            activity.setWeatherCondition(weatherText);
        }


        //Category
        int category = radioGroupCategory.getCheckedRadioButtonId();
        if(category==-1){
            Toast.makeText(Randevu.getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
            wantToCloseDialog= false;
        }else{
            RadioButton categorySelected = (RadioButton) mView.findViewById(category);
            categoryText = (String) categorySelected.getText();
            activity.setCategoryId(categoryText);
        }

        //Store
        if(wantToCloseDialog){
            activity.setComplete(false);
            activity.setSelected(false);
            activity.setDeleted(false);
            activitiesRef.push().setValue(activity); // Sends activity to server
            dialog.dismiss();
        }
    }

    private void initiateAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Randevu.getContext());
        dateIdeasRV.setLayoutManager(layoutManager);
        allActivites = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        final DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        DatabaseReference activitiesRef = databaseForUser.child("activities");
        activitiesRef.orderByChild("selected").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseActivites = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    final Activities currentActivity = postSnapshot.getValue(Activities.class);
                    currentActivity.setFirebaseId(postSnapshot.getKey());
                    categoryId = currentActivity.getCategoryId();
                    if(Randevu.getmSettings().getBoolean(categoryId,false)){
                        firebaseActivites.add(currentActivity);
                    }
                }

                 mainthread.post(new Runnable() {
                     @Override
                     public void run() {
                         setAdapter();
                     }
                 });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setAdapter() {
        dateAdapter = new DateIdeasAdapter(firebaseActivites, new ActivityCallBack() {
            @Override
            public void perform(String value) {
            }

            @Override
            public void perform(String value, boolean status) {

            }

            @Override
            public void perform(Category value, boolean status) {

            }

            @Override
            public void perform(Activities currentActivity, String value, boolean status) {
                // save to local database
                Randevu.getDaoSession().getActivitiesDao().update(currentActivity);

                dateAdapter.notifyDataSetChanged();
            }
        });

        dateIdeasRV.setAdapter(dateAdapter);
        initSwipe();
    }

    private List<Activities> _getActivities() {
        List<Activities> activities = null;

        List<WhereCondition> thisPredicate = _buildQuery();
        WhereCondition condition1 = thisPredicate.get(0);

        thisPredicate.remove(0);
        if(thisPredicate.size() >= 1){

            if(thisPredicate.size() == 1){
                WhereCondition condition2 = thisPredicate.get(0);
                activities = Randevu.getDaoSession().getActivitiesDao().queryBuilder()
                        .whereOr(condition1, condition2).list();
            }else{
                WhereCondition condition2 = thisPredicate.get(0);
                WhereCondition[] conds = new WhereCondition[thisPredicate.size()];
                int i=0;
                for(WhereCondition where : thisPredicate){
                    conds[i] = where;
                    i++;
                }

                activities = Randevu.getDaoSession().getActivitiesDao().queryBuilder()
                        .whereOr(condition1, condition2, conds).orderDesc(ActivitiesDao.Properties.Selected).list();
            }


        }else{
            activities = Randevu.getDaoSession().getActivitiesDao().queryBuilder()
                    .where(condition1).orderDesc(ActivitiesDao.Properties.Selected).list();
        }

            return activities;
    }

    private List<WhereCondition> _buildQuery() {
        List<WhereCondition> conditions = new ArrayList<>();


        if(Randevu.getmSettings().getBoolean("Casual",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Casual");
            conditions.add(condition);
        }

        if(Randevu.getmSettings().getBoolean("Adventure",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Adventure");
            conditions.add(condition);
        }

        if(Randevu.getmSettings().getBoolean("Home",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Home");
            conditions.add(condition);
        }

        if(Randevu.getmSettings().getBoolean("Fancy",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Fancy");
            conditions.add(condition);
        }

        if(Randevu.getmSettings().getBoolean("Volunteering",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Volunteering");
            conditions.add(condition);
        }

        if(Randevu.getmSettings().getBoolean("Crafts",false)){
            WhereCondition condition = ActivitiesDao.Properties.CategoryId.eq("Crafts");
            conditions.add(condition);
        }

        //Location Specific
        String location = Randevu.getmSettings().getString("location","");
        WhereCondition condition = ActivitiesDao.Properties.Location.eq(location);
        conditions.add(condition);

        return conditions;
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
                Randevu.getDaoSession().getActivitiesDao().delete(currentActivity);
                initiateAdapter();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(dateIdeasRV);
    }

    private void storeIdea(String idea, String weatherText, String categoryText) {
        Activities customActivity = new Activities();
        customActivity.setDeleted(false);
        customActivity.setSelected(false);
        customActivity.setCategoryId(categoryText);
        customActivity.setWeatherCondition(weatherText);
        customActivity.setActivityName(idea);
        Randevu.getDaoSession().getActivitiesDao().insert(customActivity);
        initiateAdapter();

    }
}
