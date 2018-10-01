package xyz.warringtons.daterandevu.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.warringtons.daterandevu.R;

/**
 * Created by Warrington on 2/13/18.
 */

public class InitialScreen extends Fragment {

    @BindView(R.id.link_signup)
    TextView signupText;
    private SignUpFragment signupFragment;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.initial_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
    }


    @OnClick(R.id.link_signup)
    public void OnCLick(){
        signupFragment = new SignUpFragment();
        try {
            fragmentTransaction.replace(R.id.mainContent, signupFragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
            Log.d("Testing fragment", "move to initial screen ");
        }
    }
}
