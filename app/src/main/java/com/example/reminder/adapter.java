package com.example.reminder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.myViewHolder> {

    Context mcontext;
    List<item> mdata;

    public adapter(Context mcontext, List<item> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View v = inflater.inflate(R.layout.card_item,parent,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        holder.r_date.setText(mdata.get(position).getDate());
        holder.r_day.setText(mdata.get(position).getDay());
        holder.r_month.setText(mdata.get(position).getMonth());
        holder.r_desc.setText(mdata.get(position).getDesc());
        holder.r_time.setText(mdata.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        TextView r_date,r_day,r_month,r_desc,r_time;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            r_date = itemView.findViewById(R.id.r_date);
            r_day = itemView.findViewById(R.id.r_day);
            r_month = itemView.findViewById(R.id.r_month);
            r_desc = itemView.findViewById(R.id.r_desc);
            r_time = itemView.findViewById(R.id.r_time);
        }
    }
}

