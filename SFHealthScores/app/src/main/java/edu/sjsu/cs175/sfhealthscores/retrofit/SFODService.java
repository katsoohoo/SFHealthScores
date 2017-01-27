package edu.sjsu.cs175.sfhealthscores.retrofit;

import java.util.List;
import java.util.Map;

import edu.sjsu.cs175.sfhealthscores.models.BusinessListing;
import edu.sjsu.cs175.sfhealthscores.models.InspectionHistory;
import edu.sjsu.cs175.sfhealthscores.models.LatestInspection;
import edu.sjsu.cs175.sfhealthscores.models.RiskCount;
import edu.sjsu.cs175.sfhealthscores.models.Violation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Interface for Retrofit calls.
 */
public interface SFODService {

    @GET(".")
    Call<List<BusinessListing>> nearby(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<BusinessListing>> search(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<BusinessListing>> recent(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<LatestInspection>> latestInspection(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<InspectionHistory>> inspectionHistory(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<RiskCount>> riskCount(@QueryMap Map<String, String> filters);

    @GET(".")
    Call<List<Violation>> violations(@QueryMap Map<String, String> filters);
}
