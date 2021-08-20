package com.matt.hacking_gravity.adapters;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matt.hacking_gravity.R;
import com.matt.hacking_gravity.model.Cat_Model;
import com.squareup.picasso.Picasso;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

import static com.matt.hacking_gravity.common.Apiclass.imgUrl;


public class CategoryAdapter extends RecyclerView.Adapter <CategoryAdapter.MyViewHolder>{
    private List<Cat_Model> catemodels ;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname;
        public ImageView imgclass;

        public MyViewHolder(View view) {
            super(view);

            txtname =  view.findViewById(R.id.txtname);
            imgclass=view.findViewById(R.id.img_class);
        }
    }


    public CategoryAdapter(Context context, List<Cat_Model> modelCategories) {
        this.catemodels = modelCategories;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_catlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.txtname.setText(catemodels.get(position).getClassname());
        Log.e("Error : ",catemodels.get(position).getClassimg());
        try
        {
            Picasso.get().load(imgUrl+catemodels.get(position).getClassimg()).resize(700,500).into(holder.imgclass);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Picasso.get().load(imgUrl+catemodels.get(position).getClassimg()).resize(700,500).into(holder.imgclass);
    }
    @Override
    public int getItemCount() {
        return catemodels.size();
    }
}



