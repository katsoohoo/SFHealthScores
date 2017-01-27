package edu.sjsu.cs175.sfhealthscores.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import edu.sjsu.cs175.sfhealthscores.R;

/**
 * Custom button clicker for WiFi Retry button.
 */
public class RetryLocationClickListener implements View.OnClickListener {
    private Fragment fragment;
    private Fragment newFragment;

    public RetryLocationClickListener(Fragment fragment, Fragment newFragment) {
        this.fragment = fragment;
        this.newFragment = newFragment;
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
        ft.replace(R.id.main_container, newFragment);
        ft.commit();
    }
}
