package edu.sjsu.cs175.sfhealthscores.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.activities.BusinessActivity;
import edu.sjsu.cs175.sfhealthscores.adapters.BusinessCardAdapter;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.helpers.RecyclerItemClickListener;
import edu.sjsu.cs175.sfhealthscores.helpers.RetryWiFiClickListener;
import edu.sjsu.cs175.sfhealthscores.models.BusinessListing;
import edu.sjsu.cs175.sfhealthscores.models.LatestInspection;
import edu.sjsu.cs175.sfhealthscores.retrofit.RetrofitCallback;
import edu.sjsu.cs175.sfhealthscores.retrofit.SFODQueries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Recent Fragments lists recent inspections.
 * Internet connection required.
 */
public class RecentFragment extends Fragment {

    private View view;
    private Context context;
    private ProgressBar loadingSpinner;
    private LinearLayout internetLayout;
    private RecyclerView recyclerView;

    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show toolbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.setTitle("Recent Inspections");
            actionBar.show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize views
        view = inflater.inflate(R.layout.fragment_recent, container, false);
        context = getContext();
        loadingSpinner = (ProgressBar) view.findViewById(R.id.recent_loading_spinner);
        internetLayout = (LinearLayout) view.findViewById(R.id.recent_internet_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recent_recycler_view);
        // Check internet connection
        if (!Globals.isConnected()) {
            view.findViewById(R.id.retry_wifi_button).setOnClickListener(
                    new RetryWiFiClickListener(this, new RecentFragment()));
            showNoInternet();
        } else {
            fetchRecentData();
        }
        return view;
    }

    /**
     * Fetch recent inspections from API call.
     */
    private void fetchRecentData() {
        showLoading();
        // Call api with Retrofit
        Call<List<BusinessListing>> apiCall = Globals.SFOD_SERVICE.recent(SFODQueries.getRecent());
        Log.i("API", "Recent: " + apiCall.request());
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
        showRecent();
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

    private void showNoInternet() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showLoading() {
        loadingSpinner.setVisibility(View.VISIBLE);
        internetLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showRecent() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}