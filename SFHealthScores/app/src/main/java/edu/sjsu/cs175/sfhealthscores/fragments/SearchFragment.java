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

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

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
 * Search Fragments performs searches on queries.
 * Internet connection required.
 */
public class SearchFragment extends Fragment {

    private View view;
    private Context context;
    private ProgressBar loadingSpinner;
    private LinearLayout internetLayout;
    private LinearLayout emptySearchLayout;
    private LinearLayout noResultsLayout;
    private FloatingSearchView floatingSearchView;
    private RecyclerView recyclerView;
    private RecyclerItemClickListener recyclerItemClickListener;

    private  Call<List<BusinessListing>> apiCall;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide toolbar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
            actionBar.setTitle("Search");
            actionBar.hide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize views
        view = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        loadingSpinner = (ProgressBar) view.findViewById(R.id.search_loading_spinner);
        internetLayout = (LinearLayout) view.findViewById(R.id.recent_internet_layout);
        emptySearchLayout = (LinearLayout) view.findViewById(R.id.search_empty_layout);
        noResultsLayout = (LinearLayout) view.findViewById(R.id.search_no_results_layout);
        floatingSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_recycler_view);
        // Check internet connection
        if (!Globals.isConnected()) {
            view.findViewById(R.id.retry_wifi_button).setOnClickListener(
                    new RetryWiFiClickListener(this, new SearchFragment()));
            showNoInternet();
        } else {
            setupSearch();
        }
        return view;
    }

    /**
     * Setup search feature.
     */
    private void setupSearch() {
        showEmptySearch();
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // TODO: SHOW SEARCH SUGGESTIONS
            }

            @Override
            public void onSearchAction(String currentQuery) {
                fetchSearchData(currentQuery);
            }
        });
    }

    /**
     * Fetch result businesses from API call.
     *
     * @param query search query
     */
    private void fetchSearchData(String query) {
        showSearching();
        // Call api with Retrofit
        if (apiCall != null && apiCall.isExecuted()) {
            apiCall.cancel();
        }
        apiCall = Globals.SFOD_SERVICE.search(SFODQueries.getSearchResults(query));
        Log.i("API", "Search Results: " + apiCall.request());
        // Receive results on asynchronous callback
        Callback callback = new RetrofitCallback<List<BusinessListing>>() {
            private AtomicInteger updateCount = new AtomicInteger();

            @Override
            public void onResponse(Call<List<BusinessListing>> call, Response<List<BusinessListing>> response) {
                System.out.println(call.isCanceled());
                if (response.isSuccessful() && !call.isCanceled()) {
                    // Found results
                    if (response.body().size() > 0) {
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
                                    // Update inspection info
                                    if (response.isSuccessful()) {
                                        if (response.body().size() > 0) {
                                            LatestInspection latest = response.body().get(0);
                                            business.inspectionScore = latest.inspectionScore;
                                            business.inspectionDate = latest.inspectionDate;
                                            business.inspectionType = latest.inspectionType;
                                            updateCount.incrementAndGet();
                                        }
                                    }
                                    // Setup view when all business retrieve latest score
                                    if (updateCount.get() == results.size()) {
                                        setupRecyclerView(results);
                                    }
                                }

                            });
                        }
                    }
                    // No results
                    else if (response.body().size() == 0) {
                        showNoResults();
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
        showResults();
        // set recycler view data
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new BusinessCardAdapter(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add on item touch listener
        if (recyclerItemClickListener != null)
            recyclerView.removeOnItemTouchListener(recyclerItemClickListener);
        recyclerItemClickListener =
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
                });
        recyclerView.addOnItemTouchListener(recyclerItemClickListener);

    }

    private void showEmptySearch() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        emptySearchLayout.setVisibility(View.VISIBLE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNoInternet() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.VISIBLE);
        emptySearchLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showSearching() {
        loadingSpinner.setVisibility(View.VISIBLE);
        internetLayout.setVisibility(View.GONE);
        emptySearchLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showNoResults() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        emptySearchLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showResults() {
        loadingSpinner.setVisibility(View.GONE);
        internetLayout.setVisibility(View.GONE);
        emptySearchLayout.setVisibility(View.GONE);
        noResultsLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
