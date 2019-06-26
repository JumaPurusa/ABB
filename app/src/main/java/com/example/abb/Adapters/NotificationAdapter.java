package com.example.abb.Adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Message;
import com.example.abb.Model.NotificationABB;
import com.example.abb.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder>{

    private List<Message> listOfMessages;
    private Context mContext;
    private ItemClickListener itemClickListener;
    private DonorsAdapter.PopupClickListener popupClickListener;


    public void setPopupClickListener(DonorsAdapter.PopupClickListener popupClickListener) {
        this.popupClickListener = popupClickListener;
    }

    public interface PopupClickListener{
        public void popupClick(View view, int position);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NotificationAdapter(Context context, List<Message> list){
        this.mContext = context;
        this.listOfMessages = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.title.setText(listOfMessages.get(position).getTitle());
        holder.description.setText(listOfMessages.get(position).getMessage());
        holder.date.setText(listOfMessages.get(position).getSent_date());

        holder.popupIcon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(popupClickListener != null)
                            popupClickListener.popupClick(v, position);
                    }
                }
        );

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(itemClickListener != null)
                            itemClickListener.itemClick(view, position);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return listOfMessages.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView title, description, date;
        private ImageView popupIcon;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleText);
            description = itemView.findViewById(R.id.descText);
            date = itemView.findViewById(R.id.dateText);
            popupIcon = itemView.findViewById(R.id.popupIcon);

        }
    }
}
