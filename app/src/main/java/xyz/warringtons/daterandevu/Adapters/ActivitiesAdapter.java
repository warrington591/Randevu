package xyz.warringtons.daterandevu.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.database.Category;

/**
 * Created by Warrington on 8/12/17.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {

    List<Category> list = new ArrayList<>();
    ActivityCallBack mCallback;

    public ActivitiesAdapter(List<Category> list, ActivityCallBack callback) {
        this.list = list;
        this.mCallback = callback;
    }

    @Override
    public ActivitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_inv_row, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ActivitiesAdapter.ViewHolder holder, int position) {
        final Category currentCategory = list.get(position);
        final String currentItem = currentCategory.getName();
        holder.activityTitle.setText(currentItem);
        setIcon(holder, currentItem);


        checkmarkFromSharedPref(holder, currentCategory);

        holder.activityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(holder, currentCategory);
            }
        });
    }

    private void checkmarkFromSharedPref(ViewHolder holder, Category currentCategory) {

        if(currentCategory.showSelected()){
            holder.checkMark.setVisibility(View.VISIBLE);
            Randevu.getEditor().putBoolean(currentCategory.getName(), true);
        }else{
            holder.checkMark.setVisibility(View.INVISIBLE);
            Randevu.getEditor().putBoolean(currentCategory.getName(), false);
        }
        Randevu.getEditor().apply();

    }

    private void setIcon(ViewHolder holder, String currentItem) {
        switch(currentItem){
            case "Fancy":
                holder.activityImage.setImageResource(R.drawable.bellring);
                break;
            case "Casual":
                holder.activityImage.setImageResource(R.drawable.casual);
                break;
            case "Adventure":
                holder.activityImage.setImageResource(R.drawable.adventure);
                break;
            case "Home":
                holder.activityImage.setImageResource(R.drawable.home);
                break;
            case "Crafts":
                holder.activityImage.setImageResource(R.drawable.crafts);
                break;
            case "Volunteering":
                holder.activityImage.setImageResource(R.drawable.volunteering);
                break;
        }
    }

    private void toggleCheckVisibility(ViewHolder holder ,Category currentItem) {
        if(holder.checkMark.getVisibility()== View.VISIBLE){
            holder.checkMark.setVisibility(View.INVISIBLE);
            mCallback.perform(currentItem, false);
        }else if((holder.checkMark.getVisibility()==View.INVISIBLE)){
            holder.checkMark.setVisibility(View.VISIBLE);
            mCallback.perform(currentItem, true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.activityText)
        public TextView activityTitle;

        @BindView(R.id.activityImage)
        public ImageView activityImage;

        @BindView(R.id.checkmark)
        public ImageView checkMark;

        @BindView(R.id.activityCard)
        public CardView activityCard;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
