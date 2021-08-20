package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Gson_category {

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
    private ArrayList<datares> datacate = new ArrayList<>();

    public  class datares {

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

        @SerializedName("image")
        private String image = "image";

        public String getImage() {
            return image;
        }

        public String getStatus() {
            return status;
        }

        @SerializedName("status")
        private String status="status";

        public ArrayList<Classes_Model> getClasses() {
            return classes;
        }

        public ArrayList<Course_Model> getCourse() {
            return course;
        }

        @SerializedName("classes")
        private ArrayList<Classes_Model> classes=new ArrayList<>();

        @SerializedName("course")
        private ArrayList<Course_Model> course=new ArrayList<>();

    }
}
