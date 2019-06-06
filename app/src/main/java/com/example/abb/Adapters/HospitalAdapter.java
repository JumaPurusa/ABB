package com.example.abb.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Hospital;
import com.example.abb.R;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder>{

    // listener from BecomeDonor Activity that is registered for each list item
    //private final View.OnClickListener clickListener;
    private ItemClickListener itemClickListener;

    private List<Hospital> hospitals;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HospitalAdapter(List<Hospital> hospitals){
        this.hospitals = hospitals;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.hospital_list_item,
                parent,
                false
        );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.hospitalNameText.setText(hospitals.get(position).getHospitalName());

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemClickListener != null)
                            itemClickListener.itemClick(v, position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        if (hospitals != null)
            return  hospitals.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView hospitalNameText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalNameText = itemView.findViewById(R.id.hospitalText);
        }
    }
}
