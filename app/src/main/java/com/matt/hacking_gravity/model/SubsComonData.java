package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

public class SubsComonData {
    @SerializedName("user_id")
    private String user_id = "user_id";

    public String getUser_id() {
        return user_id;
    }

    @SerializedName("subscription_type")
    private String subscription_type = "subscription_type";

    public String getSubscription_type() {
        return subscription_type;
    }

    @SerializedName("start_date")
    private String start_date = "start_date";

    public String getStart_date() {
        return start_date;
    }

    @SerializedName("end_date")
    private String end_date = "end_date";

    public String getEnd_date() {
        return end_date;
    }
}
