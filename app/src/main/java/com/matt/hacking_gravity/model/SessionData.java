package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

public class SessionData {
    @SerializedName("id")
    public String id="id";
    @SerializedName("user_id")
    public String user_id="user_id";
    @SerializedName("class_id")
    public String class_id="class_id";
    @SerializedName("date")
    public String date="date";
    @SerializedName("duration")
    public String duration="duration";
    @SerializedName("class_name")
    public String class_name="class_name";

    public String getId() {
        return id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public String getClass_name() {
        return class_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    @SerializedName("course_name")
    public String course_name="course_name";
}
