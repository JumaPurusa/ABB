package com.example.abb.Activities;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.abb.Dialogs.PhotoUploadSelectionDialog;
import com.example.abb.R;
import com.example.abb.Utils.Permissions;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getName();

    private static final int VERIFY_PERMISSION_REQUEST = 0;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_profile);

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
    }

    private void onImageViewClicked(){
        imageView = findViewById(R.id.profile_image);
        imageView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PhotoUploadSelectionDialog dialog = new PhotoUploadSelectionDialog();
                        dialog.show(getSupportFragmentManager(), "dialog");
                    }
                }
        );
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
}
