package edu.sjsu.cs175.sfhealthscores.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

import edu.sjsu.cs175.sfhealthscores.R;
import edu.sjsu.cs175.sfhealthscores.models.BusinessListing;
import edu.sjsu.cs175.sfhealthscores.models.InspectionHistory;
import edu.sjsu.cs175.sfhealthscores.retrofit.SFODService;
import edu.sjsu.cs175.sfhealthscores.retrofit.ServiceGenerator;

/**
 * Global variables and methods.
 */
public final class Globals {

    // Prevent instantiation
    private Globals() {
    }

    public static Context APP_CONTEXT;
    public static SFODService SFOD_SERVICE;

    public static final int PERMISSIONS_REQUEST_LOCATION = 341;
    public static final int GOOD_SCORE = 91;
    public static final int ADEQUATE_SCORE = 86;
    public static final int NEEDS_IMPROVEMENT_SCORE = 71;
    public static final int POOR_SCORE = 70;
    public static final String HIGH_RISK = "High Risk";
    public static final String MODERATE_RISK = "Moderate Risk";
    public static final String LOW_RISK = "Low Risk";

    /**
     * Initialize fields.
     *
     * @param context application context
     */
    public static void init(Context context) {
        APP_CONTEXT = context;
        SFOD_SERVICE = ServiceGenerator.createService(SFODService.class);
    }

    /**
     * Check if connected to internet
     *
     * @return true if connect, else false
     */
    public static boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) APP_CONTEXT.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Get color code based on score
     *
     * @param score inspection score
     * @return color code
     */
    public static int getScoreColor(int score) {
        if (score >= GOOD_SCORE) {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorGoodScore);
        } else if (score >= ADEQUATE_SCORE) {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorAdequateScore);
        } else if (score >= NEEDS_IMPROVEMENT_SCORE) {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorNeedsImprovement);
        } else {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorPoorScore);
        }
    }

    /**
     * Get color code based on violation category
     *
     * @param category violation category
     * @return color code
     */
    public static int getViolationColor(String category) {
        if (category.equals(Globals.HIGH_RISK)) {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorHighRisk);
        } else if (category.equals(Globals.MODERATE_RISK)) {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorModerateRisk);
        } else {
            return ContextCompat.getColor(APP_CONTEXT, R.color.colorLowRisk);
        }
    }

    /**
     * Convert timestamp to date.
     *
     * @param timestamp timestamp
     * @return date as string
     */
    public static String timestampToDate(String timestamp) {
        return timestamp.split("T")[0];
    }

    /**
     * Convert meters to miles.
     *
     * @param meters meters
     * @return miles as double
     */
    public static double metersToMiles(double meters) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        return new Double(df.format(meters / 1609.344));
    }

    /**
     * Convert business listing to hashmap with fields and values.
     * Used when passing intent to other activities.
     *
     * @param business business listing to convert
     * @return hashmap of fields and values
     */
    public static HashMap<String, Object> getBusinessListingData(BusinessListing business) {
        HashMap<String, Object> data = new HashMap<>();
        for (Field field : business.getClass().getDeclaredFields()) {
            try {
                data.put(field.getName(), field.get(business));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * Convert inspection history to hashmap with fields and values.
     * Used when passing intent to other activities.
     *
     * @param inspection inspection to convert
     * @return hashmap of fields and values
     */
    public static HashMap<String, Object> getInspectionHistoryData(InspectionHistory inspection) {
        HashMap<String, Object> data = new HashMap<>();
        for (Field field : inspection.getClass().getDeclaredFields()) {
            try {
                data.put(field.getName(), field.get(inspection));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
