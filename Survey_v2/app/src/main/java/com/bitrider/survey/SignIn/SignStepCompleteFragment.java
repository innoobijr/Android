package com.bitrider.survey.SignIn;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Interpolator;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
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
import com.bitrider.survey.ActivityListener.Constants;
import com.bitrider.survey.ActivityListener.Networking.LoginToFirebaseWToken;
import com.bitrider.survey.ActivityListener.Networking.SignUpService;
import com.bitrider.survey.GeoDB.GeoBean;
import com.bitrider.survey.GeoDB.PlaceViewModel;
import com.bitrider.survey.QueryPreferences;
import com.bitrider.survey.R;
import com.bitrider.survey.TrackerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SignStepCompleteFragment extends Fragment implements BlockingStep {

    boolean check = false;
    RadioGroup group;
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int REQUEST_INTERNET = 2;
    private static final int REQUEST_LOCATION = 3;

    private boolean signUpSuccess = false;

    private StepperLayout mStepperLayout;


    private FirebaseAuth mAuth;
    File localFile;
    JSONObject json;
    private PlaceViewModel placeViewModel;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    private Handler mHandler;
    ThreadPoolExecutor executor;
    ProgressBar progressBar;
    LoginToFirebaseWToken lg = new LoginToFirebaseWToken();
    ImageView signUpIcon;
    TextView signUpText;
    Button tryAgain;
    ViewGroup mContainer;
    BroadcastReceiver broadcastReceiver;
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
        v.findViewById(R.id.main3).setVisibility(View.VISIBLE);

        mStepperLayout = (StepperLayout) getActivity().findViewById(R.id.stepperLayout);


        mContainer = container;
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //check = (RadioButton) v.findViewById(R.id.check);
        //check.setId((int)9);
        signUpIcon = (ImageView) v.findViewById(R.id.signInIcon);
        progressBar = (ProgressBar) v.findViewById(R.id.signInIconProgress);
        signUpText = (TextView) v.findViewById(R.id.signInText);
        tryAgain = (Button) v.findViewById(R.id.try_again);

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();


        int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(
                NUMBER_OF_CORES*2,
                NUMBER_OF_CORES*2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.SIGN_UP_STATE) && intent.getIntExtra("messenger", 7) == 0){
                    //TODO: Control for Token:none, State: false
                    //TODO: commenting out to add to stepper
                    Log.e(TAG, "Try Again");
                    boolean state = intent.getBooleanExtra("state", false);
                    String token = intent.getStringExtra("token");

                    Log.e(TAG, "Try Again: " + state);


                    if (state) {
                        if(signUpText.getVisibility() == View.INVISIBLE) {
                            signUpText.setVisibility(View.VISIBLE);
                        }
                        signUpText.setText("Logging in...");
                        QueryPreferences.setToken(getContext(), token);
                        loginToFb(token);

                    } else {
                        //TODO: Display error and show try again button
                        progressBar.setVisibility(View.INVISIBLE);
                        signUpText.setVisibility(View.INVISIBLE);
                        tryAgain.setVisibility(View.VISIBLE);
                        mStepperLayout.setCompleteButtonEnabled(false);

                    }
                    //Toast.makeText(getContext(), "Received the message", Toast.LENGTH_SHORT).show();
                    //sendit(intent.getBooleanExtra("state", false), intent.getStringExtra("token"));
                }
            }
        };

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(Constants.SIGN_UP_STATE));

        return v;
    }

    public void loginToFb(String token){
        Task<AuthResult> task =  mAuth.signInWithCustomToken(token);
        Log.d(TAG, "Calling sign-in task: with token: " + token);

        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String name = dataManager.getName();
                    String status;
                    switch(dataManager.getUserStatus()){
                        case 1:
                            status = "Driver";
                            break;
                        case 2:
                            status = "Passenger";
                            break;
                        default:
                            status = "Other";
                    }
                    String uid = mAuth.getCurrentUser().getUid();
                    myRef = database.getReference("users/" + uid + "/credentials");
                    QueryPreferences.setUsername(getContext(), name);
                    QueryPreferences.setUserstatus(getContext(), status);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Name", name);
                    childUpdates.put("Status", status);
                    myRef.setValue(childUpdates);

                    Log.d(TAG, "firebase auth success");
                    Log.d(TAG, "signInWithCustomToken:success");
                    signUpText.setText("Configuring the application...");
                    user = mAuth.getCurrentUser();
                    runStorage();

                    //boolean bool = true;
                } else {
                    //Does something first and then destroys the app
                    Log.d(TAG, "firebase auth fail");
                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                    attemptLogin();

                    //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                    //startActivity(intent);
                    //unregisterReceiver(stopReceiver);
                    //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                    //startActivity(intent);
                    //stopSelf();
                }
            }
        });
    }

    public void runStorage() {
        Log.i(TAG,";local tem file created  created ");
        HandlerThread handlerThread = new HandlerThread("Handler");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg){}

        };
        executor.execute(new Runnable() {
            @Override
            public void run() {
                    try {
                        StorageReference firebaseRef = mStorageRef.child("files/places_list.json");
                        localFile = File.createTempFile("innocent", ".json");
                        firebaseRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        // Successfully downloaded data to local file
                                        Log.i(TAG, "success " + localFile.getName());
                                        String name = localFile.getName();
                                        loadJSONFromAsset(name);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                Log.e(TAG, "fail ");
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "bad fail ");

                    }
            }
        });

        //Log.i(TAG, "File path: " + localFile.getAbsolutePath());
    }

    public void loadJSONFromAsset(String name){
        placeViewModel = ViewModelProviders.of(this).get(PlaceViewModel.class);
        String jj = null;
        try {
            InputStream in = new FileInputStream(new File(getActivity().getCacheDir(), name));
            //FileReader fn = new FileReader(localFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            //BufferedReader reader = new BufferedReader(fn);
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            Log.i(TAG, out.toString());   //Prints the string content read from input stream
            reader.close();
            json = new JSONObject(out.toString());
            String jsonObject = json.get("50").toString();
            Log.i(TAG, "json: " + jsonObject.getClass());

            Gson gson = new Gson();
            Type collectionType = new TypeToken<HashMap<String, GeoBean>>(){}.getType();

            HashMap<String, GeoBean> beans = gson.fromJson(json.toString(), collectionType);
            Log.i(TAG, "somebody's bean: " + beans.values().size());
            List<GeoBean> places = new ArrayList<>(beans.values());
            Log.i(TAG,"Places size is " + places.size());
            placeViewModel.insert(places);
            QueryPreferences.setStopDownloaded(getActivity(), true);
            progressBar.setVisibility(View.INVISIBLE);
            signUpIcon.setVisibility(View.VISIBLE);
            signUpText.setText("Sign Up Complete!");
            if(tryAgain.getVisibility() == View.VISIBLE){
                tryAgain.setVisibility(View.INVISIBLE);
                signUpIcon.setVisibility(View.VISIBLE);
                signUpText.setVisibility(View.VISIBLE);
            }
            signUpSuccess = true;
            mStepperLayout.setCompleteButtonEnabled(true);
            executor.shutdownNow();
            View v = getLayoutInflater().inflate(R.layout.sign_in_one, null, true);

            //for(int i = 0; i < places.size(); i++){ placeViewModel.insert(places.get(i));}

            //jj = "" + json;
        } catch(Exception e){
            e.printStackTrace();
            Log.i(TAG, "somebody save error: ");
            //return null;
        }
        //return json;
    }

    @Override
    public VerificationError verifyStep() {
        if(signUpSuccess == false){
            return new VerificationError("Sign up failed. Try again.");
        }
       return null;
    }

    @Override
    public void onSelected(){
        mStepperLayout.setCompleteButtonEnabled(false);
        //TODO: Check if there is network connection before running command.
        String token = QueryPreferences.getToken(getContext());
        if(token.length() > 5){
            Log.e(TAG, "Markings");
            loginToFb(token);
        } else {
            //TODO attempt login should check for network
            attemptLogin();
        }
    }


    @Override
    @UiThread
    public void onNextClicked(final StepperLayout.OnNextClickedCallback callback) {
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
        //progressBar.setVisibility(View.VISIBLE);
        //mRequestQueue = Volley.newRequestQueue(getContext());
        Log.d(TAG, "set login button listener");
        //progressBar.setVisibility(View.VISIBLE);
        //attemptLogin();
        //attemptLogin(callback, mAuth);
        //signUpText.setVisibility(View.VISIBLE);
        //signUpIcon.setVisibility(View.VISIBLE);
        QueryPreferences.setUserAccountCreated(getContext(), true);
        callback.complete();
        Log.i(TAG, "Login process completed");
    }

    @Override
    public void onError(@NonNull VerificationError error){
        Snackbar.make(getView(), error.getErrorMessage(), Snackbar.LENGTH_SHORT).show();

    }

    public void attemptLogin(){
        if(progressBar.getVisibility() == View.INVISIBLE){
            progressBar.setVisibility(View.VISIBLE);
        }
        if(tryAgain.getVisibility() == View.VISIBLE){
            tryAgain.setVisibility(View.INVISIBLE);
        }
        Intent i = new Intent(getActivity(), SignUpService.class);
        i.putExtra("messenger", 0);
        getActivity().startService(i);
        Log.e(TAG, "Markings");
    }



}
