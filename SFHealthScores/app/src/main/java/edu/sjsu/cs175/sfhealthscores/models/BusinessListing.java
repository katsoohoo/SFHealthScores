package edu.sjsu.cs175.sfhealthscores.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kat on 12/11/16.
 */

public class BusinessListing {
    @SerializedName("business_address")
    @Expose
    public String businessAddress;
    @SerializedName("business_city")
    @Expose
    public String businessCity;
    @SerializedName("business_id")
    @Expose
    public String businessId;
    @SerializedName("business_name")
    @Expose
    public String businessName;
    @SerializedName("business_postal_code")
    @Expose
    public String businessPostalCode;
    @SerializedName("business_latitude")
    @Expose
    public String businessLatitude;
    @SerializedName("business_longitude")
    @Expose
    public String businessLongitude;
    @SerializedName("business_state")
    @Expose
    public String businessState;
    @SerializedName("inspection_date")
    @Expose
    public String inspectionDate;
    @SerializedName("inspection_score")
    @Expose
    public String inspectionScore;
    @SerializedName("inspection_type")
    @Expose
    public String inspectionType;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("latest_inspection_id")
    @Expose
    public String latestInspectionId;
    @SerializedName("business_phone_number")
    @Expose
    public String businessPhoneNumber;

}
