package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_Usersession {

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
    Alldata alldata;

    public Alldata getAlldata() {
        return alldata;
    }

    public static class Alldata {
        @SerializedName("day_streak")
        private String day_streak = "day_streak";

        public String getDay_streak() {
            return day_streak;
        }

        @SerializedName("total_time")
        private String total_time = "total_time";

        public String getTotal_time() {
            return total_time;
        }

        @SerializedName("total_session")
        private String total_session = "total_session";

        public String getTotal_session() {
            return total_session;
        }

        public ArrayList<SessionData> getSession() {
            return session;
        }

        @SerializedName("session")
        public ArrayList<SessionData> session=new ArrayList<>();
    }
}
