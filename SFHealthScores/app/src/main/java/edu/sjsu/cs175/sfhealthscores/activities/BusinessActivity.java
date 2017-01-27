package edu.sjsu.cs175.sfhealthscores.activities;

import android.content.Context;
import android.content.Intent;
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
import edu.sjsu.cs175.sfhealthscores.adapters.InspectionCardAdapter;
import edu.sjsu.cs175.sfhealthscores.helpers.Globals;
import edu.sjsu.cs175.sfhealthscores.helpers.RecyclerItemClickListener;
import edu.sjsu.cs175.sfhealthscores.models.InspectionHistory;
import edu.sjsu.cs175.sfhealthscores.retrofit.RetrofitCallback;
import edu.sjsu.cs175.sfhealthscores.retrofit.SFODQueries;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Business Activity shows business information and inspection history.
 */
public class BusinessActivity extends AppCompatActivity {

    private Context context;
    private LinearLayout headerLayout;
    private TextView scoreTextView;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView phoneNumberTextView;
    private RecyclerView recyclerView;
    private ProgressBar inspectionLoadingSpinner;

    private HashMap<String, Object> businessInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        if (getIntent().hasExtra("businessInfo")) {
            // Get data from intent
            businessInfo = (HashMap<String, Object>) getIntent().getSerializableExtra("businessInfo");
            // Initialize views
            context = getBaseContext();
            headerLayout = (LinearLayout) findViewById(R.id.business_header);
            scoreTextView = (TextView) findViewById(R.id.business_score);
            nameTextView = (TextView) findViewById(R.id.business_name);
            addressTextView = (TextView) findViewById(R.id.business_address);
            phoneNumberTextView = (TextView) findViewById(R.id.business_phone_number);
            recyclerView = (RecyclerView) findViewById(R.id.business_inspection_recycler_view);
            inspectionLoadingSpinner = (ProgressBar) findViewById(R.id.business_inspection_loading_spinner);
            // Fetch data
            fillBusinessInfo();
            fetchInspectionHistory();
        } else {
            Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fill business info data.
     */
    private void fillBusinessInfo() {
        Object score = businessInfo.get("inspectionScore");
        Object name = businessInfo.get("businessName");
        Object address = businessInfo.get("businessAddress");
        Object city = businessInfo.get("businessCity");
        Object state = businessInfo.get("businessState");
        Object phoneNumber = businessInfo.get("businessPhoneNumber");
        int scoreColor = Globals.getScoreColor(Integer.parseInt(score.toString()));
        headerLayout.setBackgroundColor(scoreColor);
        scoreTextView.setText((String) score);
        nameTextView.setText((String) name);
        addressTextView.setText(address + "\n" + city + ", " + state);
        phoneNumberTextView.setText((String) phoneNumber);
    }

    /**
     * Fetch inspection history from API call.
     */
    private void fetchInspectionHistory() {
        showLoading();
        // Call api with Retrofit
        Call<List<InspectionHistory>> apiCall = Globals.SFOD_SERVICE.inspectionHistory(
                SFODQueries.getInspectionHistory(businessInfo.get("businessId").toString()));
        Log.i("API", "Inspection History: " + apiCall.request());
        // Receive results on asynchronous callback
        Callback callback = new RetrofitCallback<List<InspectionHistory>>() {
            @Override
            public void onResponse(Call<List<InspectionHistory>> call, Response<List<InspectionHistory>> response) {
                if (response.isSuccessful()) {
                    setupRecyclerView(response.body());
                } else {
                    Toast.makeText(context, "API Response Failure", Toast.LENGTH_LONG).show();
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
    private void setupRecyclerView(final List<InspectionHistory> data) {
        showInspections();
        // set recycler view data
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new InspectionCardAdapter(data));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // add on item touch listener
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // pass selected inspection to next activity
                        InspectionHistory selected = data.get(position);
                        HashMap<String, Object> fieldMap = Globals.getInspectionHistoryData(selected);
                        Intent intent = new Intent(context, InspectionActivity.class);
                        intent.putExtra("inspectionInfo", fieldMap);
                        startActivity(intent);
                    }
                })
        );
    }

    private void showLoading() {
        inspectionLoadingSpinner.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void showInspections() {
        inspectionLoadingSpinner.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
