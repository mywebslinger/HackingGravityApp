package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_SubscriptionPlan {

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

    @SerializedName("data")
    public ArrayList<datares> dataplan = new ArrayList<>();

    public class datares {
        @SerializedName("id")
        private String id = "id";

        public String getId() {
            return id;
        }

        @SerializedName("name")
        private String name = "name";

        public String getName() {
            return name;
        }

        @SerializedName("value")
        private String value = "value";

        public String getValue() {
            return value;
        }

        @SerializedName("type")
        private String type = "type";

        public String getType() {
            return type;
        }

    }
}
