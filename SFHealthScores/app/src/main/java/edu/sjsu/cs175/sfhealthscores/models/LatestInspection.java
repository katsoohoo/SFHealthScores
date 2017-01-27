package edu.sjsu.cs175.sfhealthscores.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kat on 12/11/16.
 */

public class LatestInspection {
    @SerializedName("inspection_date")
    @Expose
    public String inspectionDate;
    @SerializedName("inspection_score")
    @Expose
    public String inspectionScore;
    @SerializedName("inspection_type")
    @Expose
    public String inspectionType;

}
