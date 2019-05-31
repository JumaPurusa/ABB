package com.example.abb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Donor;
import com.example.abb.R;

import java.util.List;

public class SpinnerViewAdapter extends RecyclerView.Adapter<SpinnerViewAdapter.MyViewHolder>{

    private List<Donor> donors;
    private Context mContext;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public SpinnerViewAdapter(Context context, List<Donor> donors){
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

        myViewHolder.textView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fading_transition_animation));
        myViewHolder.textView.setText(donors.get(position).getName());

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

        private TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView);
        }
    }
}
