package com.matt.hacking_gravity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.matt.hacking_gravity.MainActivity;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_Sociallogin;
import com.matt.hacking_gravity.model.Gson_regi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String strusername = "", stremail = "", strpass = "", strtoken = "";
    private EditText edtEmail, edtName, edtPass;
    private ImageView imgClose;
    private CheckBox chkSend;
    private RelativeLayout relSign;
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private ClassRetrofit classRetrofit;
    private MyProgress myProgress;
    private Context context;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String fb_name = "", fb_id = "", fb_gmail = "", fb_sur_name = "", fb_profile = "";

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.imgBack_w),
                getResources().getDimensionPixelSize(R.dimen.imgBack_h)
        );
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.imgBack_marLeft), myPref.getstatusHeight(), 0, 0);
        imgClose.setLayoutParams(params);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = RegistrationActivity.this;
        checkNetwork = new CheckNetwork(context);
        myPref = new MyPref(context);
        classRetrofit = new ClassRetrofit(context);
        myProgress = new MyProgress(context);
        findid();
    }

    private void findid() {
        imgClose = findViewById(R.id.imgClose);
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPass = findViewById(R.id.edtPass);
        chkSend = findViewById(R.id.chkSend);
        relSign = findViewById(R.id.relSign);
        setMarginTop();
        imgClose.setOnClickListener(RegistrationActivity.this);
        relSign.setOnClickListener(RegistrationActivity.this);

        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
//        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                finish();
                break;
            case R.id.relSign:
                setSignUp();
                break;
            default:
                break;
        }
    }

    private void setSignUp() {
        strusername = edtName.getText().toString();
        stremail = edtEmail.getText().toString();
        strpass = edtPass.getText().toString();
        strtoken = myPref.getMytoken();

        String succ = checkvalidation();
        if (succ.equalsIgnoreCase("true")) {

            if (checkNetwork.NetworkAvailable()) {
                myProgress.showLoading("Wait...");
                new regconnection().execute();
            } else {
                Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class regconnection extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            service = classRetrofit.myresponse(Apiclass.url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            service.getRegistration(strusername, stremail, strpass, strtoken, "0").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    myProgress.hideLoading();
                    if (response.isSuccessful()) {

                        Gson gson = new Gson();
                        Gson_regi regi = gson.fromJson(myres, Gson_regi.class);

                        if (regi != null) {
                            if (regi.getStatus().equalsIgnoreCase("true")) {
                                myPref.setLogin(true);
                                if (regi.getAlldata() != null) {
                                    Log.e("regi-auth", "-" + regi.getAlldata().getAuth_token());

                                    myPref.setId(regi.getAlldata().getId());
                                    myPref.setName(regi.getAlldata().getName());
                                    myPref.setUsername(regi.getAlldata().getUsername());
                                    myPref.setEmail(regi.getAlldata().getEmail());
                                    myPref.setSubscription(regi.getAlldata().getSubscription());
                                    myPref.setAuth_token(regi.getAlldata().getAuth_token());
                                    myPref.setDevice_token(regi.getAlldata().getDevice_token());

                                }

                                if (regi.getAlldata().getSubdata() != null) {
                                    myPref.setUser_id(regi.getAlldata().getSubdata().getUser_id());
                                    myPref.setSubscription_type(regi.getAlldata().getSubdata().getSubscription_type());
                                    myPref.setStart_date(regi.getAlldata().getSubdata().getStart_date());
                                    myPref.setEnd_date(regi.getAlldata().getSubdata().getEnd_date());
                                }

                                Toast.makeText(context, "SuccessFully Registration ", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);

//                                finish();
                            } else {
                                ArrayList<String> errortoast = regi.getTheerror().email;
                                Toast.makeText(context, "" + errortoast.get(0), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    myProgress.hideLoading();
                }
            });
        }

    }

    private String checkvalidation() {
        String succ = "false";
        if (strusername.isEmpty() || strusername.length() < 3) {
            edtName.setError("Username must have 3 character!");
        } else if (!stremail.matches(emailPattern)) {
            edtEmail.setError("Enter valid gmail!");
        } else if (strpass.isEmpty() || strpass.length() < 3) {
            edtPass.setError("Password must have 3 character long!");
        } else {
            succ = "true";
        }

        return succ;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
//            accessToken = loginResult.getAccessToken().getToken();

            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {

                    try {
                        fb_name = object.getString("first_name");
                        fb_sur_name = object.getString("last_name");
                        if (object.has("email")) {
                            fb_gmail = object.getString("email");
                        } else {
                            fb_gmail = "";
                        }
                        fb_id = object.getString("id");
                        strtoken = myPref.getMytoken();
                        myProgress.showLoading("Wait...");
                        new sociallogconn().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name , email");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onError(FacebookException error) {
        }
    };

    public class sociallogconn extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service = classRetrofit.myresponse(Apiclass.url);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            service.getSocialLogin(fb_name, fb_gmail, "Facebook", strtoken, "0","",fb_id).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    myProgress.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_Sociallogin sociallogin = gson.fromJson(myres, Gson_Sociallogin.class);
                        if (sociallogin.getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "" + sociallogin.getMessage(), Toast.LENGTH_SHORT).show();
                            myPref.setLogin(true);
                            if (sociallogin.getAlldata() != null) {
                                myPref.setId(sociallogin.getAlldata().getId());
                                myPref.setName(sociallogin.getAlldata().getName());
                                myPref.setUsername(sociallogin.getAlldata().getUsername());
                                myPref.setEmail(sociallogin.getAlldata().getEmail());
                                myPref.setSubscription(sociallogin.getAlldata().getSubscription());
                                myPref.setAuth_token(sociallogin.getAlldata().getAuth_token());
                                myPref.setDevice_token(sociallogin.getAlldata().getDevice_token());
                            }
                            finishAffinity();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
//                            finish();
                        } else {

                            if (sociallogin.getErrorClass() != null) {
                                if (sociallogin.getErrorClass().getEmail().size() > 0) {
                                    String error = sociallogin.getErrorClass().getEmail().get(0);
                                    Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                                }
                            }
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