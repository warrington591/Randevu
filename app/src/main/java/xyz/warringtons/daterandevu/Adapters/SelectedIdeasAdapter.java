package xyz.warringtons.daterandevu.Adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.BinderThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.Interfaces.ActivityCallBack;
import xyz.warringtons.daterandevu.Interfaces.SelectedCallback;
import xyz.warringtons.daterandevu.Interfaces.YelpCallback;
import xyz.warringtons.daterandevu.Modules.Activities;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;
import xyz.warringtons.daterandevu.Utility.RoundedCornersTransformation;
import xyz.warringtons.daterandevu.Utility.YelpClient;

/**
 * Created by Warrington on 8/12/17.
 *
 * The the rows for the final date ideas selected
 */
public class SelectedIdeasAdapter extends RecyclerView.Adapter<SelectedIdeasAdapter.ViewHolder> {

    List<Activities> list = new ArrayList<>();
    SelectedCallback mCallback;

    public static int sCorner = 15;
    public static int sMargin = 2;
    public static int sBorder = 5;
    public static String sColor = "#ffffff";

    public SelectedIdeasAdapter(List<Activities> list, SelectedCallback callback) {
        this.list = list;
        this.mCallback = callback;
    }

    @Override
    public SelectedIdeasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_row, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final SelectedIdeasAdapter.ViewHolder holder, final int position) {

        final Activities currentActivity = list.get(position);
        String dateIdea = currentActivity.getActivityName();
        holder.dateideaTv.setText(dateIdea);




        if(currentActivity.getPicDatabaseId()!=null && !currentActivity.getPicDatabaseId().equals("")){
//            Glide.with(Randevu.getContext()).load(currentActivity.getPicDatabaseId()).apply(RequestOptions.circleCropTransform()).into(holder.dateIdeaImage);

            Glide.with(Randevu.getContext())
                    .load(currentActivity.getPicDatabaseId())
                    .apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(Randevu.getContext(), sCorner, sMargin, sColor, sBorder)))
                    .into(holder.dateIdeaImage);

        }else{
            Glide.with(Randevu.getContext()).load(R.drawable.date_idea_holder).apply(RequestOptions.circleCropTransform()).into(holder.dateIdeaImage);
        }

        holder.invLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Boolean  value= !holder.checkBox.isChecked();
//                holder.checkBox.setChecked(value);
//
//                Activities activitySelected = list.get(position);
//
//                if(value){
//                    activitySelected.setComplete(true);
//                    Randevu.getDaoSession().getActivitiesDao().update(activitySelected);
//                }else{
//                    activitySelected.setComplete(false);
//                    Randevu.getDaoSession().getActivitiesDao().update(activitySelected);
//                }
            }
        });


        holder.dateIdeaImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.perform(currentActivity);
            }
        });

        if(currentActivity.getYelpKeyword()!=null && !currentActivity.getYelpKeyword().equals("")){
            holder.yelpIcon.setVisibility(View.VISIBLE);
        }

        holder.yelpIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YelpClient.makeRequest(currentActivity.getYelpKeyword(), new YelpCallback() {
                    @Override
                    public void hasReturned() {
                        mCallback.showYelp(currentActivity);
                    }
                });

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

        @BindView(R.id.dateIdeaImage)
        ImageView dateIdeaImage;

        @BindView(R.id.yelpIcon)
        ImageView yelpIcon;

        @BindView(R.id.googleMapIcon)
        ImageView googleIcon;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
