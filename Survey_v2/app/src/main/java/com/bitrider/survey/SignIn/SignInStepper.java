package com.bitrider.survey.SignIn;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bitrider.survey.ActivityListener.Constants;
import com.bitrider.survey.NewReport.NewReportAdapter;
import com.bitrider.survey.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class SignInStepper extends AppCompatActivity implements DataManager, StepperLayout.StepperListener {

    private StepperLayout mStepperLayout;
    private final static String TAG = SignInStepper.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;
    DataManager dataManager;
    int userStatus;

    String data = null;
    boolean permissionStatus = false;
    public static final String CURRENT_STEP_POSITION_KEY = "position_key";
    private static final String EXTRA_REPLY = "siginInStepper";
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;



    @Override
    public void onCompleted(View completeButton) {
        sendit(true, "none");
    }

    @Override
    public void onError(VerificationError verificationError) {
        //Toast.makeText(this, "onError! -> " + verificationError.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Toast.makeText(this, "onStepSelected! -> " + newStepPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReturn() {
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_stepper);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setListener(this);
        dataManager = (DataManager) this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //((Main3Activity) getActivity()).setSupportActionBar(toolbar);
        //((Main3Activity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inflate a menu to be displayed in the toolbar
        toolbar.setNavigationIcon(R.drawable.ic_x_unchecked);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Snackbar.make(v, "Back", Snackbar.LENGTH_SHORT).show();
                //TODO: Goes back!

            }
        });

        toolbar.setTitle("create a new account");
        toolbar.setTitleMarginStart(0);
        toolbar.setTitleTextAppearance(this, R.style.ToolBarFontTwo);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.coloYellow));
        int startingStep;

        //TODO:Change to SignInFragmentAdapter
        try {
            startingStep = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
        } catch (Throwable t){
            startingStep = 0;
        }

        mStepperLayout.setAdapter(new SignStepAdapter(getSupportFragmentManager(), this), startingStep);


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.SIGN_UP_STATE)) {
                    //TODO: commenting out to add to stepper
                    //sendit(intent.getBooleanExtra("state", false), intent.getStringExtra("token"));
                }
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.SIGN_UP_STATE));
    }

    public void sendit(Boolean v, String t){
        if (v == true ) {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, "works");
            replyIntent.putExtra("token", t);
            setResult(RESULT_OK, replyIntent);
            finish();
        } else {
            Intent replyIntent = new Intent();
            setResult(RESULT_CANCELED, replyIntent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isPermitted = true;
        Log.e(TAG, "Hello Logan" + permissions + " " + grantResults);
        for (String permission : permissions) {
            switch (permission) {
                case Manifest.permission.READ_PHONE_STATE: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        Log.d(TAG, "onRequestPermissionsResult: READ_PHONE_STATE permissions received");
                    } else {
                        isPermitted = false;
                        Log.d(TAG, "onRequestPermissionsResult: READ_PHONE_STATE permissions denied");

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                case Manifest.permission.INTERNET: {
                    if (grantResults.length > 0
                            && grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        Log.d(TAG, "onRequestPermissionsResult: INTERNET permissions received");
                    } else {
                        isPermitted = false;
                        Log.d(TAG, "onRequestPermissionsResult: INTERNET permissions denied");
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                case Manifest.permission.ACCESS_NETWORK_STATE: {
                    if (grantResults.length > 0
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        Log.d(TAG, "onRequestPermissionsResult: NETWORK permissions received");
                    } else {
                        isPermitted = false;
                        Log.d(TAG, "onRequestPermissionsResult: NETWORK permissions denied");
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
                case Manifest.permission.ACCESS_FINE_LOCATION: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.
                        Log.d(TAG, "onRequestPermissionsResult: LOCATION permissions received");

                    } else {
                        isPermitted = false;
                        Log.d(TAG, "onRequestPermissionsResult: LOCATION permissions denied");
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }
            }
            if (isPermitted == false) {
                dataManager.savePermissionStatus(false);
            } else {
                dataManager.savePermissionStatus(true);
            }

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString("Name", data);
        Log.i(TAG, "Name:" + data);
        Log.i(TAG, "Name: gut check");
        outState.putInt(CURRENT_STEP_POSITION_KEY, mStepperLayout.getCurrentStepPosition());
        super.onSaveInstanceState(outState);
    }

    public void saveName(String data){
        Log.e(TAG, "Name111: " + data);
        this.data = data;
    }

    public String getName(){
        return data;
    }

    @Override
    public void savePermissionStatus(boolean status){
        Log.e(TAG, "Permission status");
        permissionStatus = status;
    }

    public int getUserStatus(){
        return userStatus;
    }

    @Override
    public void saveUserStatus(int status) {
        userStatus = status;
    }

    @Override
    public boolean getPermissionStatus() {
        return permissionStatus;
    }
}
