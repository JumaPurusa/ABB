package com.example.abb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Donor;
import com.example.abb.R;

import java.util.List;

public class DonorsAdapter extends RecyclerView.Adapter<DonorsAdapter.MyViewHolder>{

    private List<Donor> donors;
    private Context mContext;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DonorsAdapter(Context context, List<Donor> donors){
        this.mContext = context;
        this.donors = donors;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        Donor donor = donors.get(position);
        //myViewHolder.nameText.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fading_transition_animation));
        myViewHolder.nameText.setText(donor.getName());

        myViewHolder.itemView.setOnClickListener(
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
        return donors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nameText;
        private ImageView callIcon, smsIcon, emailIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

             nameText = itemView.findViewById(R.id.nameText);
             callIcon = itemView.findViewById(R.id.callIcon);
             smsIcon = itemView.findViewById(R.id.smsIcon);
             emailIcon = itemView.findViewById(R.id.emailIcon);
        }
    }
}
