package com.matt.hacking_gravity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;
import com.matt.hacking_gravity.adapters.PaymentAdapter;
import com.matt.hacking_gravity.classcourse.ClassesActivity;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.common.RecyclerItemClickListener;
import com.matt.hacking_gravity.model.Cat_Model;
import com.matt.hacking_gravity.model.Gson_SubscriptionPlan;
import com.matt.hacking_gravity.model.SubscribeData;
import com.matt.hacking_gravity.settingpage.PrivacyActivity;
import com.matt.hacking_gravity.settingpage.TermConditionActivity;

import java.util.ArrayList;
import java.util.List;
//https://medium.com/androiddevelopers/subscriptions-101-for-android-apps-b7005a7e93a6
public class InAppActivity extends AppCompatActivity implements PurchasesUpdatedListener {
    private TextView txt_u_name;
    private RecyclerView payment_list;
    private ArrayList<Cat_Model> all_list = new ArrayList<>();
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private Context context;
    private ImageView imgmain, imgclose;
    private String auth_token;
    private ClassRetrofit classRetrofit;
    private MyProgress mProgressDialog;
    private TextView tvPrivacy, tvTerm;
    private String userid, subType;

    public String SKU__MONTH = "monthly_plan";
    public String SKU__YEAR = "yearly_plan";
    public String SKU__LIFE = "life_time_plan";
    private BillingClient mBillingClient;
    private String ITEM_SKU = "",selectedType="";

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.imgBack_h),
                getResources().getDimensionPixelSize(R.dimen.imgBack_h)
        );
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.imgBack_marLeft), myPref.getstatusHeight() + getResources().getDimensionPixelSize(R.dimen.inAppPad), 0, 0);
        imgclose.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_app);

        context = InAppActivity.this;
        myPref = new MyPref(context);
        imgclose = findViewById(R.id.imgclose);
        tvPrivacy = findViewById(R.id.tvPrivacy);
        tvTerm = findViewById(R.id.tvTerm);
        setMarginTop();

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkNetwork = new CheckNetwork(context);
        classRetrofit = new ClassRetrofit(context);
        mProgressDialog = new MyProgress(context);
        txt_u_name = findViewById(R.id.txtu_name);
        imgmain = findViewById(R.id.img_main);
        payment_list = findViewById(R.id.rec_paylist);

        payment_list.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(all_list!=null) {
                    setPlan(all_list.get(position).getPay_id());
                }
            }
        }));

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(InAppActivity.this, PrivacyActivity.class);
                startActivity(in);
            }
        });

        tvTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(InAppActivity.this, TermConditionActivity.class);
                startActivity(in);
            }
        });

        txt_u_name.setText(myPref.getName());
        auth_token = myPref.getAuth_token();
        userid = myPref.getId();

        if (all_list.size() == 0) {
            if (checkNetwork.NetworkAvailable()) {
                mProgressDialog.showLoading("Please Wait....");
                new subscription().execute();
            } else {
                Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            insertdata();
        }

        setBillingClient();

           /*
       this service for in app subscribe data plan
       if(myPref.getSubscription().equalsIgnoreCase("1")&&!myPref.getSubscription_type().equalsIgnoreCase("")){
       mProgressDialog.showLoading("Please Wait....");
            subType=myPref.getSubscription_type();
            new SubscriptionsConnection().execute();
        }*/

    }

    public void setBillingClient(){
        mBillingClient = BillingClient.newBuilder(InAppActivity.this).enablePendingPurchases().setListener(this).build();
        if (!mBillingClient.isReady()) {
            StartConnection();
        }
    }

    public void setPlan(String detail) {
        String plan = detail;
        if (plan.equalsIgnoreCase("1")) {
            selectedType=BillingClient.SkuType.SUBS;
            ITEM_SKU = SKU__MONTH;
            subType="1";
        } else if (plan.equalsIgnoreCase("2")) {
            selectedType=BillingClient.SkuType.SUBS;
            ITEM_SKU = SKU__YEAR;
            subType="2";
        } else if (plan.equalsIgnoreCase("3")) {
            selectedType=BillingClient.SkuType.INAPP;
            ITEM_SKU = SKU__LIFE;
            subType="3";
        }else{
            ITEM_SKU="";
        }
        Log.e("ITEM_SKU","-"+ITEM_SKU);
        if (!ITEM_SKU.equalsIgnoreCase("")) {
//            mBillingClient = BillingClient.newBuilder(InAppActivity.this).enablePendingPurchases().setListener(this).build();
//            if (!mBillingClient.isReady()) {
//                StartConnection();
//            }
            if(mBillingClient.isReady()){
                setProductID();
                queryPurchases();
            }else{
                StartConnection();
            }
        } else {
            Toast.makeText(InAppActivity.this, "Something Went Wrong! Please Try Again.", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }

    private void insertdata() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        payment_list.setLayoutManager(linearLayoutManager);
        PaymentAdapter adapter_recyclerview = new PaymentAdapter(context, all_list);
        payment_list.setAdapter(adapter_recyclerview);
    }

    public class subscription extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            service = classRetrofit.categoryres(Apiclass.url, auth_token);
            Log.e("TAG",auth_token);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            service.getsubscription().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        String myres = response.body().toString();
                        Gson gson = new Gson();
                        Gson_SubscriptionPlan plnres = gson.fromJson(myres, Gson_SubscriptionPlan.class);
                        if (plnres.getStatus().equalsIgnoreCase("true")) {

                            for (int i = 0; i < plnres.dataplan.size(); i++) {
                                Cat_Model cat_model = new Cat_Model();
                                cat_model.setPay_id(plnres.dataplan.get(i).getId());
                                cat_model.setPay_name(plnres.dataplan.get(i).getName());
                                cat_model.setPay_type(plnres.dataplan.get(i).getType());
                                cat_model.setPay_value(plnres.dataplan.get(i).getValue());
                                all_list.add(cat_model);
                            }
                        } else {
                            Toast.makeText(context, "" + plnres.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                    insertdata();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    mProgressDialog.hideLoading();
                }
            });
        }
    }

    //service after plan subscription
    private class SubscriptionsConnection extends AsyncTask<Void, Void, Void> {
        Apiclass service;

        @Override
        protected Void doInBackground(Void... voids) {
            service = classRetrofit.categoryres(Apiclass.url, auth_token);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("service Date :",userid+" "+subType);
            service.postsubscribe(userid, subType).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String myres = response.body().toString();
                    mProgressDialog.hideLoading();
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();
                        SubscribeData gsonTerm = gson.fromJson(myres, SubscribeData.class);
                        if (gsonTerm.getStatus().equalsIgnoreCase("true")) {
                            Toast.makeText(context, "" + gsonTerm.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {

                            if (gsonTerm.getErrorClass() != null) {
                                if (gsonTerm.getErrorClass().getSubscription_type().size() > 0) {
                                    String error = gsonTerm.getErrorClass().getSubscription_type().get(0);

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
                    mProgressDialog.hideLoading();
                }
            });
        }
    }


    public void StartConnection() {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The billing client is ready. You can query purchases here.
//                    setProductID();
                    queryPurchases();
                } else {
                    Toast.makeText(InAppActivity.this, billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
//                Toast.makeText(InAppActivity.this, getResources().getString(R.string.billing_connection_failure), Toast.LENGTH_SHORT);
            }
        });
    }

    private void setProductID() {
        List<String> skuList = new ArrayList<>();
        skuList.add(ITEM_SKU);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();

        params.setSkusList(skuList).setType(selectedType);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                     List<SkuDetails> skuDetailsList) {
                        if (billingResult == null) {
                            Log.wtf("result", "onSkuDetailsResponse: null BillingResult");
                            return;
                        }

                        if (skuDetailsList != null) {
//                            Toast.makeText(context," "+skuDetailsList.size(),Toast.LENGTH_SHORT).show();
                            if (skuDetailsList.size() > 0) {
                                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                        .setSkuDetails(skuDetailsList.get(0))
                                        .build();
                                BillingResult responseCode = mBillingClient.launchBillingFlow(InAppActivity.this, flowParams);
                                if (responseCode.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                                }
                                Log.e("getResponseCode", "-" + responseCode.getResponseCode());
                            } else {
                                Log.e("skuDetailsList size", "-" + skuDetailsList.size());
                            }
                        } else {
                            Log.e("skulist null", "null sku list");
                        }
                    }

                });

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        if (billingResult.getResponseCode()==BillingClient.BillingResponseCode.DEVELOPER_ERROR)
        {
            Log.e("developer size", "size- " + purchases.size());
        }
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            Log.e("purchases size", "size- " + purchases.size());
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Log.e("TAG", "User Canceled" + billingResult.getResponseCode());
            onBackPressed();
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
//            new SubscriptionsConnection().execute();
//            ShowDialog(); Bhavesh
        } else {
            Log.e("TAG", "Other code" + billingResult.getResponseCode());
//            onBackPressed();//check flow require or not bhk
            // Handle any other error codes.
        }
    }

    private void handlePurchase(Purchase purchase) {
        //below bhk need for new version
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            // Acknowledge purchase and grant the item to the user
//            Log.e("purchase success", "success");
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(BillingResult billingResult) {
                        new SubscriptionsConnection().execute();
                        Log.e("acknowledge res code: ", "" + billingResult.getResponseCode());
                        
                    }
                });
            }
