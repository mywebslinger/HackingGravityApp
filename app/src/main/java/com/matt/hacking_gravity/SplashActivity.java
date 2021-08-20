package com.matt.hacking_gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.MyPref;


public class SplashActivity extends AppCompatActivity {

    private Context context;
    private CheckNetwork checkNetwork;
    private int SPLASH_TIME_OUT = 3000;
    private MyPref myPref;

    public void getStatusHight() {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                int statusBarHeight = 0;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                }
/*
                int dphei=(int)(statusBarHeight / Resources.getSystem().getDisplayMetrics().density);

                Resources r = getResources();
                int px = (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        statusBarHeight,
                        r.getDisplayMetrics()
                );


                int dps=(int)TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX,
                        px,
                        r.getDisplayMetrics()
                );

                Log.e("Status Height", "-" + statusBarHeight+" - "+dphei+" - "+dps);*/

              /*  Rect rectangle = new Rect();
                Window window = getWindow();
                window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
                int statusBarHeight1 = rectangle.top;
                int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
                int titleBarHeight = contentViewTop - statusBarHeight1;


                Log.e("Status Height 1", "-" + statusBarHeight1);
*/
                myPref.setstatusHeight(statusBarHeight);
            } else {
                myPref.setstatusHeight(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        context = SplashActivity.this;
        myPref = new MyPref(context);
        checkNetwork = new CheckNetwork(context);

        getStatusHight();
        if (checkNetwork.NetworkAvailable()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (myPref.getLogin()) {
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(context, StartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);

        } else {
            Toast.makeText(SplashActivity.this, "network is not available", Toast.LENGTH_LONG).show();
        }
       /* try {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.e("getInstanceId failed", "" + task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    if (token != null) {
                        if (token.equalsIgnoreCase("") && token.length() > 10) {
                            myPref.setMytoken(token);
                        }
                    }
                    Log.e("SPlash_token", "-" + token);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
