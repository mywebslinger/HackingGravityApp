package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_Daily {

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

    public ArrayList<datares> getDatacate() {
        return datacate;
    }

    @SerializedName("data")
    public ArrayList<datares> datacate = new ArrayList<>();

    public  final class datares {
        @SerializedName("id")
        public String id="id";
        @SerializedName("content")
        public String content="content";
        @SerializedName("image")
        public String image="image";
        @SerializedName("scheduled_at")
        public String scheduled_at="scheduled_at";

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getImage() {
            return image;
        }

        public String getScheduled_at() {
            return scheduled_at;
        }

        public String getStatus() {
            return status;
        }

        @SerializedName("status")
        public String status="status";

    }
}
