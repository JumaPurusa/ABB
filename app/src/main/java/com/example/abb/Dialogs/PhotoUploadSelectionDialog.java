package com.example.abb.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.abb.Activities.Login;
import com.example.abb.Adapters.PhotoUploadSelectionAdapter;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Selection;
import com.example.abb.R;
import com.example.abb.Utils.DialogDisplay;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class PhotoUploadSelectionDialog extends DialogFragment {


    private final static String TAG = PhotoUploadSelectionDialog.class.getName();
    public final static int CAMERA_REQUEST_CODE = 1;
    public final static int GALLERY_REQUEST_CODE = 2;
    private Context mContext;

    private CameraPhoto cameraPhoto;
    private GalleryPhoto galleryPhoto;

    private RecyclerView recyclerView;
    private PhotoUploadSelectionAdapter adapter;

    public OnImageReturnToActivity onImageReturnToActivity;

    public void setOnImageReturn(OnImageReturnToActivity onImageReturn) {
        this.onImageReturnToActivity = onImageReturn;
    }

    public PhotoUploadSelectionDialog(){

    }

    @SuppressLint("ValidFragment")
    public PhotoUploadSelectionDialog(Activity activity){
        this.mContext = activity;
    }

    public interface OnImageReturnToActivity{

        public void onImageReturn(String photoPath);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
                            galleryPhoto = new GalleryPhoto(getActivity());
                            getActivity().startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST_CODE);
                        }


                        if(selection.getPosition() == 1){
                            /*
                            cameraPhoto = new CameraPhoto(getActivity());
                            try {
                                getActivity().startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST_CODE);
                                cameraPhoto.addToGallery();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            */
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                            //camera.setTextColor(Color.parseColor("#E6392F"));
                            //camera.setSelected(true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){

                try{
                    if(data.hasExtra("data")){
                       // Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        //Bitmap bitmap = ImageLoader.init().from((String)data.getExtras().get("data"))
                                //.requestSize(96,96).getBitmap();
                        onImageReturnToActivity.onImageReturn(String.valueOf(data.getData()));
                        Toast.makeText(mContext, String.valueOf(data.getData()), Toast.LENGTH_SHORT).show();

                    }

                }catch (NullPointerException e){

                }
            }

            if(requestCode == GALLERY_REQUEST_CODE){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();

                //Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(96,96).getBitmap();
                onImageReturnToActivity.onImageReturn(photoPath);
                Toast.makeText(mContext, photoPath, Toast.LENGTH_SHORT).show();
            }
        }

    }
}
