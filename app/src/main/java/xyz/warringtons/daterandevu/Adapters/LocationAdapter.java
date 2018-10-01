package xyz.warringtons.daterandevu.Adapters;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by Warrington on 8/12/17.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    List<String> list = new ArrayList<>();
    ActivityCallBack mCallback;

    public LocationAdapter(List<String> list, ActivityCallBack callback) {
        this.list = list;
        this.mCallback = callback;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_inv_row, parent, false);
        LocationAdapter.ViewHolder vh = new LocationAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String currentItem = list.get(position);
        holder.locationTitle.setText(currentItem);

        switch(currentItem){
            case "New York":
                holder.locationImage.setImageResource(R.drawable.newyork);
                break;
            case "Florida":
                holder.locationImage.setImageResource(R.drawable.florida);
                break;
            case "Los Angeles":
                holder.locationImage.setImageResource(R.drawable.la);
                break;
        }

        switch(currentItem){
            case "New York":
                holder.locationCard.setCardBackgroundColor(Color.parseColor("#66b96a"));
                break;
            case "Florida":
                holder.locationCard.setCardBackgroundColor(Color.parseColor("#f97a83"));
                break;
            case "Los Angeles":
                holder.locationCard.setCardBackgroundColor(Color.parseColor("#f8906f"));
                break;
        }



        holder.locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.perform(currentItem);
            }
        });

    }




    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.locationText)
        public TextView locationTitle;

        @BindView(R.id.locationImage)
        public ImageView locationImage;

        @BindView(R.id.checkmark)
        public ImageView locationcheckMark;

        @BindView(R.id.locationCard)
        public CardView locationCard;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
