package com.matt.hacking_gravity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.matt.hacking_gravity.fragments.PositiveFragment;
import com.matt.hacking_gravity.fragments.PracticeFragment;
import com.matt.hacking_gravity.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linPrac,linPosiVibe,linProfile;
    private ImageView imgPrac,imgPosi,imgProfile;

    Bundle bundle;
    boolean isNoti=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle=getIntent().getExtras();
        if(bundle!=null){
            if(bundle.containsKey("Data")){
                isNoti=true;
            }
        }
        findid();
    }

    private void findid(){
        linPrac=findViewById(R.id.linPrac);
        linPosiVibe=findViewById(R.id.linPosiVibe);
        linProfile=findViewById(R.id.linProfile);

        imgPrac=findViewById(R.id.imgPrac);
        imgPosi=findViewById(R.id.imgPosi);
        imgProfile=findViewById(R.id.imgProfile);
        linPrac.setOnClickListener(MainActivity.this);
        linPosiVibe.setOnClickListener(MainActivity.this);
        linProfile.setOnClickListener(MainActivity.this);


        //default Page
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if(isNoti) {
            unSelected();
            imgPosi.setImageResource(R.drawable.message_select);
            isNoti=false;
            transaction.replace(R.id.main_container, new PositiveFragment());
        }else{
            transaction.replace(R.id.main_container, new PracticeFragment());
        }
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        unSelected();
        Fragment fragment=null;
        switch (v.getId()){
            case R.id.linPrac:

                fragment=new PracticeFragment();
                imgPrac.setImageResource(R.drawable.home_select);
                break;
            case R.id.linPosiVibe:
                fragment=new PositiveFragment();
                imgPosi.setImageResource(R.drawable.message_select);
                break;
            case R.id.linProfile:
                fragment=new ProfileFragment();
                imgProfile.setImageResource(R.drawable.profile_select);
                break;

            default:
                break;
        }

        if(fragment!=null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.main_container, fragment);
            transaction.commit();
        }
    }

    private void unSelected(){
        imgPrac.setImageResource(R.drawable.home_unselect);
        imgPosi.setImageResource(R.drawable.message_unselect);
        imgProfile.setImageResource(R.drawable.profile_unselect);
    }



}