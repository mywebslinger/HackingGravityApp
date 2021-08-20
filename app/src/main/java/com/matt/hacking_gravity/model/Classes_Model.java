package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

public class Classes_Model {
    @SerializedName("id")
    private String id="id";
    @SerializedName("category_id")
    private String category_id="category_id";
    @SerializedName("course_id")
    private String course_id="course_id";
    @SerializedName("name")
    private String name="name";
    @SerializedName("description")
    private String description="description";
    @SerializedName("image")
    private String image="image";
    @SerializedName("video")
    private String video="video";
    @SerializedName("is_free")
    private String is_free="is_free";

    public String getId() {
        return id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getIs_free() {
        return is_free;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("status")
    private String status="status";

}
