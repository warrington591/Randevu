package xyz.warringtons.daterandevu.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.Modules.YelpBusinesses;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.database.Category;

/**
 * Created by Warrington on 2/19/18.
 */

public class YelpBusinessAdapter extends RecyclerView.Adapter<YelpBusinessAdapter.ViewHolder> {


    List<YelpBusinesses> list = new ArrayList<>();

    public YelpBusinessAdapter(List<YelpBusinesses> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yelp_row, parent, false);
        YelpBusinessAdapter.ViewHolder vh = new YelpBusinessAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("Testing", "onBindViewHolder: ");

        final YelpBusinesses currentBusiness = list.get(position);
        holder.yelpName.setText(currentBusiness.getName());

        if(currentBusiness.getImage_url()!=null && currentBusiness.getImage_url()!=""){
            Glide.with(Randevu.getContext()).load(currentBusiness.getImage_url()).into(holder.yelpImage);
        }

        holder.yelpRating.setText(currentBusiness.getRate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.yelpName)
         TextView yelpName;

        @BindView(R.id.yelpImage)
         ImageView yelpImage;

        @BindView(R.id.yelpRating)
        TextView yelpRating;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
