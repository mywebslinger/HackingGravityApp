package com.matt.hacking_gravity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.model.Cat_Model;
import com.matt.hacking_gravity.model.Course_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.matt.hacking_gravity.common.Apiclass.imgUrl;


public class CoursesAdapter extends RecyclerView.Adapter <CoursesAdapter.MyViewHolder>{
    private List<Course_Model> catemodels ;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname,txtDays;
        public ImageView imgclass;

        public MyViewHolder(View view) {
            super(view);
            txtDays=view.findViewById(R.id.txtDays);
            txtname =  view.findViewById(R.id.txtname);
            imgclass=view.findViewById(R.id.img_class);
        }
    }


    public CoursesAdapter(Context context, List<Course_Model> modelCategories) {
        this.catemodels = modelCategories;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cources, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtname.setText(catemodels.get(position).getName());
        holder.txtDays.setText(catemodels.get(position).getClasses().size()+" Days");
        try
        {
            Picasso.get().load(imgUrl+catemodels.get(position).getImage()).into(holder.imgclass);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return catemodels.size();
    }
}



