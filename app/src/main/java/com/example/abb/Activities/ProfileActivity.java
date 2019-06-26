package com.example.abb.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abb.Adapters.PhotoUploadSelectionAdapter;
import com.example.abb.Dialogs.PhotoUploadSelectionDialog;
import com.example.abb.Interfaces.ItemClickListener;
import com.example.abb.Model.Selection;
import com.example.abb.Model.User;
import com.example.abb.R;
import com.example.abb.Utils.Permissions;
import com.google.gson.Gson;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getName();

    private static final int VERIFY_PERMISSION_REQUEST = 0;
    private ImageView imageView;
    private TextView usernameText, emailText, passwordText, phoneText;

    public final static int CAMERA_REQUEST_CODE = 1;
    public final static int GALLERY_REQUEST_CODE = 2;

    private static CameraPhoto cameraPhoto;
    private static GalleryPhoto galleryPhoto;

    private static RecyclerView recyclerView;
    private static PhotoUploadSelectionAdapter adapter;
    private static SharedPreferences sharedPreferences;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);

        String userJsonString = sharedPreferences.getString("profile", null);
        if(userJsonString != null);
        user = new Gson().fromJson(userJsonString, User.class);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            onImageViewClicked();
        }else{
            verifyingPermission(Permissions.PERMISSIONS);
        }

        onSettingWidgets();
    }

    private void onImageViewClicked(){
        imageView = findViewById(R.id.profile_image);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //PhotoUploadSelectionDialog dialog = new PhotoUploadSelectionDialog(ProfileActivity.this);
                        //dialog.show(getSupportFragmentManager(), "dialog");
                        SelectUploadDialog dialog = new SelectUploadDialog();
                        dialog.show(getSupportFragmentManager(), "dialog");
                    }
                }
        );
    }

    private void onSettingWidgets(){

        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        passwordText = findViewById(R.id.passwordText);
        phoneText = findViewById(R.id.phoneText);

        usernameText.setText(user.getUsername());
        emailText.setText(user.getEmail());
        passwordText.setText("Password");
        phoneText.setText(user.getPhone());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void verifyingPermission(String[] permissions){
        Log.d(TAG, "verifyingPermission: " + permissions);

        ActivityCompat.requestPermissions(
                ProfileActivity.this,
                permissions,
                VERIFY_PERMISSION_REQUEST
        );
    }
    private boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permission array");

        for(int i=0; i<permissions.length; i++){
            String check = permissions[i];
            if (!isCheckedPermission(check))
                return false;
        }

        return true;
    }

    private boolean isCheckedPermission(String permission){
        Log.d(TAG, "isCheckPermission: Checking a permission = " + permission);

        int requestPermission = ActivityCompat.checkSelfPermission(getApplicationContext(), permission);

        if(requestPermission != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "isCheckPermission: " + permission + " permission is not granted");
            return false;
        }else{
            Log.d(TAG, "isCheckedPermission: " + permission + " permission is granted");
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){

                try{
                    if(data != null && data.hasExtra("data")){
                        //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        //Bitmap bitmap = ImageLoader.init().from(String.valueOf(data.getExtras().get("data")))
                        //.requestSize(96,96).getBitmap();
                        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                        imageView.setImageBitmap(bitmap);
                        //Toast.makeText(ProfileActivity.this, String.valueOf(data.getData()), Toast.LENGTH_SHORT).show();

                    }

                }catch (NullPointerException e){

                }
            }

            if(requestCode == GALLERY_REQUEST_CODE){
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(96,96).getBitmap();
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(ProfileActivity.this, photoPath, Toast.LENGTH_SHORT).show();
            }
        }

    }


    public static class SelectUploadDialog extends DialogFragment{

        private RecyclerView recyclerView;
        private PhotoUploadSelectionAdapter adapter;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_photo_upload_selection_dialog, container, false);

            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogScale;

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
    }

}
