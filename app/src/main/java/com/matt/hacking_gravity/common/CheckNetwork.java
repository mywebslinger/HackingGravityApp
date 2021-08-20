package com.matt.hacking_gravity.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckNetwork {
    Context context;
    String abab;
    ConnectivityManager connectivityManager;
    boolean connected = false;

    public CheckNetwork(Context context) {
        this.context = context;
    }

    public boolean NetworkAvailable() {
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = false;
            if (networkInfo != null) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                    if (networkInfo.isConnectedOrConnecting())
                        connected = true;
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                    if (networkInfo.isConnectedOrConnecting())
                        connected = true;
            } else {
                connected = false;
            }
            return connected;
        } catch (Exception e) {
        }
        return connected;
    }
}
