package edu.sjsu.cs175.sfhealthscores.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.fragments.NearbyFragment;
import edu.sjsu.cs175.sfhealthscores.fragments.RecentFragment;
import edu.sjsu.cs175.sfhealthscores.fragments.SearchFragment;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;

/**
 * Main Activity shows the main app page with navigation.
 * TODO: Add side drawer
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Globals.init(getApplicationContext());
        initView();
    }

    /**
     * Initializes fragment and bottom navigation.
     */
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        fragment = new NearbyFragment();
        replaceFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_nearby:
                                fragment = new NearbyFragment();
                                break;
                            case R.id.action_search:
                                fragment = new SearchFragment();
                                break;
                            case R.id.action_recent:
                                fragment = new RecentFragment();
                                break;
                            default:
                                break;
                        }
                        replaceFragment();
                        return true;
                    }
                }
        );
    }

    /**
     * Replaces fragment.
     */
    private void replaceFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }
}
