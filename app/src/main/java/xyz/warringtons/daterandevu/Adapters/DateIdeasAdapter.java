package xyz.warringtons.daterandevu.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.R;

/**
 * Created by Warrington on 8/12/17.
 */

public class DateIdeasAdapter  extends RecyclerView.Adapter<DateIdeasAdapter.ViewHolder> {

    List<Activities> list = new ArrayList<>();
    ActivityCallBack mCallback;

    public DateIdeasAdapter(List<Activities> list, ActivityCallBack callback) {
        this.list = list;
        Collections.reverse(list); // selected items show first
        this.mCallback = callback;
    }

    @Override
    public DateIdeasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_inv_row, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final DateIdeasAdapter.ViewHolder holder, int position) {

        final Activities currentActivity = list.get(position);
        String dateIdea = currentActivity.getActivityName();
        holder.dateideaTv.setText(dateIdea);

        boolean completed = currentActivity.wasSelected();
        if(completed){
            holder.completedStatus.setImageResource(R.drawable.ic_action_completed);
        }else{
            holder.completedStatus.setImageResource(R.drawable.ic_action_bullet);
        }

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        DatabaseReference databaseForUser = FirebaseDatabase.getInstance().getReference("users").child(userId);
        final DatabaseReference activitiesRef = databaseForUser.child("activities");


        holder.invLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentActivity.wasSelected()){
                    currentActivity.setSelected(false);
                    activitiesRef.child(currentActivity.getFirebaseId()).child("selected").setValue(false);
                }else{
                    currentActivity.setSelected(true);
                    activitiesRef.child(currentActivity.getFirebaseId()).child("selected").setValue(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.dateideaTv)
        TextView dateideaTv;

        @BindView(R.id.invLL)
        LinearLayout invLL;

        @BindView(R.id.completedStatus)
        ImageView completedStatus;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
