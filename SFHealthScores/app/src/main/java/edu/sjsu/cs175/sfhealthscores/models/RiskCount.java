package edu.sjsu.cs175.sfhealthscores.models;

/**
 * Created by kat on 12/8/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RiskCount {

    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("risk_category")
    @Expose
    public String riskCategory;
}