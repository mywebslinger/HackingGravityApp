package com.matt.hacking_gravity.classcourse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.matt.hacking_gravity.adapters.CoursesCLassesAdapter;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.common.RecyclerItemClickListener;
import com.matt.hacking_gravity.model.Classes_Model;
import com.matt.hacking_gravity.model.Course_Model;
import com.squareup.picasso.Picasso;

public class CoursesDetailActivity extends AppCompatActivity {
    private Bundle bun;
    private String data = "";
    Course_Model video_model = null;
    private Classes_Model classvideo_model = null;
    private TextView tvCourse, tvDesc, tvDay;
    private RecyclerView recClass;
    private ImageView  imgBack;
    private Context context;
    private TextView tvClass;
    private ShapeableImageView imgCourse,imgTrans;
    MyPref myPref;

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
        setContentView(R.layout.activity_courses_detail);

        imgCourse = findViewById(R.id.imgCourse);
        imgTrans=findViewById(R.id.imgTrans);
        imgCourse.setShapeAppearanceModel(imgCourse.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,25)
                .setBottomLeftCorner(CornerFamily.ROUNDED,25)
                .build());

        imgTrans.setShapeAppearanceModel(imgCourse.getShapeAppearanceModel()
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED,25)
                .setBottomLeftCorner(CornerFamily.ROUNDED,25)
                .build());

        myPref = new MyPref(CoursesDetailActivity.this);
        tvCourse = findViewById(R.id.tvCourse);
        tvDesc = findViewById(R.id.tvDesc);
        recClass = findViewById(R.id.recClass);
        imgBack = findViewById(R.id.imgBack);
        setMarginTop();

        context = CoursesDetailActivity.this;
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvClass = findViewById(R.id.tvClass);
        tvDay = findViewById(R.id.tvDay);
        bun = getIntent().getExtras();


        tvClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(video_model.getClasses().size()>0)
                {
                    if (video_model.getClasses().get(0).getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")) {

                        String name = video_model.getClasses().get(0).getCategory_id() + video_model.getClasses().get(0).getCourse_id();

                        Log.e(name," - "+myPref.getShowVideoTrack(name));

                            String video=video_model.getClasses().get(0).getVideo().toString();
                            Log.e("VIDEO", " "+video);
                            String extension = video.substring(video.lastIndexOf("."));
                            if(video.isEmpty())
                            {
                                Toast.makeText(CoursesDetailActivity.this,"Sorry Video data file not available...", Toast.LENGTH_SHORT).show();
                                Log.e("VIDEO", "video not available...");
                            }else {
                                Intent in = new Intent(CoursesDetailActivity.this, VideoActivity.class);
                                in.putExtra("category_id", "" + video_model.getClasses().get(0).getCategory_id());
                                in.putExtra("course_id", "" + video_model.getClasses().get(0).getCourse_id());
                                in.putExtra("video", "" + video_model.getClasses().get(0).getVideo());
                                in.putExtra("class_id", "" + video_model.getClasses().get(0).getId());
                                in.putExtra("pos", 0);
                                startActivity(in);
                            }
                    } else {
                        Intent in = new Intent(CoursesDetailActivity.this, InAppActivity.class);
                        startActivity(in);
                    }
                }else
                {
                    if(classvideo_model.getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")){
                        try {
                            String video=classvideo_model.getVideo().toString();
                            Log.e("VIDEO", " "+video);
                            String extension = video.substring(video.lastIndexOf("."));
                            if(video.isEmpty())
                            {
                                Toast.makeText(CoursesDetailActivity.this,"Sorry Video data file not available...", Toast.LENGTH_SHORT).show();
                                Log.e("VIDEO", "video not available...");
                            }else {
                                if (extension.equals(".MOV")) {
                                    Toast.makeText(CoursesDetailActivity.this, "video file not supported", Toast.LENGTH_SHORT).show();
                                } else {
                                    Intent in = new Intent(CoursesDetailActivity.this, VideoActivity.class);
                                    in.putExtra("category_id", "" + classvideo_model.getCategory_id());
                                    in.putExtra("course_id", "" + classvideo_model.getCourse_id());
                                    in.putExtra("class_id", "" + classvideo_model.getId());
                                    in.putExtra("video", "" + classvideo_model.getVideo());
                                    startActivity(in);
                                }
                            }
                        }catch (Exception e){e.printStackTrace();}

                    }else{
                        Intent in = new Intent(CoursesDetailActivity.this, InAppActivity.class);
                        startActivity(in);
                    }

                }

            }
        });

        if (bun.containsKey("data")) {
            data = bun.getString("data");
//            Log.e("data", "-" + data);
            if (data != null) {
                Gson gson = new Gson();
                video_model = gson.fromJson(data, Course_Model.class);
//                tvTitle.setText(video_model.getName());
//                Log.e("data", "-" + video_model);
                tvCourse.setText(video_model.getName());
                String des = video_model.getDescription();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvDesc.setText(Html.fromHtml(des, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvDesc.setText(Html.fromHtml(des));
                }
                tvDay.setText(video_model.getClasses().size() + " Days");
                try {
                    Picasso.get().load(Apiclass.imgUrl + video_model.getImage()).into(imgCourse);
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(video_model.getClasses().size()>0) {
                    if (video_model.getClasses().get(0).getIs_free().equalsIgnoreCase("1") || (myPref.getSubscription().toString()).equalsIgnoreCase("1")) {
                        tvClass.setText("Start Class");
                    } else {
                        tvClass.setText("Unlock");
                    }
                }

                setAdapters();
            }

            if(video_model.getClasses().size()==0)
            {

                        Gson gson = new Gson();
                        classvideo_model = gson.fromJson(data, Classes_Model.class);

                        if(classvideo_model.getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")){
                            tvClass.setText("Start Class");
                        }else{
                            tvClass.setText("Unlock");
                        }
            }

        }
    }

    public void setAdapters() {
//    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//    recClass.setLayoutManager(linearLayoutManager);
        recClass.setLayoutManager(new GridLayoutManager(this, 5));
        CoursesCLassesAdapter adapter_recyclerview = new CoursesCLassesAdapter(context, video_model.getClasses());
        recClass.setAdapter(adapter_recyclerview);

        recClass.addOnItemTouchListener(new RecyclerItemClickListener(CoursesDetailActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                String name = video_model.getClasses().get(position).getCategory_id() + video_model.getClasses().get(position).getCourse_id();

//                Log.e(name," - "+myPref.getShowVideoTrack(name));

              /*  if (myPref.getShowVideoTrack(name) == position-1|| position==0||myPref.getShowVideoTrack(name) >= position) {
                    Intent in = new Intent(CoursesDetailActivity.this, VideoActivity.class);
                    in.putExtra("category_id", "" + video_model.getClasses().get(position).getCategory_id());
                    in.putExtra("course_id", "" + video_model.getClasses().get(position).getCourse_id());
                    in.putExtra("video", "" + video_model.getClasses().get(position).getVideo());
                    in.putExtra("class_id",""+video_model.getClasses().get(position).getId());
                    in.putExtra("pos",position);
                    startActivity(in);
                }else{
                    Toast.makeText(CoursesDetailActivity.this,"Show First "+position,Toast.LENGTH_SHORT).show();
                }*/

                if (video_model.getClasses().get(position).getIs_free().equalsIgnoreCase("1")||(myPref.getSubscription().toString()).equalsIgnoreCase("1")) {

                    String name = video_model.getClasses().get(position).getCategory_id() + video_model.getClasses().get(position).getCourse_id();

                    Log.e(name," - "+myPref.getShowVideoTrack(name));

                    if (myPref.getShowVideoTrack(name) == position-1|| position==0||myPref.getShowVideoTrack(name) >= position) {
                        String video=video_model.getClasses().get(position).getVideo().toString();
                        String extension = video.substring(video.lastIndexOf("."));
                        if(video.isEmpty())
                        {
                            Toast.makeText(CoursesDetailActivity.this,"Sorry Video data file not available...", Toast.LENGTH_SHORT).show();
                            Log.e("VIDEO", "video not available...");
                        }else {
                            Intent in = new Intent(CoursesDetailActivity.this, VideoActivity.class);
                            in.putExtra("category_id", "" + video_model.getClasses().get(position).getCategory_id());
                            in.putExtra("course_id", "" + video_model.getClasses().get(position).getCourse_id());
                            in.putExtra("video", "" + video_model.getClasses().get(position).getVideo());
                            in.putExtra("class_id", "" + video_model.getClasses().get(position).getId());
                            in.putExtra("pos", position);
                            startActivity(in);
                        }
                    }else{
                        Toast.makeText(CoursesDetailActivity.this,"Show First "+position,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent in = new Intent(CoursesDetailActivity.this, InAppActivity.class);
                    startActivity(in);
                }
            }
        }));
    }

}