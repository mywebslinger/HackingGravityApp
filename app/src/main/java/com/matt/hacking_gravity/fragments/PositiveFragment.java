package com.matt.hacking_gravity.fragments;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.CheckNetwork;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.MyProgress;
import com.matt.hacking_gravity.model.Gson_Daily;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PositiveFragment extends Fragment implements View.OnClickListener {
    private Context context;
    private CheckNetwork checkNetwork;
    private MyPref myPref;
    private ClassRetrofit classRetrofit;
    private String auth_token, userid;
    private MyProgress mProgressDialog;
    private ImageView imgVibe, hideimag;
    private TextView tvVibeDate, tvContent, mytext;
    private LinearLayout linShare, linSave;
    private String imgUrls = "";
    private boolean isSave = false;
    int resid;
    RelativeLayout myTopRel;

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                getResources().getDimensionPixelSize(R.dimen.btn_height)
        );
        params.setMargins(0, myPref.getstatusHeight() + getActivity().getResources().getDimensionPixelSize(R.dimen.vibe_top_mar), 0, 0);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tvVibeDate.setLayoutParams(params);
    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_positive, container, false);
        imgVibe = view.findViewById(R.id.imgVibe);
        mytext = view.findViewById(R.id.mytext);
        hideimag = view.findViewById(R.id.hideimag);
        myTopRel = view.findViewById(R.id.myTopRel);
        resid = R.id.tvVibeDate;
        tvVibeDate = view.findViewById(R.id.tvVibeDate);
        setMarginTop();
        tvContent = view.findViewById(R.id.tvContent);
        linShare = view.findViewById(R.id.linShare);
        linSave = view.findViewById(R.id.linSave);
        linSave.setOnClickListener(this);
        linShare.setOnClickListener(this);

        if (checkNetwork.NetworkAvailable()) {
            mProgressDialog.showLoading("Please Wait....");
            new vibsconn().execute();
        } else {
            Toast.makeText(context, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

//        printHashKey(context);

        return view;
    }

    public static void printHashKey(Context context) {
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("AppLog", "key:" + hashKey + "=");
            }
        } catch (Exception e) {
            Log.e("AppLog", "error:", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linShare:
                isSave = false;
                verifyPermissions();
                break;
            case R.id.linSave:
                isSave = true;
                verifyPermissions();
                break;
            default:
                break;
        }
    }


    public void SaveVide() {
        if (!imgUrls.equalsIgnoreCase("")) {
            mProgressDialog.showLoading("Please Wait....");
            sharesave();
//            Picasso.get().load(imgUrls).into(getTarget("myImg.jpg"));
        } else {
            Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
        }
    }

    public class vibsconn extends AsyncTask<Void, Void, Void> {
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
            service.getdailyvobs(userid).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {


                    if (response.isSuccessful()) {
                        String myres = response.body().toString();
                        Gson gson = new Gson();
                        Gson_Daily dailyres = gson.fromJson(myres, Gson_Daily.class);
                        if (dailyres.getStatus().equalsIgnoreCase("true")) {
                            if (dailyres.getDatacate().size() > 0) {
                                if (dailyres.getDatacate().get(0).getImage() != null) {
                                    if (!dailyres.getDatacate().get(0).getImage().equalsIgnoreCase("")) {
                                        imgUrls = Apiclass.imgUrl + dailyres.getDatacate().get(0).getImage();

//                                        Picasso.get().load(imgUrls).fit().centerCrop().into(imgVibe);

                                        Picasso.get().load(imgUrls).fit().centerCrop().into(imgVibe, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                                mProgressDialog.hideLoading();
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                hidedialog();
                                            }
                                        });
                                    } else {
                                        hidedialog();
                                    }
                                } else {
                                    hidedialog();
                                }

                                try {
                                    if (!dailyres.getDatacate().get(0).getScheduled_at().equalsIgnoreCase("")) {
                                        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date = fmt.parse(dailyres.getDatacate().get(0).getScheduled_at());

                                        SimpleDateFormat fmtOut = new SimpleDateFormat("MMMM dd, yyyy");
                                        String dates = fmtOut.format(date);
                                        tvVibeDate.setText("" + dates);
                                    }
                                } catch (Exception e) {

                                }


                                tvContent.setText(dailyres.getDatacate().get(0).getContent());
                            } else {
                                hidedialog();
                            }
                        } else {
                            hidedialog();
                            Toast.makeText(context, "" + dailyres.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        hidedialog();
                        Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hidedialog();
                }
            });
        }
    }

    public void sharesave() {

        try {
            if (isSave) {
                hideimag.setImageBitmap(((BitmapDrawable) imgVibe.getDrawable()).getBitmap());
                mytext.setText(tvContent.getText().toString());
                /*new Thread(new Runnable() {

                    @Override
                    public void run() {*/

              /*  ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File directory = cw.getDir("profile", Context.MODE_PRIVATE);
                if (!directory.exists()) {
                    directory.mkdir();
                }*/

                File path = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getResources().getString(R.string.app_name));
                if (!path.exists()) {
                    path.mkdir();
                }
                final File file = new File(path, "yoga_" + System.currentTimeMillis() + ".jpg");
                try {
                    file.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(file);
                    Bitmap mybit = getBitmapFromView(myTopRel);
                    if (mybit != null) {
                        mybit.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                        ostream.flush();
                        ostream.close();

//                        getActivity().runOnUiThread(new Runnable() {
//                            public void run() {
                        hidedialog();
                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        });
                    } else {
                        hidedialog();
                        Log.e("null bitmap", "null bitmap");
                    }
                } catch (IOException e) {
                    Log.e("IOException", e.getLocalizedMessage());
                }
