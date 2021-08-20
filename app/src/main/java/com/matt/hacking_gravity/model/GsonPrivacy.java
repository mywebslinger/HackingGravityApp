package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GsonPrivacy {
    @SerializedName("status")
    public String status = "status";
    @SerializedName("message")
    public String message = "message";

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<GsonTerm.data> getData() {
        return data;
    }

    @SerializedName("data")
    public ArrayList<GsonTerm.data> data = new ArrayList<>();


    public class data {
        @SerializedName("id")
        public String id = "id";
        @SerializedName("title")
        public String title = "title";
        @SerializedName("slug")
        public String slug = "slug";

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getSlug() {
            return slug;
        }

        public String getContent() {
            return content;
        }

        @SerializedName("content")
        public String content = "content";
    }
}
