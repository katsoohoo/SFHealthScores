package edu.sjsu.cs175.sfhealthscores.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Business {

    @SerializedName("business_address")
    @Expose
    private String businessAddress;
    @SerializedName("business_city")
    @Expose
    private String businessCity;
    @SerializedName("business_id")
    @Expose
    private String businessId;
    @SerializedName("business_latitude")
    @Expose
    private String businessLatitude;
    //    @SerializedName("business_location")
//    @Expose
//    private BusinessLocation businessLocation;
    @SerializedName("business_longitude")
    @Expose
    private String businessLongitude;
    @SerializedName("business_name")
    @Expose
    private String businessName;
    @SerializedName("business_postal_code")
    @Expose
    private String businessPostalCode;
    @SerializedName("business_state")
    @Expose
    private String businessState;
    @SerializedName("inspection_date")
    @Expose
    private String inspectionDate;
    @SerializedName("inspection_id")
    @Expose
    private String inspectionId;
    @SerializedName("inspection_score")
    @Expose
    private String inspectionScore;
    @SerializedName("inspection_type")
    @Expose
    private String inspectionType;
    @SerializedName("risk_category")
    @Expose
    private String riskCategory;
    @SerializedName("violation_description")
    @Expose
    private String violationDescription;
    @SerializedName("violation_id")
    @Expose
    private String violationId;
    @SerializedName("business_phone_number")
    @Expose
    private String businessPhoneNumber;

    /**
     * @return The businessAddress
     */
    public String getBusinessAddress() {
        return businessAddress;
    }

    /**
     * @param businessAddress The business_address
     */
    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress;
    }

    /**
     * @return The businessCity
     */
    public String getBusinessCity() {
        return businessCity;
    }

    /**
     * @param businessCity The business_city
     */
    public void setBusinessCity(String businessCity) {
        this.businessCity = businessCity;
    }

    /**
     * @return The businessId
     */
    public String getBusinessId() {
        return businessId;
    }

    /**
     * @param businessId The business_id
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * @return The businessLatitude
     */
    public String getBusinessLatitude() {
        return businessLatitude;
    }

    /**
     * @param businessLatitude The business_latitude
     */
    public void setBusinessLatitude(String businessLatitude) {
        this.businessLatitude = businessLatitude;
    }

//    /**
//     *
//     * @return
//     * The businessLocation
//     */
//    public BusinessLocation getBusinessLocation() {
//        return businessLocation;
//    }
//
//    /**
//     *
//     * @param businessLocation
//     * The business_location
//     */
//    public void setBusinessLocation(BusinessLocation businessLocation) {
//        this.businessLocation = businessLocation;
//    }

    /**
     * @return The businessLongitude
     */
    public String getBusinessLongitude() {
        return businessLongitude;
    }

    /**
     * @param businessLongitude The business_longitude
     */
    public void setBusinessLongitude(String businessLongitude) {
        this.businessLongitude = businessLongitude;
    }

    /**
     * @return The businessName
     */
    public String getBusinessName() {
        return businessName;
    }

    /**
     * @param businessName The business_name
     */
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**
     * @return The businessPostalCode
     */
    public String getBusinessPostalCode() {
        return businessPostalCode;
    }

    /**
     * @param businessPostalCode The business_postal_code
     */
    public void setBusinessPostalCode(String businessPostalCode) {
        this.businessPostalCode = businessPostalCode;
    }

    /**
     * @return The businessState
     */
    public String getBusinessState() {
        return businessState;
    }

    /**
     * @param businessState The business_state
     */
    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    /**
     * @return The inspectionDate
     */
    public String getInspectionDate() {
        return inspectionDate;
    }

    /**
     * @param inspectionDate The inspection_date
     */
    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    /**
     * @return The inspectionId
     */
    public String getInspectionId() {
        return inspectionId;
    }

    /**
     * @param inspectionId The inspection_id
     */
    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    /**
     * @return The inspectionScore
     */
    public String getInspectionScore() {
        return inspectionScore;
    }

    /**
     * @param inspectionScore The inspection_score
     */
    public void setInspectionScore(String inspectionScore) {
        this.inspectionScore = inspectionScore;
    }

    /**
     * @return The inspectionType
     */
    public String getInspectionType() {
        return inspectionType;
    }

    /**
     * @param inspectionType The inspection_type
     */
    public void setInspectionType(String inspectionType) {
        this.inspectionType = inspectionType;
    }

    /**
     * @return The riskCategory
     */
    public String getRiskCategory() {
        return riskCategory;
    }

    /**
     * @param riskCategory The risk_category
     */
    public void setRiskCategory(String riskCategory) {
        this.riskCategory = riskCategory;
    }

    /**
     * @return The violationDescription
     */
    public String getViolationDescription() {
        return violationDescription;
    }

    /**
     * @param violationDescription The violation_description
     */
    public void setViolationDescription(String violationDescription) {
        this.violationDescription = violationDescription;
    }

    /**
     * @return The violationId
     */
    public String getViolationId() {
        return violationId;
    }

    /**
     * @param violationId The violation_id
     */
    public void setViolationId(String violationId) {
        this.violationId = violationId;
    }

    /**
     * @return The businessPhoneNumber
     */
    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    /**
     * @param businessPhoneNumber The business_phone_number
     */
    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }

}
