package com.matt.hacking_gravity.settingpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.GsonTerm;
import com.matt.hacking_gravity.model.SubscribeData;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubscriptionActivity extends AppCompatActivity {
    private ImageView imgBack;
    private ClassRetrofit classRetrofit;
    private Context context;
    private MyProgress myProgress;
    private MyPref myPref;
    private RelativeLayout relHeader;
    private String auth_token, userid, subType;

    private TextView tvSubstatuc, tvSub, tvExpiring, tvWillRe, tvLatest;

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
        setContentView(R.layout.activity_subscription);

        context = SubscriptionActivity.this;

        tvSubstatuc = findViewById(R.id.tvSubstatuc);
        tvSub = findViewById(R.id.tvSub);
        tvExpiring = findViewById(R.id.tvExpiring);
        tvWillRe = findViewById(R.id.tvWillRe);
        tvLatest = findViewById(R.id.tvLatest);

        classRetrofit = new ClassRetrofit(context);
        myProgress = new MyProgress(context);
        myPref = new MyPref(context);
        relHeader = findViewById(R.id.relHeader);
        userid = myPref.getId();
        auth_token = myPref.getAuth_token();
        setMarginTop();
        findid();

        if (myPref.getSubscription().equalsIgnoreCase("1") && !myPref.getSubscription_type().equalsIgnoreCase("")) {
            tvSubstatuc.setText("Subscribed");
            tvSubstatuc.setTextColor(getResources().getColor(R.color.black));
            String subs = "";
            if (myPref.getSubscription_type().equalsIgnoreCase("1")) {
                subs = "Monthly";
            } else if (myPref.getSubscription_type().equalsIgnoreCase("2")) {
                subs = "Yearly";
            } else if (myPref.getSubscription_type().equalsIgnoreCase("3")) {
                subs = "Life time";
            }
            tvSub.setText(subs);

            try {
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM dd, yyyy");
                if (myPref.getStart_date() != null) {
                    if (!myPref.getStart_date().equalsIgnoreCase("")) {
                        Date date = fmt.parse(myPref.getStart_date());
                        String dates = fmtOut.format(date);
                        tvLatest.setText("" + dates);
                    }
                }

                if (myPref.getEnd_date() != null) {
                    if (!myPref.getEnd_date().equalsIgnoreCase("")) {
                        Date date = fmt.parse(myPref.getEnd_date());
                        String dates = fmtOut.format(date);
                        tvExpiring.setText("" + dates);
                        tvWillRe.setText(""+dates);
                    }
                }

            }catch (Exception e){

            }

        }


    }

    public void findid() {
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}