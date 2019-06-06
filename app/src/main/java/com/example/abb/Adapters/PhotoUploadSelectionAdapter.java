package com.example.abb.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Selection;
import com.example.abb.R;

public class PhotoUploadSelectionAdapter extends RecyclerView.Adapter<PhotoUploadSelectionAdapter.SelectionViewHolder>{

    private Context mContext;
    private Selection[] mSelections;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PhotoUploadSelectionAdapter(Context context, Selection[] selections){
        this.mContext = context;
        this.mSelections = selections;
    }

    @Override
    public SelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.selection_layout, parent, false);
        return new SelectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectionViewHolder holder, final int position) {

        Selection action = mSelections[position];

        holder.textView.setText(action.getActionText());

        int resID = mContext.getResources().getIdentifier(action.getActionImage(),
                "drawable", mContext.getPackageName());
        holder.imageView.setImageResource(resID);

        holder.layout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.itemClick(v, position);
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return mSelections.length;
    }


    public class SelectionViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout layout;
        private ImageView imageView;
        private TextView textView;

        public SelectionViewHolder(View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.action_relative);
            imageView = itemView.findViewById(R.id.action_image);
            textView = itemView.findViewById(R.id.action_text);

        }
    }
}
