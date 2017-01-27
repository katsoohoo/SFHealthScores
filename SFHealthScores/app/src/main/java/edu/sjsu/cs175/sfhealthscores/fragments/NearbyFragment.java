package edu.sjsu.cs175.sfhealthscores.fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.activities.BusinessActivity;
import edu.sjsu.cs175.sfhealthscores.adapters.BusinessCardAdapter;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.helpers.RecyclerItemClickListener;
import edu.sjsu.cs175.sfhealthscores.helpers.RetryLocationClickListener;
import edu.sjsu.cs175.sfhealthscores.helpers.RetryWiFiClickListener;
import edu.sjsu.cs175.sfhealthscores.models.BusinessListing;
import edu.sjsu.cs175.sfhealthscores.models.LatestInspection;
import edu.sjsu.cs175.sfhealthscores.retrofit.RetrofitCallback;
import edu.sjsu.cs175.sfhealthscores.retrofit.SFODQueries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Nearby Fragment lists nearby businesses based on location.
 * Internet connection and Location services required.
 * TODO: Pull to refresh location
 */
public class NearbyFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private View view;
    private Context context;
    private ProgressBar loadingSpinner;
    private LinearLayout permissionLayout;
    private LinearLayout internetLayout;
    private LinearLayout locationLayout;
    private RecyclerView recyclerView;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;


    public NearbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show toolbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.setTitle("Nearby Food Facilities");
            actionBar.show();
        }
    }

    @Override
    public void onStop() {
        Log.i("LOCATION", "Disconnect Google Play services");
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize views
        view = inflater.inflate(R.layout.fragment_nearby, container, false);
        context = getContext();
        loadingSpinner = (ProgressBar) view.findViewById(R.id.nearby_loading_spinner);
        permissionLayout = (LinearLayout) view.findViewById(R.id.nearby_permission_layout);
        internetLayout = (LinearLayout) view.findViewById(R.id.nearby_internet_layout);
        locationLayout = (LinearLayout) view.findViewById(R.id.nearby_location_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.nearby_recycler_view);
        // Create an instance of GoogleAPIClient
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
            Log.i("LOCATION", "Connect Google Play services");
        }
        // Check internet connection
        if (!Globals.isConnected()) {
            view.findViewById(R.id.retry_wifi_button).setOnClickListener(
                    new RetryWiFiClickListener(this, new NearbyFragment()));
            showNoInternet();
        }
        return view;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        findLocation();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("LOCATION", "Connection failed: " + connectionResult);
        Toast.makeText(context, "Google Play services connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("LOCATION", "Connection suspended: " + i);
        Toast.makeText(context, "Google Play services connection suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Globals.PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.i("LOCATION", "Permission Denied");
                    showNoPermission();
                }
                else {
                    findLocation();
                }
                break;
            default:
                showNoLocation();
                break;
        }
    }

    /**
     * Attempt to find user's location and takes action.
     */
    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    Globals.PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i("LOCATION", "Location found at: " + mLastLocation);
        // Check if found location
        if (mLastLocation != null && Globals.isConnected()) {
            fetchNearbyData(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
        else if (Globals.isConnected()) {
            view.findViewById(R.id.retry_location_button).setOnClickListener(
                    new RetryLocationClickListener(this, new NearbyFragment()));
            showNoLocation();
        }
    }

    /**
     * Fetch nearby businesses from API call.
     */
    private void fetchNearbyData(double lat, double lng) {
        showLoading();
        // Call api with Retrofit
        Call<List<BusinessListing>> apiCall = Globals.SFOD_SERVICE.nearby(
                SFODQueries.getNearby(lat, lng));
        Log.i("API", "Nearby: " + apiCall.request());
        // Receive results on asynchronous callback
        Callback callback = new RetrofitCallback<List<BusinessListing>>() {
            private AtomicInteger updateCount = new AtomicInteger();

            @Override
            public void onResponse(Call<List<BusinessListing>> call, Response<List<BusinessListing>> response) {
                if (response.isSuccessful()) {
                    final List<BusinessListing> results = response.body();
                    // get latest inspection for each business
                    for (final BusinessListing business : results) {
                        // Call api with Retrofit
                        Call<List<LatestInspection>> apiCall = Globals.SFOD_SERVICE.latestInspection(
                                SFODQueries.getLatestInspection(business.latestInspectionId));
                        // Receive results on asynchronous callback
                        apiCall.enqueue(new RetrofitCallback<List<LatestInspection>>() {
                            @Override
                            public void onResponse(Call<List<LatestInspection>> call, Response<List<LatestInspection>> response) {
                                // update inspection info
                                if (response.isSuccessful()) {
                                    if (response.body().size() > 0) {
                                        LatestInspection latest = response.body().get(0);
                                        business.inspectionScore = latest.inspectionScore;
                                        business.inspectionDate = latest.inspectionDate;
                                        business.inspectionType = latest.inspectionType;
                                        updateCount.incrementAndGet();
                                    }
                                }
                                // setup view when all business retrieve latest score
                                if (updateCount.get() == results.size()) {
                                    setupRecyclerView(results);
                                }
                            }

                        });
                    }
                } else {
                    Toast.makeText(context, "API Response Failure", Toast.LENGTH_SHORT).show();
                }
            }
        };
        apiCall.enqueue(callback);
    }

    /**
     * Setup recycler view with data.
     *
     * @param data
     */
    private void setupRecyclerView(final List<BusinessListing> data) {
        showNearby();
        // set recycler view data
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BusinessCardAdapter(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setVisibility(View.VISIBLE);
        // add on item touch listener
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // pass selected business to next activity
                        BusinessListing selected = data.get(position);
                        HashMap<String, Object> fieldMap = Globals.getBusinessListingData(selected);
                        Intent intent = new Intent(context, BusinessActivity.class);
                        intent.putExtra("businessInfo", fieldMap);
                        startActivity(intent);
                    }
                })
        );
    }

    private void showNoPermission() {
        loadingSpinner.setVisibility(View.GONE);
        permissionLayout.setVisibility(View.VISIBLE);
        internetLayout.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNoInternet() {
        loadingSpinner.setVisibility(View.GONE);
        permissionLayout.setVisibility(View.GONE);
        internetLayout.setVisibility(View.VISIBLE);
        locationLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNoLocation() {
        loadingSpinner.setVisibility(View.GONE);
        permissionLayout.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        locationLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showLoading() {
        loadingSpinner.setVisibility(View.VISIBLE);
        permissionLayout.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNearby() {
        loadingSpinner.setVisibility(View.GONE);
        permissionLayout.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        locationLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}
