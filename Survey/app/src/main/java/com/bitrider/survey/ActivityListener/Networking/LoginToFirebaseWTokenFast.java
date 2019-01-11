package com.bitrider.survey.ActivityListener.Networking;

import android.content.BroadcastReceiver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoginToFirebaseWToken {

    private final static String TAG = LoginToFirebaseWToken.class.getSimpleName();
    BroadcastReceiver broadcastReceiver;

    Throwable bigE;

    private FirebaseUser user;
    String uid;

    public ArrayList<Object> loginToFirebaseWToken(String t, FirebaseUser user) {
        String token = t;
        this.user = user;

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

        /**if (user == null ){**/
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
        Future<ArrayList<Object>> future = doer.submit(new Login(token));
        ArrayList<Object> signedIn = null;

        try{
            signedIn = future.get(1000, TimeUnit.MILLISECONDS);
            Log.i(TAG, "Automatic procedd and ready to push");
            Log.e(TAG, "Checking E:" + signedIn.get(1));
        } catch (Throwable te ) {
            //TODO: need to address this IS A BUG
            te.printStackTrace();
            Log.e(TAG, "hey");
            //Log.e(TAG, "InterruptedException | ExecutionException | TimeoutException: Executor Shutdown");
            //throw new RuntimeException(t);

        }
        ArrayList<Object> stuff = new ArrayList<>();
        stuff.add(signedIn.get(0));
        stuff.add(signedIn.get(1));
        return stuff;
        /** } else {
            Log.i(TAG, "Return true");
            return true;

        }**/
    }

    public class Login implements Callable<ArrayList<Object>> {
        private static final String TAG = "Callables";
        public boolean mIsSuccessful;
        private FirebaseAuth mAuth;
        private Throwable t;


        String token;

        public Login(String t) {
            this.token = t;
            this.mAuth = FirebaseAuth.getInstance();
        }

        @Override
        public ArrayList<Object> call() throws Exception {
            Task<AuthResult> task =  mAuth.signInWithCustomToken(token);
            Log.d(TAG, "Calling sign-in task: with token: " + token);

            task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mIsSuccessful = true;
                        Log.d(TAG, "firebase auth success");
                        Log.d(TAG, "signInWithCustomToken:success");

                        //boolean bool = true;
                    } else {
                        //Does something first and then destroys the app
                        mIsSuccessful = false;
                        Log.e(TAG, "hey: "+bigE);
                        Log.d(TAG, "firebase auth fail");
                        Log.w(TAG, "signInWithCustomToken:failure---", task.getException());

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
            ArrayList<Object> trace = new ArrayList<>();
            trace.add(result);
            trace.add(task.getException());

            return trace;
        }
    }
    public void assign(Throwable t){
        this.bigE = t;
    }
}
