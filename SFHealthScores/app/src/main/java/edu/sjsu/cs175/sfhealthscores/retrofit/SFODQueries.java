package edu.sjsu.cs175.sfhealthscores.retrofit;

import android.support.test.espresso.core.deps.guava.collect.ImmutableMap;

import java.util.Map;

/**
 * List of queries used to call SFOD api.
 */
public final class SFODQueries {

    private SFODQueries() {
    }

    // Used with BusinessListing model
    public static Map<String, String> getNearby(double lat, double lng) {
        return ImmutableMap.of(
                "$select", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_latitude, business_longitude, business_phone_number, " +
                        "DISTANCE_IN_METERS(business_location, 'POINT(" + lng + " " + lat + ")') AS distance, " +
                        "MAX(inspection_id) AS latest_inspection_id",
                "$where", "inspection_score > 0",
                "$group", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_location, business_latitude, business_longitude, business_phone_number",
                "$order", "distance",
                "$limit", "50"
        );

    }

    // Used with BusinessListing model
    public static Map<String, String> getSearchResults(String query) {
        return ImmutableMap.of(
                "$select", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_latitude, business_longitude, business_phone_number, " +
                        "MAX(inspection_id) AS latest_inspection_id, MAX(inspection_date) AS latest_inspection_date",
                "$where", "inspection_score > 0",
                "$group", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_latitude, business_longitude, business_phone_number",
                "$order", "latest_inspection_date DESC",
                "$q", query
        );
    }

    // Used with BusinessListing model
    public static Map<String, String> getRecent() {
        return ImmutableMap.of(
                "$select", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_latitude, business_longitude, business_phone_number, " +
                        "MAX(inspection_id) AS latest_inspection_id, MAX(inspection_date) AS latest_inspection_date",
                "$where", "inspection_score > 0",
                "$group", "business_id, business_name, business_address, business_city, business_state, " +
                        "business_postal_code, business_latitude, business_longitude, business_phone_number",
                "$order", "latest_inspection_date DESC",
                "$limit", "50"
        );
    }

    // Used with LatestInspection model
    public static Map<String, String> getLatestInspection(String inspectionId) {
        return ImmutableMap.of(
                "$select", "inspection_score, inspection_date, inspection_type",
                "$where", "inspection_id = '" + inspectionId + "'",
                "$limit", "1"
        );
    }

    // Used with InspectionHistory model
    public static Map<String, String> getInspectionHistory(String businessId) {
        return ImmutableMap.of(
                "$select", "inspection_date, inspection_id, inspection_score, inspection_type",
                "$where", "inspection_score > 0 AND business_id = '" + businessId + "'",
                "$group", "inspection_date, inspection_id, inspection_score, inspection_type",
                "$order", "inspection_date DESC"
        );
    }

    // Used with RiskCount model
    public static Map<String, String> getRiskCount(String inspectionId) {
        return ImmutableMap.of(
                "$select", "risk_category, count(*)",
                "$where", "inspection_id = '" + inspectionId + "'",
                "$group", "risk_category"
        );
    }

    // Used with Violation model
    public static Map<String, String> getViolations(String inspectionId) {
        return ImmutableMap.of(
                "$select", "violation_id, violation_description, risk_category",
                "$where", "inspection_id = '" + inspectionId + "'",
                "$order", "violation_id"
        );
    }

}
