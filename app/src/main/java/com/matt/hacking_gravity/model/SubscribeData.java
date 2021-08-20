package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubscribeData {
    @SerializedName("status")
    private String status = "status";

    @SerializedName("message")
    private String message = "message";

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public ErrorClass getErrorClass() {
        return errorClass;
    }

    @SerializedName("errors")
    ErrorClass errorClass;

    public class ErrorClass{
        public List<String> getSubscription_type() {
            return subscription_type;
        }

        @SerializedName("subscription_type")
        List<String> subscription_type=new ArrayList<>();
    }
}
