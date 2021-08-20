package com.matt.hacking_gravity.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matt.hacking_gravity.InAppActivity;
import com.matt.hacking_gravity.MainActivity;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.adapters.CategoryAdapter;
import com.matt.hacking_gravity.classcourse.ClassesActivity;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.common.RecyclerItemClickListener;
import com.matt.hacking_gravity.model.Cat_Model;
import com.matt.hacking_gravity.model.Gson_category;
import com.matt.hacking_gravity.settingpage.SettingActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PracticeFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private ClassRetrofit classRetrofit;
    private String auth_token, userid;
    private static ArrayList<Cat_Model> categorylist = new ArrayList<>();
    private RecyclerView rec_list;
    private MyProgress mProgressDialog;
    private TextView tvUnlock;
    static Gson_category category=null;
    LinearLayout linHeader;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelSize(R.dimen.header_frag)
        );
        params.setMargins(0, myPref.getstatusHeight(), 0, 0);
        linHeader.setLayoutParams(params);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice, container, false);
        context = getActivity();
        checkNetwork = new CheckNetwork(context);
        myPref = new MyPref(context);
        linHeader=view.findViewById(R.id.linHeader);
        setMarginTop();
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog=new MyProgress(context);
        rec_list = view.findViewById(R.id.reccat_list);
        tvUnlock=view.findViewById(R.id.tvUnlock);
        tvUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(context, InAppActivity.class);
                startActivity(in);
            }
        });
        rec_list.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(category!=null) {

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(category.getDatacate().get(position));

                    Intent in = new Intent(getActivity(), ClassesActivity.class);
                    in.putExtra("data", jsonString);
                    startActivity(in);
                }

            }
        }));

        userid = myPref.getId();
        auth_token = myPref.getAuth_token();

        if (categorylist.size() == 0) {
            if (checkNetwork.NetworkAvailable()) {
                mProgressDialog.showLoading("Please Wait....");
                new cateconn().execute();
            } else {
                Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            insertdata();
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSetting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            default:
                break;
        }
    }
    private void insertdata() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rec_list.setLayoutManager(linearLayoutManager);
        CategoryAdapter adapter_recyclerview = new CategoryAdapter(context, categorylist);
        rec_list.setAdapter(adapter_recyclerview);
    }


    public class cateconn extends AsyncTask<Void, Void, Void> {
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
            service.getcategory(userid).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                         category = gson.fromJson(myres, Gson_category.class);
                        if (category.getStatus().equalsIgnoreCase("true")) {
                            for (int i = 0; i < category.getDatacate().size(); i++) {
                                Cat_Model cat_model = new Cat_Model();
                                cat_model.setClassname(category.getDatacate().get(i).getName());
                                cat_model.setClassimg(category.getDatacate().get(i).getImage());
                                categorylist.add(cat_model);
                            }
                        } else {
                            Toast.makeText(context, "" + category.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                    insertdata();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mProgressDialog.hideLoading();
                    Log.e("fail", "onFailure: " + t.getMessage());
                }
            });

        }
    }

}