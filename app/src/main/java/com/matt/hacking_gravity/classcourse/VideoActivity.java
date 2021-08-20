package com.matt.hacking_gravity.classcourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.common.Apiclass;
import com.matt.hacking_gravity.common.ClassRetrofit;
import com.matt.hacking_gravity.common.MyPref;
import com.matt.hacking_gravity.model.Gson_Daily;
import com.potyvideo.library.AndExoPlayerView;
import com.potyvideo.library.globalInterfaces.ExoPlayerCallBack;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.matt.hacking_gravity.common.Apiclass.VideoUrl;

/*
videos/new-to-yoga-start-here/QETEa6jv6Mc72P2rcDqf.mp4

 */
public class VideoActivity extends AppCompatActivity {
    private Bundle bundle;
    private String category_id = "", course_id = "", class_id = "", video = "", duration = "";
    AndExoPlayerView andExoPlayerView;
    MyPref myPref;
    private ClassRetrofit classRetrofit;
    private String auth_token, userid;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        context = VideoActivity.this;
        bundle = getIntent().getExtras();
        myPref = new MyPref(VideoActivity.this);
        userid = myPref.getId();
        auth_token = myPref.getAuth_token();

        classRetrofit = new ClassRetrofit(VideoActivity.this);
        andExoPlayerView = findViewById(R.id.andExoPlayerView);
        if (bundle != null) {
            category_id = bundle.getString("category_id");
            course_id = bundle.getString("course_id");
            video = VideoUrl + bundle.getString("video");
            class_id = bundle.getString("class_id");

            try {
                if(video.isEmpty())
                {
                    Toast.makeText(VideoActivity.this,"Sorry Video data file not available...", Toast.LENGTH_SHORT).show();
                    Log.e("VIDEO", "video not available...");
                }else {

                    String extension = video.substring(video.lastIndexOf("."));

                    if(extension.equals(".MOV"))
                    {
                        Toast.makeText(VideoActivity.this,"video file not supported", Toast.LENGTH_SHORT).show();
                    }else {
                        andExoPlayerView.setSource(video);
                        andExoPlayerView.setExoPlayerCallBack(new ExoPlayerCallBack() {
                            @Override
                            public void onEnded() {
                                try {
                                    long dur = andExoPlayerView.getPlayer().getDuration();
//                                int timeSec = (int) (dur / 1000);
                                    int timeSec = (int) TimeUnit.MILLISECONDS.toMinutes(dur);
                                    duration = String.valueOf(timeSec);
                                } catch (Exception e) {
                                    duration = "0";
                                    e.printStackTrace();
                                }
                                Log.e("duration", "-" + duration);
                                call_myservices();
                                if (bundle.containsKey("pos")) {
                                    String name = category_id + course_id;
                                    int val = bundle.getInt("pos");
                                    if (myPref.getShowVideoTrack(name) < val) {
                                        myPref.setShowVideoTrack(name, val);
                                    }
                                }

                                Intent in = new Intent(VideoActivity.this, AftervideoActivity.class);
                                startActivity(in);
                                finish();
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(VideoActivity.this, "video file not supported", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            }catch (Exception e)
            {
                Toast.makeText(VideoActivity.this,"video file not supported", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

         /*   andExoPlayerView.getPlayer().addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    String stateString;
                    switch (playbackState) {
                        case Player.STATE_IDLE:
                            stateString = "ExoPlayer.STATE_IDLE      -";
                            break;
                        case Player.STATE_BUFFERING:
                            stateString = "ExoPlayer.STATE_BUFFERING -";
                            break;
                        case Player.STATE_READY:
                            stateString = "ExoPlayer.STATE_READY     -";
                            break;
                        case Player.STATE_ENDED:
//require service calling
                            try {
                                long dur = andExoPlayerView.getPlayer().getDuration();
//                                int timeSec = (int) (dur / 1000);
                                int timeSec =(int) TimeUnit.MILLISECONDS.toMinutes(dur);
                                duration = String.valueOf(timeSec);
                            } catch (Exception e) {
                                duration = "0";
                                e.printStackTrace();
                            }
                            Log.e("duration", "-" + duration);
                            call_myservices();
                            if (bundle.containsKey("pos")) {
                                String name = category_id + course_id;
                                int val = bundle.getInt("pos");
                                if (myPref.getShowVideoTrack(name) < val) {
                                    myPref.setShowVideoTrack(name, val);
                                }
                            }

                            Intent in = new Intent(VideoActivity.this, AftervideoActivity.class);
                            startActivity(in);
                            finish();
                            stateString = "ExoPlayer.STATE_ENDED     -";
                            break;
                        default:
                            stateString = "UNKNOWN_STATE             -";
                            break;
                    }

                    Log.e("changed state to ", stateString
                            + " playWhenReady: " + playWhenReady);

                }
            });*/

//            Intent in=new Intent(VideoActivity.this,AftervideoActivity.class);
//            startActivity(in);
            //"https://file-examples-com.github.io/uploads/2017/04/file_example_MP4_1280_10MG.mp4"
        }

//        Log.e("course_id", "-" + course_id);
//        Log.e("category_id", "-" + category_id);
//        Log.e("video", "-" + video);
    }

    public void call_myservices() {
        String data = "01/01/2020";
        try {
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
            data = sm.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Apiclass service = classRetrofit.categoryres(Apiclass.url, auth_token);
        service.postusersession(userid, class_id, data, duration).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                String myres = response.body().toString();
                if (response.isSuccessful()) {
                   /* Gson gson = new Gson();
                    Gson_Daily dailyres = gson.fromJson(myres, Gson_Daily.class);
                    if (dailyres.getStatus().equalsIgnoreCase("true")) {
                        if (dailyres.getDatacate().size() > 0) {

                        }
                    } else {
//                        Toast.makeText(context, "" + dailyres.getMessage(), Toast.LENGTH_SHORT).show();
                    }
*/
                } else {
                    Toast.makeText(context, "" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("error", "error");
            }
        });
    }

}