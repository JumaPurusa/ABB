package com.example.abb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abb.Model.IntroItem;
import com.example.abb.R;

import java.util.List;

public class IntroScreenPageAdapter extends PagerAdapter {

    private Context mContext;
    private List<IntroItem> mData;

    public IntroScreenPageAdapter(Context context, List<IntroItem> list){
        this.mContext = context;
        this.mData = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_screen, null);

        ImageView introImage = view.findViewById(R.id.intro_img);
        TextView introTitle = view.findViewById(R.id.intro_title);
        TextView introDesc = view.findViewById(R.id.intro_desc);

        introImage.setImageResource(mData.get(position).getImage());
        introTitle.setText(mData.get(position).getIntroTitle());
        introDesc.setText(mData.get(position).getIntroDesc());

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
