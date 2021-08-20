package com.matt.hacking_gravity.settingpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.GsonPrivacy;
import com.matt.hacking_gravity.model.Gson_forgot;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyActivity extends AppCompatActivity {
    private ClassRetrofit classRetrofit;
    private Context context;
    private MyProgress myProgress;
    private WebView myWeb;
    private ImageView imgBack;
    private MyPref myPref;
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
        setContentView(R.layout.activity_privacy);
        context= PrivacyActivity.this;
        myPref=new MyPref(context);
        classRetrofit = new ClassRetrofit(context);
        myProgress=new MyProgress(context);
        myProgress.showLoading("Wait...");
        myWeb=findViewById(R.id.myWeb);
        imgBack=findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        relHeader=findViewById(R.id.relHeader);
        setMarginTop();
        setData();
        new PageConnection().execute();
    }

    public void setData(){
        WebSettings webSettings = myWeb.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private class PageConnection extends AsyncTask<Void, Void, Void> {
        Apiclass service;
        @Override
        protected Void doInBackground(Void... voids) {
            service = classRetrofit.myresponse(Apiclass.url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            service.getpage("privacy").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    myProgress.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        GsonPrivacy gsonPriv = gson.fromJson(myres, GsonPrivacy.class);

                        if (gsonPriv.getStatus().equalsIgnoreCase("true")) {
                            if(gsonPriv.getData().size()>0) {
                                String unencodedHtml = "" + gsonPriv.getData().get(0).getContent();
                                String encodedHtml = Base64.encodeToString(unencodedHtml.getBytes(), Base64.NO_PADDING);
                                myWeb.loadData(encodedHtml, "text/html", "base64");
                            }
                        } else {
                            Toast.makeText(context, "" + gsonPriv.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    myProgress.hideLoading();
                }
            });
        }
    }
}