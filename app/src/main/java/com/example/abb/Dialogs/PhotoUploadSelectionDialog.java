package com.example.abb.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abb.Activities.Login;
import com.example.abb.Adapters.PhotoUploadSelectionAdapter;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Selection;
import com.example.abb.R;
import com.example.abb.Utils.DialogDisplay;

public class PhotoUploadSelectionDialog extends DialogFragment {


    private final  static String TAG = PhotoUploadSelectionDialog.class.getName();


    private RecyclerView recyclerView;
    private PhotoUploadSelectionAdapter adapter;

    public PhotoUploadSelectionDialog(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_upload_selection_dialog, container, false);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogScale;
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);

        adapter = new PhotoUploadSelectionAdapter(getContext(), Selection.selections);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setItemClickListener(
                new ItemClickListener() {
                    @Override
                    public void itemClick(View view, int position) {

                        Selection selection = Selection.selections[position];
                        if(selection.getPosition() == 0){

                        }

                        if(selection.getPosition() == 1){

                        }

                        getDialog().dismiss();
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

}
