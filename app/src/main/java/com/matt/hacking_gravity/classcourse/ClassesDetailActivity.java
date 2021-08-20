package com.matt.hacking_gravity.classcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.gson.Gson;
import com.matt.hacking_gravity.InAppActivity;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.model.Classes_Model;
import com.matt.hacking_gravity.model.Gson_category;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.matt.hacking_gravity.common.Apiclass.VideoUrl;

public class ClassesDetailActivity extends AppCompatActivity {
    private Bundle bun;
    private String data = "";
    private Classes_Model video_model = null;
    private ImageView imgBack;
    ShapeableImageView imgClasses,imgTrans;
    private TextView tvCourse,tvClass,tvDesc,tvDuration;
    private RelativeLayout relclass;
    private MyPref myPref;

    public void setMarginTop() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                getResources().getDimensionPixelSize(R.dimen.imgBack_h),
                getResources().getDimensionPixelSize(R.dimen.imgBack_h)
        );
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.imgBack_marLeft), myPref.getstatusHeight(), 0, 0);
        imgBack.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classes_detail);


        myPref=new MyPref(ClassesDetailActivity.this);
        Log.e("Subscription",myPref.getSubscription());

        imgClasses=findViewById(R.id.imgClasses);
        imgTrans=findViewById(R.id.imgTrans);
        imgClasses.setShapeAppearanceModel(imgClasses.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,25)
                .setBottomLeftCorner(CornerFamily.ROUNDED,25)
                .build());

        imgTrans.setShapeAppearanceModel(imgClasses.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,25)
                .setBottomLeftCorner(CornerFamily.ROUNDED,25)
                .build());
        tvDuration=findViewById(R.id.tvDuration);

        imgBack=findViewById(R.id.imgBack);
        setMarginTop();
        tvCourse=findViewById(R.id.tvCourse);
        tvClass=findViewById(R.id.tvClass);
        tvDesc=findViewById(R.id.tvDesc);
        relclass=findViewById(R.id.relclass);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bun = getIntent().getExtras();
        if (bun.containsKey("data")) {
            data = bun.getString("data");

            if (data != null) {
                Gson gson = new Gson();
                video_model = gson.fromJson(data, Classes_Model.class);
//                tvTitle.setText(video_model.getName());
                try {
                    Picasso.get().load(Apiclass.imgUrl+video_model.getImage()).into(imgClasses);
                }catch (Exception e){
                    e.printStackTrace();
                }

                tvCourse.setText(video_model.getName());
                String des=video_model.getDescription();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDesc.setText(Html.fromHtml(des,Html.FROM_HTML_MODE_COMPACT));
                }else{
                    tvDesc.setText(Html.fromHtml(des));
                }
                if(video_model.getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")){
                    tvClass.setText("Start Class");
                }else{
                    tvClass.setText("Unlock");
                }

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            if (!isFinishing()) {
                                String video=video_model.getVideo().toString();
                                String extension = video.substring(video.lastIndexOf("."));

                                if (!(extension.equals(".MOV"))) {
                                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                    if (Build.VERSION.SDK_INT >= 14)
                                        retriever.setDataSource(VideoUrl + video_model.getVideo(), new HashMap<String, String>());
                                    else
                                        retriever.setDataSource(VideoUrl + video_model.getVideo());

                                    final String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (tvDuration != null) {
                                                    if (!time.equalsIgnoreCase("")) {
                                                        int timeSec = (int) TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(time));
                                                        tvDuration.setText("" + timeSec + " Minutes");
                                                    }
                                                }
                                            }
                                        });
                                        //                        Thread.sleep(300);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Log.e("time", "-" + time);

                                    //            int timeSec = (int) TimeUnit.MILLISECONDS.toMinutes(time);
                                    retriever.release();
                                }
                            } else {
                                Log.e("isBreak", "-" + isFinishing());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                }.execute();

                relclass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(video_model.getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")){
                            String video=video_model.getVideo().toString();
                            String extension = video.substring(video.lastIndexOf("."));
                            if(video.isEmpty())
                            {
                                Toast.makeText(ClassesDetailActivity.this,"Sorry Video data file not available...", Toast.LENGTH_SHORT).show();
                                Log.e("VIDEO", "video not available...");
                            }else {
                                if (extension.equals(".MOV")) {
                                    Toast.makeText(ClassesDetailActivity.this, "video file not supported", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent in = new Intent(ClassesDetailActivity.this, VideoActivity.class);
                                    in.putExtra("category_id", "" + video_model.getCategory_id());
                                    in.putExtra("course_id", "" + video_model.getCourse_id());
                                    in.putExtra("class_id", "" + video_model.getId());
                                    in.putExtra("video", "" + video_model.getVideo());
                                    startActivity(in);
                                }
                            }
                        }else{
                            Intent in = new Intent(ClassesDetailActivity.this, InAppActivity.class);
                            startActivity(in);
                        }
                    }
                });

            }
        }
    }
}