//                    }
//                }).start();
            } else {

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");

                Bitmap mybit = ((BitmapDrawable) imgVibe.getDrawable()).getBitmap();
                if (mybit != null) {
                    hideimag.setImageBitmap(mybit);
                    mytext.setText(tvContent.getText().toString());

//                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(drawTextOnBitmap(context,resid,tvContent.getText().toString())));//getLocalBitmapUri(bitmap));
//                    myTopRel.setVisibility(View.VISIBLE);
                    try {
                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(getBitmapFromView(myTopRel)));//getLocalBitmapUri(bitmap));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    hidedialog();

                    if (i.resolveActivity(context.getPackageManager()) != null) {
                        startActivity(Intent.createChooser(i, "Share Image"));
                    } else {
                        Toast.makeText(context, "Activity Not Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    hidedialog();
                    Log.e("null bitmap", "null bitmap");
                }
            }
        } catch (Exception e) {
            Log.e("error on vibe", "-" + e.getMessage());
            e.printStackTrace();
            hidedialog();
        }
    }

    public void hidedialog() {
        try {
            if (mProgressDialog != null) {
                mProgressDialog.hideLoading();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    //target to save
   /* private Target getTarget(final String url) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {

                if (isSave) {
                    hideimag.setImageBitmap(((BitmapDrawable) imgVibe.getDrawable()).getBitmap());
                    mytext.setText(tvContent.getText().toString());
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            final File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
                            try {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);


                                Bitmap mybit = getBitmapFromView(myTopRel);

                                mybit.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                                ostream.flush();
                                ostream.close();

                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null) {
                                                mProgressDialog.hideLoading();
                                            }
                                        } catch (Exception e) {

                                        }
                                        Toast.makeText(getApplicationContext(), "Save Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                Log.e("IOException", e.getLocalizedMessage());
                            }
                        }
                    }).start();
                } else {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");

                    hideimag.setImageBitmap(((BitmapDrawable) imgVibe.getDrawable()).getBitmap());
                    mytext.setText(tvContent.getText().toString());

//                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(drawTextOnBitmap(context,resid,tvContent.getText().toString())));//getLocalBitmapUri(bitmap));
//                    myTopRel.setVisibility(View.VISIBLE);
                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(getBitmapFromView(myTopRel)));//getLocalBitmapUri(bitmap));

                    try {
                        if (mProgressDialog != null) {
                            mProgressDialog.hideLoading();
                        }
                    } catch (Exception e) {

                    }

                    startActivity(Intent.createChooser(i, "Share Image"));
                }

            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.e("fail", "fail");
                if (mProgressDialog != null) {
                    mProgressDialog.hideLoading();
                }
            }


            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.e("onPrepareLoad", "onPrepareLoad");

            }
        };
        return target;
    }*/

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {

            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("profile", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(directory, "share_image.jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
//            bmpUri = Uri.fromFile(file);
            bmpUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), file);
//            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private static final String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public void verifyPermissions() {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionReadExternalMemory = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED && permissionReadExternalMemory != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(STORAGE_PERMISSIONS, 1001);
        } else {
            SaveVide();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    SaveVide();
                } else {
                    verifyPermissions();
                }
                break;

            default:
                break;
        }
    }

    private Bitmap getBitmapFromView(View view) {
        try {
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            //Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            //Get the view's background
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null) {
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            } else {
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            }
            // draw the view on the canvas
            view.draw(canvas);
            //return the bitmap
            return returnedBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap drawTextOnBitmap(Context context, int resId, String text) {

        // prepare canvas
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = ((BitmapDrawable) imgVibe.getDrawable()).getBitmap();//BitmapFactory.decodeResource(resources, resId);

        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are immutable, so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);

        // new antialiased Paint
        TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));
        // text size in pixels
        paint.setTextSize((int) (bitmap.getHeight() / 10 * scale));
        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

        // set text width to canvas width minus 16dp padding
        int textWidth = canvas.getWidth() - (int) (16 * scale);

        // init StaticLayout for text
        StaticLayout textLayout = new StaticLayout(text, paint, textWidth,
                Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

        // get height of multiline text
        int textHeight = textLayout.getHeight();

        // get position of text's top left corner
        float x = (bitmap.getWidth() - textWidth) / 2;
        float y = (bitmap.getHeight() - textHeight) / 2;

        // draw text to the Canvas center
        canvas.save();
        canvas.translate(x, y);
        textLayout.draw(canvas);
        canvas.restore();

        return bitmap;
    }

}