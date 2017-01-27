package edu.sjsu.cs175.sfhealthscores.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kat on 12/12/16.
 */

public class Violation {
    @SerializedName("risk_category")
    @Expose
    public String riskCategory;
    @SerializedName("violation_description")
    @Expose
    public String violationDescription;
    @SerializedName("violation_id")
    @Expose
    public String violationId;
}
