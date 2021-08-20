package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Gson_login {

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
    CommonData alldata;

    public CommonData getAlldata() {
        return alldata;
    }

/*    public  class Alldata {
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

        @SerializedName("username")
        private String username = "username";

        public String getUsername() {
            return username;
        }

        @SerializedName("email")
        private String email = "email";

        public String getEmail() {
            return email;
        }

        @SerializedName("subscription")
        private String subscription = "subscription";

        public String getSubscription() {
            return subscription;
        }

        @SerializedName("auth_token")
        private String auth_token = "auth_token";

        public String getAuth_token() {
            return auth_token;
        }

        @SerializedName("device_token")
        private String device_token = "device_token";

        public String getDevice_token() {
            return device_token;
        }

        @SerializedName("device_type")
        private String device_type = "device_type";

        public String getDevice_type() {
            return device_type;
        }

        @SerializedName("subscribe")
        SubsComonData subdata;

        public SubsComonData getSubdata() {
            return subdata;
        }
    }*/
}
