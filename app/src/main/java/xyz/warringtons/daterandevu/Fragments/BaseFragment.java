package xyz.warringtons.daterandevu.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import xyz.warringtons.daterandevu.MainActivity;

/**
 * Created by Warrington on 8/12/17.
 */

public class BaseFragment extends Fragment {

    private MainActivity mainActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) getActivity();
        mainActivity.getFragmentManager().addOnBackStackChangedListener(new android.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d("TestingTag", "changed: ");

                shouldDisplayHomeUp();

            }
        });

        Boolean value = shouldDisplayHomeUp();
        Log.d("TestingNow", "onCreate: ");
    }



    public boolean shouldDisplayHomeUp() {
        //Enable Up button only  if there are entries in the back stack
        boolean canBack = false;
        try {
            canBack = getFragmentManager().getBackStackEntryCount() > 0;
        } catch (Exception ex) {

        }

        if (canBack) {
            mainActivity.drawableEnable();
        } else {
            mainActivity.drawableDisable();
        }

        return canBack;
    }
}
