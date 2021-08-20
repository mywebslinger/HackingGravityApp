package com.matt.hacking_gravity.common;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPref {
    Context context;
    SharedPreferences sharedPreferences;
    String mytoken = "mytoken";
    String id = "id";
    String name = "name";
    String username = "username";
    String email = "email";
    String subscription = "subscription";
    String auth_token = "auth_token";
    String device_token = "device_token";
//    String subscribe = "subscribe";
    String user_id = "user_id";
    String subscription_type = "subscription_type";
    String start_date = "start_date";
    String end_date = "end_date";
    String isLogin = "isLogin";
    String statusHeight = "statusHeight";
    String reminder = "reminder";
    String day = "day";
    String time = "time";
    String notification = "notification";
    String mydefaulttime = "mydefaulttime";


    public void setShowVideoTrack(String Names,int value) {
        sharedPreferences.edit().putInt(Names, value).commit();
    }

    public int getShowVideoTrack(String Names) {
        return sharedPreferences.getInt(Names, -1);
    }

    public void setstatusHeight(int value) {
        sharedPreferences.edit().putInt(statusHeight, value).commit();
    }

    public int getstatusHeight() {
        return sharedPreferences.getInt(statusHeight, 30);
    }


    public MyPref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public boolean getLogin() {
        return sharedPreferences.getBoolean(isLogin, false);
    }

    public void setLogin(Boolean value) {
        sharedPreferences.edit().putBoolean(isLogin, value).commit();
    }

    public void setMytoken(String value) {
        sharedPreferences.edit().putString(mytoken, value).commit();
    }

    public String getMytoken() {
        return sharedPreferences.getString(mytoken, "");
    }

    public void setId(String value) {
        sharedPreferences.edit().putString(id, value).commit();
    }

    public String getId() {
        return sharedPreferences.getString(id, "");
    }

    public void setName(String value) {
        sharedPreferences.edit().putString(name, value).commit();
    }

    public String getName() {
        return sharedPreferences.getString(name, "");
    }

    public void setUsername(String value) {
        sharedPreferences.edit().putString(username, value).commit();
    }

    public String getUsername() {
        return sharedPreferences.getString(username, "");
    }

    public void setEmail(String value) {
        sharedPreferences.edit().putString(email, value).commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(email, "");
    }

    public void setSubscription(String value) {
        sharedPreferences.edit().putString(subscription, value).commit();
    }

    public String getSubscription() {
        return sharedPreferences.getString(subscription, "");
    }

    public void setAuth_token(String value) {
        sharedPreferences.edit().putString(auth_token, value).commit();
    }

    public String getAuth_token() {
        return sharedPreferences.getString(auth_token, "");
    }

    public void setDevice_token(String value) {
        sharedPreferences.edit().putString(device_token, value).commit();
    }

    public String getDevice_token() {
        return sharedPreferences.getString(device_token, "");
    }

  /*  public void setSubscribe(String value) {
        sharedPreferences.edit().putString(subscribe, value).commit();
    }

    public String getSubscribe() {
        return sharedPreferences.getString(subscribe, "");
    }*/

    public void setUser_id(String value) {
        sharedPreferences.edit().putString(user_id, value).commit();
    }

    public String getUser_id() {
        return sharedPreferences.getString(user_id, "");
    }

    public void setSubscription_type(String value) {
        sharedPreferences.edit().putString(subscription_type, value).commit();
    }

    public String getSubscription_type() {
        return sharedPreferences.getString(subscription_type, "");
    }

    public void setStart_date(String value) {
        sharedPreferences.edit().putString(start_date, value).commit();
    }

    public String getStart_date() {
        return sharedPreferences.getString(start_date, "");
    }

    public void setEnd_date(String value) {
        sharedPreferences.edit().putString(end_date, value).commit();
    }

    public String getEnd_date() {
        return sharedPreferences.getString(end_date, "");
    }

    public void logout() {
        setLogin(false);
        setUser_id("");
    }


    public void setMydefaulttime(String value) {
        sharedPreferences.edit().putString(mydefaulttime, value).commit();
    }

    public String getMydefaulttime() {
        return sharedPreferences.getString(mydefaulttime, "");
    }

    public void setReminder(String value) {
        sharedPreferences.edit().putString(reminder, value).commit();
    }

    public String getReminder() {
        return sharedPreferences.getString(reminder, "");
    }

    public void setDay(String value) {
        sharedPreferences.edit().putString(day, value).commit();
    }

    public String getDay() {
        return sharedPreferences.getString(day, "");
    }

    public void setTime(String value) {
        sharedPreferences.edit().putString(time, value).commit();
    }

    public String getTime() {
        return sharedPreferences.getString(time, "");
    }

    public void setNotification(String value) {
        sharedPreferences.edit().putString(notification, value).commit();
    }

    public String getNotification() {
        return sharedPreferences.getString(notification, "0");
    }


}
