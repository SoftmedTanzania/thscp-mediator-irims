package tz.go.moh.him.thscp.mediator.irims.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;


public class IRIMSResponse {

    /**
     * The Status Code.
     */
    @SerializedName("Status")
    @JsonProperty("Status")
    private int status;

    /**
     * The Response Message.
     */
    @SerializedName("Message")
    @JsonProperty("Message")
    private String message;

    public IRIMSResponse() {
    }

    public IRIMSResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
