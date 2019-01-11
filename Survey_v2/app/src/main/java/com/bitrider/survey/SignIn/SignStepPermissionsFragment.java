package com.bitrider.survey.SignIn;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import static android.content.Context.LOCATION_SERVICE;

public class SignStepPermissionsFragment extends Fragment implements BlockingStep {

    boolean check = false;
    private StepperLayout mStepperLayout;
    RadioGroup group;
    EditText name;
    Boolean permissionReady;
    private final static String TAG = SignStepPermissionsFragment.class.getSimpleName();

    public static SignStepPermissionsFragment newInstance() {
        SignStepPermissionsFragment fragment = new SignStepPermissionsFragment();
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
        View v = inflater.inflate(R.layout.permissions_stepper, container, false);
        mStepperLayout = (StepperLayout) getActivity().findViewById(R.id.stepperLayout);


        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        Button b = (Button) v.findViewById(R.id.permission_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGPSEnabled();
            }
        });
        return v;
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
            mStepperLayout.setNextButtonEnabled(true);
        } else {
            mStepperLayout.setNextButtonEnabled(true);
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

    public boolean hasPermission(){
        return( ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED);

    }


    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected(){
        mStepperLayout.setNextButtonEnabled(false);
    }

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        //callback.getStepperLayout().showProgress("Operation in progress, please wait...");

        boolean i = dataManager.getPermissionStatus();
        //dataManager.saveData(i);
        if (i == true) {
            Log.i(TAG, "Name111:" + i);
            //Log.i(TAG, "Name111: gurry");
            callback.goToNextStep();
            //callback.getStepperLayout().hideProgress();
        } else {
            //TODO: inform user to accept all permissions.
        }

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

    }



}
