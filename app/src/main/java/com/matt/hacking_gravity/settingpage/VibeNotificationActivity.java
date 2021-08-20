package com.matt.hacking_gravity.settingpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_Remi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VibeNotificationActivity extends AppCompatActivity {
    private SwitchCompat myswitch;
    private ImageView imgBack;

    private String strdays = "", strtime = "", u_id, reminder, notify = "0";
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private Context context;
    private String auth_token;
    private ClassRetrofit classRetrofit;
    private MyProgress mProgressDialog;
    private RelativeLayout relHeader;

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.header_height)
        );
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.imgBack_marLeft), myPref.getstatusHeight(), 0, 0);
        relHeader.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibe_notification);
        context = VibeNotificationActivity.this;
        myPref = new MyPref(context);
        checkNetwork = new CheckNetwork(context);
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog = new MyProgress(context);
        u_id = myPref.getId();
        auth_token = myPref.getAuth_token();
        relHeader=findViewById(R.id.relHeader);
        setMarginTop();
        findid();
    }

    public void findid() {
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myswitch = findViewById(R.id.myswitch);

        if (!myPref.getNotification().equalsIgnoreCase("")) {
            notify = myPref.getNotification();
            if (notify.equalsIgnoreCase("1")) {
                myswitch.setChecked(true);
            } else {
                myswitch.setChecked(false);
            }
        }

        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (myswitch.isChecked()) {
                    notify = "1";
                } else {
                    notify = "0";
                }

                if (checkNetwork.NetworkAvailable()) {
                    mProgressDialog.showLoading("Please Wait....");
                    new setsetting().execute();
                } else {
                    Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (!myPref.getReminder().equalsIgnoreCase("")) {
            reminder = myPref.getReminder();
        } else {
            reminder = "0";
        }
        if (!myPref.getDay().equalsIgnoreCase("")) {
            strdays = myPref.getDay();
        } else {
            strdays = "1";
        }
        if (!myPref.getMydefaulttime().equalsIgnoreCase("")) {
            strtime = myPref.getMydefaulttime();
        } else {
            strtime = "08:00 AM";
        }
    }

    public class setsetting extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service = classRetrofit.categoryres(Apiclass.url, auth_token);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            service.postusersetting(u_id, reminder, strdays, strtime, notify).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_Remi remi = gson.fromJson(myres, Gson_Remi.class);
                        if (remi.getStatus().equalsIgnoreCase("true")) {
                            myPref.setNotification(notify);
                        } else {
                            Toast.makeText(context, "" + remi.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mProgressDialog.hideLoading();
                }
            });
        }
    }
}