package xyz.warringtons.daterandevu.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.warringtons.daterandevu.R;
import xyz.warringtons.daterandevu.Randevu;

/**
 * Created by Warrington on 2/18/18.
 */

public class ProfileFragment extends BaseFragment {

    private FirebaseAuth auth;

    @BindView(R.id.userEmailTV)
    TextView userEmail;

    @BindView(R.id.user_profile_name)
    TextView userName;

    @BindView(R.id.user_profile_photo)
    ImageButton userProfilePhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String email = firebaseUser.getEmail();
        String name = firebaseUser.getDisplayName();
        Uri photoUrl = firebaseUser.getPhotoUrl();

        userEmail.setText(email);
        userName.setText(name);
        if(photoUrl!=null){
            Glide.with(Randevu.getContext()).load(photoUrl).apply(RequestOptions.circleCropTransform()).into(userProfilePhoto);
        }
    }
}
