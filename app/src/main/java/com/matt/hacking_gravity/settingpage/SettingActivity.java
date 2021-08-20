package com.matt.hacking_gravity.settingpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.StartActivity;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_getsetting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private MyPref myPref;

    private ImageView imgBack;
    private RelativeLayout relreminders, relPosVibe, relFeedback, relTerm, relPrivacy, relSubs;
    private TextView TvLogout;

    private String auth_token, uid;
    private ClassRetrofit classRetrofit;
    private MyProgress mProgressDialog;
    private CheckNetwork checkNetwork;
    private Context context;
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
        setContentView(R.layout.activity_setting);
        context = SettingActivity.this;

        myPref = new MyPref(context);
        relHeader=findViewById(R.id.relHeader);
        setMarginTop();
        checkNetwork = new CheckNetwork(context);
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog = new MyProgress(context);
        auth_token = myPref.getAuth_token();
        uid = myPref.getId();
        findid();

        if (checkNetwork.NetworkAvailable()) {
            mProgressDialog.showLoading("Wait...");
            new getsetting().execute();
        } else {
            Toast.makeText(context, "Please Check Your Network", Toast.LENGTH_SHORT).show();
        }
    }

    private void findid() {
        imgBack = findViewById(R.id.imgBack);
        relreminders = findViewById(R.id.relreminders);
        relPosVibe = findViewById(R.id.relPosVibe);
        relFeedback = findViewById(R.id.relFeedback);
        relTerm = findViewById(R.id.relTerm);
        relPrivacy = findViewById(R.id.relPrivacy);
        relSubs = findViewById(R.id.relSubs);
        TvLogout = findViewById(R.id.TvLogout);
        relSubs.setOnClickListener(SettingActivity.this);
        relPrivacy.setOnClickListener(SettingActivity.this);
        relTerm.setOnClickListener(SettingActivity.this);
        relFeedback.setOnClickListener(SettingActivity.this);
        relPosVibe.setOnClickListener(SettingActivity.this);
        relreminders.setOnClickListener(SettingActivity.this);
        imgBack.setOnClickListener(SettingActivity.this);
        TvLogout.setOnClickListener(SettingActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;
            case R.id.relreminders:
                startActivity(new Intent(this, ReminderActivity.class));
                break;
            case R.id.relPosVibe:
                startActivity(new Intent(this, VibeNotificationActivity.class));
                break;
            case R.id.relFeedback:
                sendFeedback();
                break;
            case R.id.relTerm:
                startActivity(new Intent(this, TermConditionActivity.class));
                break;
            case R.id.relPrivacy:
                startActivity(new Intent(this, PrivacyActivity.class));
                break;
            case R.id.relSubs:
                startActivity(new Intent(this, SubscriptionActivity.class));
                break;
            case R.id.TvLogout:
                myPref.logout();
                finishAffinity();
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                /* finish();*/
                break;
            default:
                break;
        }
    }

    private void sendFeedback() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        String aEmailList[] = {getResources().getString(R.string.feedbackmail)};
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
        String feedback_msg = getString(R.string.feedback_sub);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Write Your Feedback here");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_sub));

        PackageManager packageManager = getPackageManager();
        boolean isIntentSafe = emailIntent.resolveActivity(packageManager) != null;
        if (isIntentSafe) {
            startActivity(emailIntent);
        } else {
            Toast.makeText(this, R.string.email_app_not_installed, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (checkNetwork.NetworkAvailable()) {
//            mProgressDialog.showLoading("Wait...");
//            new getsetting().execute();
//        } else {
//            Toast.makeText(context, "Please Check Your Network", Toast.LENGTH_SHORT).show();
//        }
    }

    public class getsetting extends AsyncTask<Void, Void, Void> {
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

            service.getusersetting(uid).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_getsetting getsetting = gson.fromJson(myres, Gson_getsetting.class);
                        if (getsetting.getStatus().equalsIgnoreCase("true")) {
                            if (getsetting.getAlldata() != null) {
                                myPref.setId(getsetting.getAlldata().getUser_id());
                                myPref.setReminder(getsetting.getAlldata().getReminder());
                                myPref.setDay(getsetting.getAlldata().getDay());
                                myPref.setTime(getsetting.getAlldata().getTime());
                                myPref.setNotification(getsetting.getAlldata().getNotification());
                            }
                        }
//                        else {
//                            Toast.makeText(context, "" + getsetting.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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