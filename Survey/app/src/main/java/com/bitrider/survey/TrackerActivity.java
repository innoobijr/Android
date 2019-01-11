package com.example.transporttracker;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.hardware.SensorEventListener;

public class TrackerActivity extends AppCompatActivity implements SensorEventListener {

    //Sensors
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private FirebaseAuth mAuth;
    public FirebaseUser user;
    public FirebaseUser currentUser;
    private LocationViewModel locationViewModel;
    private static final String TAG = "TrackerActivity";
    private static final int PERMISSIONS_REQUEST = 1;
    public TextView text;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mAuth = FirebaseAuth.getInstance();
        loginToFirebase();
        text = (TextView) findViewById(R.id.information);
        text.setText("Hey bobby");

        //mAuth = FirebaseAuth.getInstance();
        //Check GPS Enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            //finish();
        }

        //Check location permission is granted = if is, start
        //the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService();
            run();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        Log.i(TAG, "The user the create");


    }

    @Override
    public void onPause() {
        super.onPause();
        run();
    }

    @Override
    public void onSensorChanged(SensorEvent event){

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    @Override
    public void onStart(){
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null) {
            uid = user.getUid();
        }

    }

    public void pushtoFirebase(List<Local> localList) {
        FirebaseUser User = mAuth.getCurrentUser();
        if (User != null) {
            uid = User.getUid();
            final String start = "users" + "/" + uid + "/" + getString(R.string.firebase_path);
            final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(start);
            final DatabaseReference fire = FirebaseDatabase.getInstance().getReference("geofire");
            GeoFire geoFire = new GeoFire(fire);

            geoFire.setLocation("hq", new GeoLocation(37.7853889, -122.4056973), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {}
            });


            Map<String, Object> push = new HashMap<String, Object>();

            for (int i = 0; i < localList.size() - 1; i++) {
                String z = "" + localList.get(i).getId();
                Log.i(TAG, "The id is: " + uid);
                push.put(z, localList.get(i));
            }
            ref.updateChildren(push);
            locationViewModel.delete();
        }
    }


    private void startTrackerService() {
        startService(new Intent(this, TrackerService.class));
        //finish();
    }

    private void loginToFirebase() {
        if (user == null) {
            String email = getString(R.string.firebase_email);
            String password = getString(R.string.firebase_password);
            mAuth.signInWithEmailAndPassword(
                    email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        uid = user.getUid();
                        Log.d(TAG, "firebase auth success");
                    } else {
                        Log.d(TAG, "firebase auth failed");
                    }

                }
            });
        }
    }

    public void run(){
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        locationViewModel.getmLocations().observe(this, new Observer<List<Local>>() {
            public void onChanged(@Nullable final List<Local> locals) {
                // Update the cached copy of the words in the adapter.
                text.setText("The size is: " + locals.size());
                Log.i(TAG, "The user id is: " + uid);
                if (locals.size() > 5) {
                    user = mAuth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(TrackerActivity.this, "Pushing to Firebase: " + locals.size(), Toast.LENGTH_SHORT).show();
                        pushtoFirebase(locals);
                    } else {
                        Toast.makeText(TrackerActivity.this, "Cant Push Yet", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Log.i(TAG, "The user the create2");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
                                           grantResults){
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Start the service when permission is granted
            startTrackerService();
            run();

        } else {
            //finish();
        }
    }
}
