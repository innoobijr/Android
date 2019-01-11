package com.bitrider.survey.SignIn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.ActivityListener.Networking.SignUpService;
import com.bitrider.survey.QueryPreferences;
import com.bitrider.survey.R;
import com.bitrider.survey.TrackerActivity;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class SignStepStatusFragment extends Fragment implements BlockingStep {

    boolean check = false;
    private final static String TAG = SignStepStatusFragment.class.getSimpleName();
    RadioGroup userStatus;
    StepperLayout mStepperLayout;
    int userStatusInt = 0;
    EditText name;
    TextView t;
    private int permission_count;
    private static final int PERMISSIONS_LOCATION= 1;
    private static final int PERMISSIONS_NETWORK=2;
    private static final int PERMISSIONS_READ_PHONE_STATE=3;
    private static final int PERMISSIONS_INTERNET=4;
    private static final int PERMISSIONS_ACTIVITY_RECOGNITION=5;

    public static SignStepStatusFragment newInstance() {
        SignStepStatusFragment fragment = new SignStepStatusFragment();
        return fragment;
    }

    DataManager dataManager;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataManager) {
            dataManager = (DataManager) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DataManager");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataManager = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.sign_in_one, container, false);
        mStepperLayout = (StepperLayout) getActivity().findViewById(R.id.stepperLayout);
        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        t = v.findViewById(R.id.statusQ);
        v.findViewById(R.id.main4).setVisibility(View.VISIBLE);
        userStatus = (RadioGroup) v.findViewById(R.id.userStatus);
        userStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.driverRadio){
                    userStatusInt = 1;
                    mStepperLayout.setNextButtonEnabled(true);
                    Log.i(TAG, "Driver checked");
                } else {
                    userStatusInt = 2;
                    mStepperLayout.setNextButtonEnabled(true);
                    Log.i(TAG, "Passenger checked");
                }
            }
        });
        return v;
    }

    @Override
    public VerificationError verifyStep() {
       if(userStatusInt == 0){
           return new VerificationError("Please choose a status");
       }
        return null;
    }

    @Override
    public void onSelected(){
      // t.setText(dataManager.getData());
        String i = (String) dataManager.getName();
        mStepperLayout.setNextButtonEnabled(false);
        Log.e(TAG, "Data Manager: Stinrg --- " + i);


    }

    public boolean hasPermission(){
        return( ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);

    }

    private void checkGPSEnabled() {
        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "Please enable location services", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        //Check location permission is granted = if is, start
        //the service, otherwise request the permission

        //Init the permissions
        boolean has_phoneStatePermission = false;
        boolean has_internetPermission = false;
        boolean has_locationPermisssion = false;

        int permissions_code = 42;
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};

        if (!hasPermission()) {
            Log.e(TAG, "SEnding request");
            ActivityCompat.requestPermissions(getActivity(), permissions, permissions_code);
        }

        /**Log.d(TAG, "attemptLogin: check for READ_PHONE_STATE permissions");
         if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
         != PackageManager.PERMISSION_GRANTED) {
         // Permission is not granted
         // ask for permission

         Log.d(TAG, "attemptLogin: requesting READ_PHONE_STATE permissions");

         ActivityCompat.requestPermissions(getActivity(),
         new String[]{android.Manifest.permission.READ_PHONE_STATE},
         PERMISSIONS_READ_PHONE_STATE);
         } else {

         has_phoneStatePermission = true;
         Log.d(TAG, "attemptLogin: READ_PHONE_STATE permissions already granted");
         Log.d(TAG, "attemptLogin: check for INTERNET permissions");
         if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.INTERNET)
         != PackageManager.PERMISSION_GRANTED) {
         // Permission is not granted
         // ask for permission

         Log.d(TAG, "attemptLogin: requesting INTERNET permissions");

         ActivityCompat.requestPermissions(getActivity(),
         new String[]{Manifest.permission.INTERNET},
         PERMISSIONS_INTERNET);
         } else {

         has_internetPermission = true;
         Log.d(TAG, "attemptLogin: INTERNET permissions already granted");
         Log.d(TAG, "attemptLogin: check for LOCATION permissions");
         if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
         != PackageManager.PERMISSION_GRANTED) {
         // Permission is not granted
         // ask for permission

         Log.d(TAG, "attemptLogin: requesting LOCATION permissions");

         ActivityCompat.requestPermissions(getActivity(),
         new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
         PERMISSIONS_LOCATION);
         } else {

         has_locationPermisssion = true;
         Log.d(TAG, "attemptLogin: LOCATION permissions already granted");

         }

         }

         }

         //once both permissions are granted, then login
         if (has_internetPermission && has_phoneStatePermission && has_locationPermisssion) {
         return true;

         } else {
         return false;
         }**/
    }

    interface permissionStatus {
        boolean getStatus();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e(TAG, "Hello Logan" + permissions + " " + grantResults);
        for (String permission: permissions){
        switch (permission) {
            case Manifest.permission.READ_PHONE_STATE : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: READ_PHONE_STATE permissions received");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            case Manifest.permission.INTERNET: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: INTERNET permissions received");
                } else {

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

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
        Log.e(TAG, "Trumpet: badadadadada");

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        //String i = name.getText().toString();
        dataManager.saveUserStatus(userStatusInt);
        //Log.i(TAG, "Name111:" + i);
        //Log.i(TAG, "Name111: gurry");
        callback.goToNextStep();
        callback.getStepperLayout().hideProgress();
    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback){
        Toast.makeText(this.getContext(), "Your custom back action. Here you should cancel currently running operations", Toast.LENGTH_SHORT).show();
        callback.goToPrevStep();
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.complete();
            }
        }, 2000L);
    }

    @Override
    public void onError(@NonNull VerificationError error){
        Snackbar.make(getView(), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();


    }


}
