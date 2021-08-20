package com.matt.hacking_gravity.adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.classcourse.ClassesActivity;
import com.matt.hacking_gravity.model.Classes_Model;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import static com.matt.hacking_gravity.common.Apiclass.VideoUrl;
import static com.matt.hacking_gravity.common.Apiclass.imgUrl;


public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {
    private List<Classes_Model> catemodels;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname, tvFree, tvDuration;
        public ImageView imgclass;

        public MyViewHolder(View view) {
            super(view);
            tvFree = view.findViewById(R.id.tvFree);
            txtname = view.findViewById(R.id.txtname);
            imgclass = view.findViewById(R.id.img_class);
            tvDuration = view.findViewById(R.id.tvDuration);
        }
    }


    public ClassesAdapter(Context context, List<Classes_Model> modelCategories) {
        this.catemodels = modelCategories;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_classes, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtname.setText(catemodels.get(position).getName());
        try
        {
            Picasso.get().load(imgUrl + catemodels.get(position).getImage()).into(holder.imgclass);
        }catch (Exception e){
            e.printStackTrace();
        }


        if (catemodels.get(position).getIs_free().equalsIgnoreCase("1")) {
            holder.tvFree.setVisibility(View.VISIBLE);
            holder.tvFree.setText("FREE");
        } else {
            holder.tvFree.setVisibility(View.GONE);
            holder.tvFree.setText("");
        }
        if(!((ClassesActivity) context).isFinishing()) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        String video=catemodels.get(position).getVideo().toString();
                        String extension = video.substring(video.lastIndexOf("."));

                        if (!(extension.equals(".MOV"))) {
                            if (!((ClassesActivity) context).isFinishing()) {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                if (Build.VERSION.SDK_INT >= 14)
                                    retriever.setDataSource(VideoUrl + catemodels.get(position).getVideo(), new HashMap<String, String>());
                                else
                                    retriever.setDataSource(VideoUrl + catemodels.get(position).getVideo());

                                final String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

                                try {
                                    ((ClassesActivity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (holder.tvDuration != null) {
                                                if (!time.equalsIgnoreCase("")) {
                                                    int timeSec = (int) TimeUnit.MILLISECONDS.toMinutes(Integer.parseInt(time));
                                                    holder.tvDuration.setText("" + timeSec + " Minutes");
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
                            } else {
                                Log.e("isBreak", "-" + ((ClassesActivity) context).isFinishing());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                }
            }.execute();
        }
    }
//
//    boolean isBreak = false;
//
//    public void isBreak(boolean mybreak) {
//        isBreak=mybreak;
//    }

    @Override
    public int getItemCount() {
        return catemodels.size();
    }
}