//            ShowDialog(); bhavesh

        } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
            // Here you can confirm to the user that they've started the pending
            // purchase, and to complete it, they should follow instructions that
            // are given to them. You can also choose to remind the user in the
            // future to complete the purchase if you detect that it is still
            // pending.
        }
    }

    private void queryPurchases() {

        //this things may be return all history like subscription and expired also
        /*mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(BillingResult billingResult, List<PurchaseHistoryRecord> purchasesList) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                                && purchasesList != null) {
                            for (PurchaseHistoryRecord purchase : purchasesList) {
                                // Process the result.
                                Log.e("PurchaseHistoryRecord","-"+purchase.getSku());
                            }
                        }
                    }
                });*/

        //Method not being used for now, but can be used if purchases ever need to be queried in the future
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(selectedType);
        if (purchasesResult != null) {
            List<Purchase> purchasesList = purchasesResult.getPurchasesList();
            if (purchasesList == null) {
                return;
            }
//            Log.e("query purchase size", "-" + purchasesList.size());
            if (!purchasesList.isEmpty()) {
                for (Purchase purchase : purchasesList) {
                    if (purchase.getSku().equals(ITEM_SKU)) {
                        Log.e("same", "same");
//                        mSharedPreferences.edit().putBoolean(getResources().getString(R.string.pref_remove_ads_key), true).commit();
//                        preferenceHelper.setAdFree(true);
//                        mBuyButton.setText(getResources().getString(R.string.pref_ad_removal_purchased));
//                        mBuyButton.setEnabled(false);
                    }
                }
            }
        }
    }

    public void setRestore(){
        if(myPref.getSubscription().equalsIgnoreCase("1")){
            queryPurchases();//require check all purchase sku
        }
//        queryPurchases - restore handle by query purchase
    }
}
