package com.matt.hacking_gravity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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
import com.matt.hacking_gravity.model.Gson_login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String stremail = "", strpass = "", strtoken = "";
    private TextView tvForgot;
    private ImageView imgClose;
    private EditText edtEmail, edtPass;
    private RelativeLayout relloginbutton;
    private Context context;
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private ClassRetrofit classRetrofit;
    private MyProgress myProgress;

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
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
//        FacebookSdk.sdkInitialize(context);
//        AppEventsLogger.activateApp(this);

        checkNetwork = new CheckNetwork(context);
        myPref = new MyPref(context);
        classRetrofit = new ClassRetrofit(context);
        myProgress = new MyProgress(context);
        findid();
    }

    private void findid() {
        tvForgot = findViewById(R.id.tvForgot);
        imgClose = findViewById(R.id.imgClose);
        setMarginTop();
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        relloginbutton = findViewById(R.id.relloginbutton);
        tvForgot.setOnClickListener(LoginActivity.this);
        imgClose.setOnClickListener(LoginActivity.this);
        relloginbutton.setOnClickListener(LoginActivity.this);

        loginButton = findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
//        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvForgot:
                startActivity(new Intent(this, ForgotActivity.class));
                break;
            case R.id.imgClose:
                finish();
                break;
            case R.id.relloginbutton:
                setSignIn();
                break;
            default:
                break;
        }
    }

    private void setSignIn() {
        stremail = edtEmail.getText().toString();
        strpass = edtPass.getText().toString();
        strtoken = myPref.getMytoken();

        String succ = checkvalidation();
        if (succ.equalsIgnoreCase("true")) {

            if (checkNetwork.NetworkAvailable()) {
                myProgress.showLoading("Wait...");
                new logconnection().execute();
            } else {
                Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class logconnection extends AsyncTask<Void, Void, Void> {
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
            service.getLogin(stremail, strpass, strtoken, "0").enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    myProgress.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        Gson_login loginres = gson.fromJson(myres, Gson_login.class);

                        if (loginres.getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "" + loginres.getMessage(), Toast.LENGTH_SHORT).show();

                            myPref.setLogin(true);
                            if (loginres.getAlldata() != null) {
                                Log.e("login-auth", "-" + loginres.getAlldata().getAuth_token());
                                myPref.setId(loginres.getAlldata().getId());
                                myPref.setName(loginres.getAlldata().getName());
                                myPref.setUsername(loginres.getAlldata().getUsername());
                                myPref.setEmail(loginres.getAlldata().getEmail());
                                myPref.setSubscription(loginres.getAlldata().getSubscription());
                                myPref.setAuth_token(loginres.getAlldata().getAuth_token());
                                myPref.setDevice_token(loginres.getAlldata().getDevice_token());
                            }
                            if (loginres.getAlldata().getSubdata() != null) {
                                myPref.setUser_id(loginres.getAlldata().getSubdata().getUser_id());
                                myPref.setSubscription_type(loginres.getAlldata().getSubdata().getSubscription_type());
                                myPref.setStart_date(loginres.getAlldata().getSubdata().getStart_date());
                                myPref.setEnd_date(loginres.getAlldata().getSubdata().getEnd_date());
                            }
                            finishAffinity();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
//                            finish();
                        } else {
                            Toast.makeText(context, "" + loginres.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String checkvalidation() {
        String succ = "false";
        if (!stremail.matches(emailPattern)) {
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
            Log.e("cancel","cancecl");
        }

        @Override
        public void onError(FacebookException error) {
            Log.e("onError","onError");
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

                    myProgress.hideLoading();
                    if (response.isSuccessful()) {
                        String myres = response.body().toString();
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
//                                    Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "hey " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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