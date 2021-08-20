package com.matt.hacking_gravity.adapters;

import android.content.Context;
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



public class PaymentAdapter extends RecyclerView.Adapter <PaymentAdapter.MyViewHolder>{
    private List<Cat_Model> catemodels ;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtpay,txtpayvalue;
        public RelativeLayout relmain;

        public MyViewHolder(View view) {
            super(view);

            txtpay =  view.findViewById(R.id.txt_pay);
            txtpayvalue =  view.findViewById(R.id.txt_payvalue);
            relmain=view.findViewById(R.id.relmain);
        }
    }


    public PaymentAdapter(Context context, List<Cat_Model> modelCategories) {
        this.catemodels = modelCategories;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_paylist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if (position==0){
            holder.txtpay.setText("$"+catemodels.get(position).getPay_value()+"/"+catemodels.get(position).getPay_name()+"(7 DAY FREE TRIAL)");
            holder.txtpayvalue.setText("$2.49/WEEK,BILLED MONTHLY");
            holder.relmain.setBackgroundResource(R.drawable.monthbox);
            holder.txtpay.setTextColor(context.getResources().getColor(R.color.black));
            holder.txtpayvalue.setTextColor(context.getResources().getColor(R.color.black));
        }else if (position==1){
            holder.txtpay.setText("$"+catemodels.get(position).getPay_value()+"/"+catemodels.get(position).getPay_name());
            holder.txtpayvalue.setText("$1.73/WEEK,BILLED ANNUALLY");
            holder.txtpay.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtpayvalue.setTextColor(context.getResources().getColor(R.color.white));
            holder.relmain.setBackgroundResource(R.drawable.yearbox);
        }else if (position==2){
            holder.txtpay.setText("$"+catemodels.get(position).getPay_value()+"-"+catemodels.get(position).getPay_name());
            holder.txtpayvalue.setText("PAY ONCE UNLIMITED APP ACCESS FOREVER");
            holder.txtpay.setTextColor(context.getResources().getColor(R.color.white));
            holder.txtpayvalue.setTextColor(context.getResources().getColor(R.color.white));
            holder.relmain.setBackgroundResource(R.drawable.lifebox);
        }else {
            holder.txtpay.setText("$"+catemodels.get(position).getPay_value()+"/"+catemodels.get(position).getPay_name());
            holder.txtpayvalue.setText("$2.49/WEEK,BILLED MONTHLY");
            holder.txtpay.setTextColor(context.getResources().getColor(R.color.black));
            holder.txtpayvalue.setTextColor(context.getResources().getColor(R.color.black));
            holder.relmain.setBackgroundResource(R.drawable.monthbox);
        }
    }
    @Override
    public int getItemCount() {
        return catemodels.size();
    }
}



