package com.bitrider.survey.SignIn;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bitrider.survey.ActivityListener.Networking.SignUpService;
import com.bitrider.survey.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SignStepCompleteFragment extends Fragment implements BlockingStep {

    boolean check = false;
    RadioGroup group;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int REQUEST_INTERNET = 2;
    private static final int REQUEST_LOCATION = 3;


    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    ImageView signUpIcon;
    TextView signUpText;
    private FirebaseUser mUser;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StepperLayout.OnCompleteClickedCallback mCallback;

    private String login_url = "https://bitdanfo-auth-heroku.herokuapp.com/";

    private RequestQueue mRequestQueue;

    private boolean tokenResponseReceived = true;
    private byte[] mToken;
    private String mHeader_Info = "";
    private String mHeader_User = "";
    View v;

    public static SignStepCompleteFragment newInstance() {
        SignStepCompleteFragment fragment = new SignStepCompleteFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.sign_in_one, container, false);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        v.findViewById(R.id.main3).setVisibility(View.VISIBLE);
        progressBar = (ProgressBar) v.findViewById(R.id.signInIconProgress);
        signUpIcon = (ImageView) v.findViewById(R.id.signInIcon);
        signUpText = (TextView) v.findViewById(R.id.signInText);
        return v;
    }

    @Override
    public VerificationError verifyStep() {
       return null;
    }

    @Override
    public void onSelected(){

    }

    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
        callback.getStepperLayout().showProgress("Operation in progress, please wait...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.goToNextStep();
                callback.getStepperLayout().hideProgress();
            }}, 2000L);

    }

    @Override
    public void onBackClicked(final StepperLayout.OnBackClickedCallback callback){
        Toast.makeText(this.getContext(), "Your custom back action. Here you should cancel currently running operations", Toast.LENGTH_SHORT).show();
        callback.goToPrevStep();
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback){
        progressBar.setVisibility(View.VISIBLE);
        //mRequestQueue = Volley.newRequestQueue(getContext());
        Log.d(TAG, "set login button listener");
        //progressBar.setVisibility(View.VISIBLE);
        //attemptLogin();
        //attemptLogin(callback, mAuth);
        //signUpText.setVisibility(View.VISIBLE);
        //signUpIcon.setVisibility(View.VISIBLE);
        Log.i(TAG, "Login process completed");
    }

    @Override
    public void onError(@NonNull VerificationError error){

    }

    class TokenRequest extends Request<byte[]> {

        private Response.Listener<String> mListener;

        public TokenRequest(String url, Response.Listener<String> listener, Response.ErrorListener elistener) {

            super(Request.Method.GET, url, elistener);
            mListener = listener;
        }

        @Override
        protected Response<byte[]> parseNetworkResponse( NetworkResponse response ){

            tokenResponseReceived = true;

            //check status code
            if (response.statusCode == 200){

                mHeader_User = response.headers.get("User");
                Log.d("mHeader_User " + mHeader_User, TAG);
            }

            mHeader_Info = response.headers.get("Info");
            Log.d("mHeader_Info " + mHeader_Info, TAG);

            mToken = response.data;
            Log.d("token length" + mToken.length, TAG);

            return Response.success(response.data, null);
        }

        @Override
        protected void deliverResponse( byte[] response ){

            //token is many bytes long
            if ( response.length > 10 ) {
                mListener.onResponse("good_response");
            }
        }
    }


    protected void sendTokenRequest( String deviceId, StepperLayout.OnCompleteClickedCallback callback, FirebaseAuth mAuth){

        mRequestQueue.cancelAll(TAG);

        //ensures only 1 token request is sent at a time
        //  if ( tokenResponseReceived ) {

        String requestUrl = login_url + "/+" + deviceId;
        Log.d(requestUrl, TAG);


        TokenRequest tokenRequest = new TokenRequest(requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response is here too: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        tokenRequest.setTag(TAG);

        Log.d( TAG, "call sendTokenRequest");
        mRequestQueue.add(tokenRequest);
        tokenResponseReceived = false;

        //}
    }



    public void login(StepperLayout.OnCompleteClickedCallback callback, FirebaseAuth mAuth){

        Log.d(TAG, "login: get deviceID");

        String deviceId;

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            deviceId  =  telephonyManager.getImei();
        } else {
            deviceId =  telephonyManager.getDeviceId();
        }

        if (deviceId == null){
            Toast.makeText(getContext(), "Retrieval of device ID failed", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "deviceID retrieved: " + deviceId);
        sendTokenRequest(deviceId, callback, mAuth);

    }

    public void attemptLogin(){

        boolean has_phoneStatePermission = false;
        boolean has_internetPermission = false;
        boolean has_locationPermisssion = false;

        Log.d(TAG, "attemptLogin: check for READ_PHONE_STATE permissions");
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // ask for permission

            Log.d(TAG, "attemptLogin: requesting READ_PHONE_STATE permissions");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {

            has_phoneStatePermission = true;
            Log.d(TAG, "attemptLogin: READ_PHONE_STATE permissions already granted");

        }

        Log.d(TAG, "attemptLogin: check for INTERNET permissions");
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // ask for permission

            Log.d(TAG, "attemptLogin: requesting INTERNET permissions");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_INTERNET);
        } else {

            has_internetPermission = true;
            Log.d(TAG, "attemptLogin: INTERNET permissions already granted");

        }

        Log.d(TAG, "attemptLogin: check for LOCATION permissions");
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // ask for permission

            Log.d(TAG, "attemptLogin: requesting LOCATION permissions");

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {

            has_locationPermisssion = true;
            Log.d(TAG, "attemptLogin: LOCATION permissions already granted");

        }

        //once both permissions are granted, then login
        if (has_internetPermission && has_phoneStatePermission && has_locationPermisssion) {
            getActivity().startService(new Intent(getActivity(), SignUpService.class));

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE: {
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
            case REQUEST_INTERNET: {
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
            case REQUEST_LOCATION: {
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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
