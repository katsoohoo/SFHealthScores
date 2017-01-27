package edu.sjsu.cs175.sfhealthscores.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.adapters.ViolationListAdapter;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.models.RiskCount;
import edu.sjsu.cs175.sfhealthscores.models.Violation;
import edu.sjsu.cs175.sfhealthscores.retrofit.RetrofitCallback;
import edu.sjsu.cs175.sfhealthscores.retrofit.SFODQueries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Inspection Activity shows inspection details and violations.
 */
public class InspectionActivity extends AppCompatActivity {

    private Context context;
    private LinearLayout headerLayout;
    private TextView scoreTextView;
    private TextView dateTextView;
    private TextView typeTextView;
    private TextView highTextView;
    private TextView moderateTextView;
    private TextView lowTextView;
    private RecyclerView recyclerView;
    private ProgressBar loadingSpinner;
    private LinearLayout violationLayout;

    private HashMap<String, Object> inspectionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection);

        if (getIntent().hasExtra("inspectionInfo")) {
            // Get data from intent
            inspectionInfo = (HashMap<String, Object>) getIntent().getSerializableExtra("inspectionInfo");
            // Initialize views
            context = getBaseContext();
            headerLayout = (LinearLayout) findViewById(R.id.inspection_header);
            scoreTextView = (TextView) findViewById(R.id.inspection_score);
            dateTextView = (TextView) findViewById(R.id.inspection_date);
            typeTextView = (TextView) findViewById(R.id.inspection_type);
            highTextView = (TextView) findViewById(R.id.inspection_high);
            moderateTextView = (TextView) findViewById(R.id.inspection_moderate);
            lowTextView = (TextView) findViewById(R.id.inspection_low);
            recyclerView = (RecyclerView) findViewById(R.id.inspection_violation_recycler_view);
            loadingSpinner = (ProgressBar) findViewById(R.id.inspection_loader_spinner);
            violationLayout = (LinearLayout) findViewById(R.id.inspection_violation_layout);
            // Fetch data
            fillInspectionInfo();
            if (!inspectionInfo.get("inspectionScore").toString().equals("100")) {
                fetchRiskCount();
                fetchViolations();
            }
            else {
                showViolations();
            }
        } else {
            Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fill initial inspection info.
     */
    private void fillInspectionInfo() {
        Object score = inspectionInfo.get("inspectionScore");
        Object date = inspectionInfo.get("inspectionDate");
        Object type = inspectionInfo.get("inspectionType");
        int scoreColor = Globals.getScoreColor(Integer.parseInt(score.toString()));
        String formatDate = Globals.timestampToDate(date.toString());
        headerLayout.setBackgroundColor(scoreColor);
        scoreTextView.setText((String) score);
        dateTextView.setText(formatDate);
        typeTextView.setText((String) type);
    }

    /**
     * Fetch risk count from API call.
     */
    private void fetchRiskCount() {
        showLoading();
        // Call api with Retrofit
        Call<List<RiskCount>> apiCall = Globals.SFOD_SERVICE.riskCount(SFODQueries.getRiskCount(
                inspectionInfo.get("inspectionId").toString()));
        Log.i("API", "Risk Count: " + apiCall.request());
        // Receive results on asynchronous callback
        Callback callback = new RetrofitCallback<List<RiskCount>>() {

            @Override
            public void onResponse(Call<List<RiskCount>> call, Response<List<RiskCount>> response) {
                if (response.isSuccessful()) {
                    List<RiskCount> results = response.body();
                    setupRiskCount(results);

                } else {
                    Toast.makeText(context, "API Response Failure", Toast.LENGTH_SHORT).show();
                }
            }
        };
        apiCall.enqueue(callback);
    }

    /**
     * Setup risk count with data.
     *
     * @param riskInfo data
     */
    private void setupRiskCount(List<RiskCount> riskInfo) {
        for (RiskCount info : riskInfo) {
            if (info.riskCategory.equals(Globals.HIGH_RISK)) {
                highTextView.setText(info.count);
            } else if (info.riskCategory.equals(Globals.MODERATE_RISK)) {
                moderateTextView.setText(info.count);
            } else if (info.riskCategory.equals(Globals.LOW_RISK)) {
                lowTextView.setText(info.count);
            }
        }
    }

    private void fetchViolations() {
        // Call api with Retrofit
        Call<List<Violation>> apiCall = Globals.SFOD_SERVICE.violations(SFODQueries.getViolations(
                inspectionInfo.get("inspectionId").toString()));
        Log.i("API", "Violation: " + apiCall.request());
        // Receive results on asynchronous callback
        Callback callback = new RetrofitCallback<List<Violation>>() {

            @Override
            public void onResponse(Call<List<Violation>> call, Response<List<Violation>> response) {
                if (response.isSuccessful()) {
                    List<Violation> results = response.body();
                    setupViolations(results);

                } else {
                    Toast.makeText(context, "API Response Failure", Toast.LENGTH_SHORT).show();
                }
            }
        };
        apiCall.enqueue(callback);
    }

    /**
     * Setup violations with data.
     *
     * @param data data
     */
    private void setupViolations(List<Violation> data) {
        showViolations();
        // set recycler view data
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ViolationListAdapter(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void showLoading() {
        loadingSpinner.setVisibility(View.VISIBLE);
        violationLayout.setVisibility(View.GONE);
    }

    private void showViolations() {
        loadingSpinner.setVisibility(View.GONE);
        violationLayout.setVisibility(View.VISIBLE);
    }
}
