package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

public class Gson_getsetting {

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
        @SerializedName("id")
        private String id = "id";

        public String getId() {
            return id;
        }

        @SerializedName("user_id")
        private String user_id = "user_id";

        public String getUser_id() {
            return user_id;
        }

        @SerializedName("reminder")
        private String reminder = "reminder";

        public String getReminder() {
            return reminder;
        }

        @SerializedName("day")
        private String day = "day";

        public String getDay() {
            return day;
        }

        @SerializedName("time")
        private String time = "time";

        public String getTime() {
            return time;
        }

        @SerializedName("notification")
        private String notification = "notification";

        public String getNotification() {
            return notification;
        }
    }

}
