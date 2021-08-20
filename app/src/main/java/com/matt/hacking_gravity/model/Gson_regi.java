package com.matt.hacking_gravity.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Gson_regi {

    @SerializedName("status")
    private String status = "status";

    public String getStatus() {
        return status;
    }

    @SerializedName("errors")
    errordata theerror;

    public errordata getTheerror() {
        return theerror;
    }

    public final class errordata {
        @SerializedName("email")
        public ArrayList<String> email;
    }

    @SerializedName("data")
    CommonData alldata;

    public CommonData getAlldata() {
        return alldata;
    }

}
