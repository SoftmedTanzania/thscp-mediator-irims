package tz.go.moh.him.thscp.mediator.irims.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class ProductRecallRequest {

    @JsonProperty("uuid")
    @SerializedName("uuid")
    private String uuid;

    @JsonProperty("actionRequired")
    @SerializedName("actionRequired")
    private String actionRequired;

    @JsonProperty("affectedCommunity")
    @SerializedName("affectedCommunity")
    private String affectedCommunity;

    @JsonProperty("batchNumber")
    @SerializedName("batchNumber")
    private String batchNumber;

    @JsonProperty("closureDate")
    @SerializedName("closureDate")
    private String closureDate;

    @JsonProperty("description")
    @SerializedName("description")
    private String description;

    @JsonProperty("distributedQuantity")
    @SerializedName("distributedQuantity")
    private int distributedQuantity;

    @JsonProperty("issue")
    @SerializedName("issue")
    private String issue;

    @JsonProperty("productCode")
    @SerializedName("productCode")
    private String productCode;

    @JsonProperty("recallDate")
    @SerializedName("recallDate")
    private String recallDate;

    @JsonProperty("recallFrequency")
    @SerializedName("recallFrequency")
    private int recallFrequency;

    @JsonProperty("recallOrganization")
    @SerializedName("recallOrganization")
    private String recallOrganization;

    @JsonProperty("recalledQuantity")
    @SerializedName("recalledQuantity")
    private int recalledQuantity;

    @JsonProperty("receivedQuantity")
    @SerializedName("receivedQuantity")
    private int receivedQuantity;

    @JsonProperty("region")
    @SerializedName("region")
    private String region;

    @JsonProperty("startDate")
    @SerializedName("startDate")
    private String startDate;

    @JsonProperty("unit")
    @SerializedName("unit")

    private String unit;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getActionRequired()
    {
        return actionRequired;
    }

    public void setActionRequired(String actionRequired)
    {
        this.actionRequired = actionRequired;
    }

    public String getAffectedCommunity()
    {
        return affectedCommunity;
    }

    public void setAffectedCommunity(String affectedCommunity)
    {
        this.affectedCommunity = affectedCommunity;
    }

    public String getBatchNumber()
    {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber)
    {
        this.batchNumber = batchNumber;
    }

    public String getClosureDate()
    {
        return closureDate;
    }

    public void setClosureDate(String closureDate)
    {
        this.closureDate = closureDate;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getDistributedQuantity()
    {
        return distributedQuantity;
    }

    public void setDistributedQuantity(int distributedQuantity)
    {
        this.distributedQuantity = distributedQuantity;
    }

    public String getIssue()
    {
        return issue;
    }

    public void setIssue(String issue)
    {
        this.issue = issue;
    }

    public String getRecallDate()
    {
        return recallDate;
    }

    public void setRecallDate(String recallDate)
    {
        this.recallDate = recallDate;
    }

    public int getRecallFrequency()
    {
        return recallFrequency;
    }

    public void setRecallFrequency(int recallFrequency)
    {
        this.recallFrequency = recallFrequency;
    }

    public int getRecalledQuantity()
    {
        return recalledQuantity;
    }

    public void setRecalledQuantity(int recalledQuantity)
    {
        this.recalledQuantity = recalledQuantity;
    }

    public int getReceivedQuantity()
    {
        return receivedQuantity;
    }

    public void setReceivedQuantity(int receivedQuantity)
    {
        this.receivedQuantity = receivedQuantity;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getUnit()
    {
        return unit;
    }

    public void setUnit(String unit)
    {
        this.unit = unit;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getRecallOrganization() {
        return recallOrganization;
    }

    public void setRecallOrganization(String recallOrganization) {
        this.recallOrganization = recallOrganization;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
