package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Course_Model {
    @SerializedName("id")
    private String id="id";
    @SerializedName("category_id")
    private String category="category_id";
    @SerializedName("name")
    private String name="name";
    @SerializedName("description")
    private String description="description";
    @SerializedName("image")
    private String image="image";
    @SerializedName("status")
    private String status="status";

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
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

    public String getStatus() {
        return status;
    }

    public ArrayList<Classes_Model> getClasses() {
        return classes;
    }

    @SerializedName("classes")
    private ArrayList<Classes_Model> classes=new ArrayList<>();
}
