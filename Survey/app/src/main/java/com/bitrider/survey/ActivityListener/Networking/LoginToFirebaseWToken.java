package com.bitrider.survey.ActivityListener.Networking;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.concurrent.TimeUnit;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bitrider.survey.QueryPreferences;
import com.bitrider.survey.TrackerActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.geojson.Point;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.mapbox.turf.TurfClassification.nearestPoint;

public class LoginToFirebase{

    private final static String TAG = LoginToFirebase.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;

    private FirebaseUser user;
    String uid;

    public boolean loginToFirebase(FirebaseUser u, String e, String p) {
        String email = e;
        String password = p;
        user = u;

        //mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "Current user? --- " + user);

        Log.d(TAG, "firebase auth try");

        /**if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
            user = null;
            Log.i(TAG, "GEtting auth");
        } else {
            user = mAuth.getCurrentUser();
            Log.i(TAG, "I got the user + " + user);
        }**/

        if (user == null ){


        /**mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    signedIn = true;
                    //boolean bool = true;
                    Log.d(TAG, "firebase auth success");
                } else {
                    //Does something first and then destroys the app
                    Log.d(TAG, "firebase auth failed");
                    signedIn = false;
                    //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                    //startActivity(intent);
                    //unregisterReceiver(stopReceiver);
                    //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                    //startActivity(intent);
                    //stopSelf();
                }
                }
        });**/
        ExecutorService doer = Executors.newSingleThreadExecutor();
        Future<Boolean> future = doer.submit(new Login(email, password));
        Boolean signedIn = false;

        try{
            signedIn = future.get(10000, TimeUnit.MILLISECONDS);
            Log.i(TAG, "Automatic procedd and ready to push");
        } catch (InterruptedException | ExecutionException | TimeoutException t ) {
            //TODO: need to address this IS A BUG
            t.printStackTrace();
            //throw new RuntimeException(t);

        }
        return signedIn;
        } else {
            Log.i(TAG, "Return true");
            return true;

        }
    }

    class Login implements Callable<Boolean> {
        private static final String TAG = "Callable";
        public boolean mIsSuccessful;
        private FirebaseAuth mAuth;


        String email;
        String password;

        public Login(String e, String p) {
            this.email = e;
            this.password = p;
            this.mAuth = FirebaseAuth.getInstance();
        }

        @Override
        public Boolean call() throws Exception {
            Task<AuthResult> task = mAuth.signInWithEmailAndPassword(email, password);
            Log.d(TAG, "Calling task" + email + " : " + password);

            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mIsSuccessful = true;
                        Log.d(TAG, "firebase auth success");

                        //boolean bool = true;
                    } else {
                        //Does something first and then destroys the app
                        mIsSuccessful = false;
                        Log.d(TAG, "firebase auth fail");
                        //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                        //startActivity(intent);
                        //unregisterReceiver(stopReceiver);
                        //Intent intent = new Intent(TrackerService.this, Main3Activity.class);
                        //startActivity(intent);
                        //stopSelf();
                    }
                }
            });

            while (!task.isComplete()){
                task.isComplete();
            }

            Boolean result = task.isSuccessful();

            return result;
        }
    }
}
