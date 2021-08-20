package com.matt.hacking_gravity.settingpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;
import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_Remi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {
    private PickerUI mPickerUI;
    private int currentPosition = -1, mHour, mMinute;
    private TextView tvDay, tvTime;
    private SwitchCompat myswitch;
    private ImageView imgBack;

    private String strdays = "2", strtime = "12:00", u_id = "", reminder = "", notify = "";
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private Context context;
    private String auth_token;
    private ClassRetrofit classRetrofit;
    private MyProgress mProgressDialog;
    private RelativeLayout relHeader;
//    private boolean isremind = false, isday = false, istime = false;

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
        setContentView(R.layout.activity_reminder);
        context = ReminderActivity.this;
        myPref = new MyPref(context);
        relHeader=findViewById(R.id.relHeader);
        setMarginTop();
        reminder = myPref.getReminder();
        strdays = myPref.getDay();

        if (!myPref.getTime().equalsIgnoreCase("")) {
            try {
                SimpleDateFormat sm = new SimpleDateFormat("HH:mm");
                SimpleDateFormat nsm = new SimpleDateFormat("hh:mm a");
                Date mydate = sm.parse(myPref.getTime());
//                strtime = myPref.getTime();
                strtime = nsm.format(mydate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        checkNetwork = new CheckNetwork(context);
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog = new MyProgress(context);
        u_id = myPref.getId();
        auth_token = myPref.getAuth_token();
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
        tvDay = findViewById(R.id.tvDay);
        tvTime = findViewById(R.id.tvTime);

        mPickerUI = (PickerUI) findViewById(R.id.picker_ui_view);
        myswitch = findViewById(R.id.myswitch);

        if (!myPref.getReminder().equalsIgnoreCase("")) {
            if (reminder.equalsIgnoreCase("1")) {
                myswitch.setChecked(true);
            } else {
                myswitch.setChecked(false);
            }
        }

        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    reminder = "1";
                } else {
                    reminder = "0";
                }

                toconn();
                setRemSetting();
            }
        });
        setRemSetting();

        tvDay.setOnClickListener(ReminderActivity.this);
        tvTime.setOnClickListener(ReminderActivity.this);
        List<String> options = new ArrayList<>();
        options.add("ON WEEKENDS");
        options.add("ON WEEKDAYS");
        options.add("EVERYDAY");

        if (!myPref.getDay().equalsIgnoreCase("")) {
            if (strdays.equalsIgnoreCase("1")) {
                tvDay.setText("ON WEEKENDS");
            } else if (strdays.equalsIgnoreCase("2")) {
                tvDay.setText("ON WEEKDAYS");
            } else if (strdays.equalsIgnoreCase("3")) {
                tvDay.setText("EVERYDAY");
            }
        }
        if (!myPref.getNotification().equalsIgnoreCase("")) {
            notify = myPref.getNotification();
        } else {
            notify = "0";
        }

        if (!myPref.getTime().equalsIgnoreCase("")) {
            tvTime.setText(strtime);
        }

        PickerUISettings pickerUISettings = new PickerUISettings.Builder().withItems(options).withColorTextCenter(R.color.background_picker).withColorTextNoCenter(R.color.background_picker).withLinesColor(R.color.background_picker).withAutoDismiss(true).withItemsClickables(false).withUseBlur(false).build();
        mPickerUI.setSettings(pickerUISettings);
        mPickerUI.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                currentPosition = position;
                tvDay.setText("" + valueResult);
                if (currentPosition == 0) {
                    strdays = "1";
                } else if (currentPosition == 1) {
                    strdays = "2";
                } else if (currentPosition == 2) {
                    strdays = "3";
                }
                if (myswitch.isChecked()) {
                    toconn();
                }
            }
        });
    }

    private void toconn() {
//        if (isremind && isday && istime) {
        if (checkNetwork.NetworkAvailable()) {

            mProgressDialog.showLoading("Please Wait....");
            new setsetting().execute();
        } else {
            Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }
//        }
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
                    mProgressDialog.hideLoading();
                    if(response.body()!=null) {
                        String myres = response.body().toString();

                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            Gson_Remi remi = gson.fromJson(myres, Gson_Remi.class);
                            if (remi.getStatus().equalsIgnoreCase("true")) {
                                myPref.setMydefaulttime(strtime);
                                myPref.setTime(strtime);
                                myPref.setReminder(reminder);
                                myPref.setDay(strdays);
                            } else {
//                                Toast.makeText(context, "" + remi.getMessage(), Toast.LENGTH_SHORT).show();
                            }
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


    public void setRemSetting() {
        if (myswitch.isChecked()) {
            tvDay.setTextColor(getResources().getColor(R.color.blue));
            tvDay.setEnabled(true);
            tvDay.setClickable(true);

            tvTime.setTextColor(getResources().getColor(R.color.blue));
            tvTime.setEnabled(true);
            tvTime.setClickable(true);

        } else {
            tvDay.setTextColor(getResources().getColor(R.color.lightblue));
            tvDay.setEnabled(false);
            tvDay.setClickable(false);

            tvTime.setTextColor(getResources().getColor(R.color.lightblue));
            tvTime.setEnabled(false);
            tvTime.setClickable(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDay:
                if (currentPosition == -1) {
                    mPickerUI.slide();
                } else {
                    mPickerUI.slide(currentPosition);
                }
                break;
            case R.id.tvTime:
                showTimePicker();
                break;
            default:
                break;
        }
    }

    public void showTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

//                tvTime.setText(hourOfDay + ":" + minute);

                String am_pm = "";
                String mm_precede = "";
                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "AM";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "PM";



                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ? "12" : datetime.get(Calendar.HOUR) + "";

                if (strHrsToShow.trim().length()==1) {
                    strHrsToShow = "0"+strHrsToShow;
                }

                String strMin= String.valueOf(datetime.get(Calendar.MINUTE));
                if(strMin.length()==1){
                    strMin="0"+strMin;
                }
//                istime = true;
                strtime = strHrsToShow + ":" + strMin + " " + am_pm;
                tvTime.setText(""+strtime);
                if (myswitch.isChecked()) {
                    toconn();
                }

//                tvTime.setText(strHrsToShow + ": " + datetime.get(Calendar.MINUTE) + " " + am_pm);


            }
        }, mHour, mMinute, false);
        timePickerDialog.show();

    }

}