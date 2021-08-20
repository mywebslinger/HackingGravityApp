package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

public class Gson_forgot {
    @SerializedName("status")
    private String status = "status";

    public String getStatus() {
        return status;
    }

    @SerializedName("message")
    private String message = "message";

    public String getMessage() {
        return message;
    }
}
