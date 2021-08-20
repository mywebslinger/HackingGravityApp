package com.matt.hacking_gravity.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.adapters.CoursesAdapter;
import com.matt.hacking_gravity.adapters.SessionAdapter;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_Profile;
import com.matt.hacking_gravity.model.Gson_Usersession;
import com.matt.hacking_gravity.settingpage.SettingActivity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    private ImageView imgSetting, imgProfile;

    private Context context;
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private ClassRetrofit classRetrofit;
    private String auth_token, userid;
    private MyProgress mProgressDialog;
    private RecyclerView recjourney;
    private TextView tvUserName, tvDay, tvTime, tvSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        checkNetwork = new CheckNetwork(context);
        myPref = new MyPref(context);
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog = new MyProgress(context);
        userid = myPref.getId();
        auth_token = myPref.getAuth_token();
    }

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getActivity().getResources().getDimensionPixelSize(R.dimen.profile_header),
                getActivity().getResources().getDimensionPixelSize(R.dimen.profile_header)
        );
        params.setMargins(0, myPref.getstatusHeight(), getResources().getDimensionPixelSize(R.dimen.imgBack_marLeft), 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imgSetting.setLayoutParams(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imgSetting = view.findViewById(R.id.imgSetting);
        imgProfile = view.findViewById(R.id.imgProfile);
        imgSetting.setOnClickListener(this);
        setMarginTop();
        tvUserName = view.findViewById(R.id.tvUserName);
        recjourney=view.findViewById(R.id.recjourney);
        tvDay = view.findViewById(R.id.tvDay);
        tvTime = view.findViewById(R.id.tvTime);
        tvSession = view.findViewById(R.id.tvSession);
        if (checkNetwork.NetworkAvailable()) {
            mProgressDialog.showLoading("Please Wait....");
            new profileconn().execute();
            new usersessionconn().execute();
        } else {
            Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            default:
                break;
        }
    }

    public class usersessionconn extends AsyncTask<Void, Void, Void> {
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
            service.getusersession(userid).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    Log.e("myres","-"+myres);
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_Usersession usersession = gson.fromJson(myres, Gson_Usersession.class);
                        if (usersession.getStatus().equalsIgnoreCase("true")) {
                            tvDay.setText(usersession.getAlldata().getDay_streak() + " Days");
                            tvTime.setText(usersession.getAlldata().getTotal_time() + " Min.");
                            tvSession.setText(usersession.getAlldata().getTotal_session() + " Sessions");

                            if (usersession.getAlldata().getSession() != null) {
                                Log.e("size", "-" + usersession.getAlldata().getSession().size());
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                recjourney.setLayoutManager(linearLayoutManager);
                                SessionAdapter adapter_recyclerview = new SessionAdapter(context,usersession.getAlldata().getSession());
                                recjourney.setAdapter(adapter_recyclerview);

                            }

                        } else {
                            Toast.makeText(context, "" + usersession.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    public class profileconn extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("TAG ",Apiclass.url+" "+auth_token);
            service = classRetrofit.categoryres(Apiclass.url, auth_token);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            service.getprofile(userid).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_Profile gson_profile = gson.fromJson(myres, Gson_Profile.class);
                        if (gson_profile.getStatus().equalsIgnoreCase("true")) {

                            if (gson_profile.getAlldata() != null) {
                                tvUserName.setText(gson_profile.getAlldata().getName());
                                myPref.setAuth_token("" + gson_profile.getAlldata().getAuth_token());
                                myPref.setSubscription(gson_profile.getAlldata().getSubscription());

                                if (gson_profile.getAlldata().getImage() != null) {
                                    if (!gson_profile.getAlldata().getImage().equalsIgnoreCase("")) {
                                        Picasso.get().load(Apiclass.imgUrl+gson_profile.getAlldata().getImage()).into(imgProfile);
                                    }
                                }

                                if (gson_profile.getAlldata().getSubdata() != null) {
                                    myPref.setUser_id(gson_profile.getAlldata().getSubdata().getUser_id());
                                    myPref.setSubscription_type(gson_profile.getAlldata().getSubdata().getSubscription_type());
                                    myPref.setStart_date(gson_profile.getAlldata().getSubdata().getStart_date());
                                    myPref.setEnd_date(gson_profile.getAlldata().getSubdata().getEnd_date());
                                }

                            }
                        } else {
                            Toast.makeText(context, "" + gson_profile.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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