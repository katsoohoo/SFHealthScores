package edu.sjsu.cs175.sfhealthscores.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import edu.sjsu.cs175.sfhealthscores.R;

/**
 * Custom button clicker for WiFi Retry button.
 */
public class RetryWiFiClickListener implements View.OnClickListener {
    private Fragment fragment;
    private Fragment newFragment;

    public RetryWiFiClickListener(Fragment fragment, Fragment newFragment) {
        this.fragment = fragment;
        this.newFragment = newFragment;
    }

    @Override
    public void onClick(View view) {
        if (!Globals.isConnected()) {
            Toast.makeText(Globals.APP_CONTEXT, "Connected failed.", Toast.LENGTH_SHORT).show();
        } else {
            FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
            ft.replace(R.id.main_container, newFragment);
            ft.commit();
        }
    }
}
