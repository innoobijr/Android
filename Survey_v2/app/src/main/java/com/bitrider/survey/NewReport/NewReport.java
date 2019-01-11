package com.bitrider.survey.NewReport;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.BitriderRoomDatabase;
import com.bitrider.survey.GeoDB.GeoBean;
import com.bitrider.survey.GeoDB.PlaceViewModel;
import com.bitrider.survey.GeoDB.PlacesDao;
import com.bitrider.survey.MultiLineRadioGroup;
import com.bitrider.survey.QueryPreferences;
import com.bitrider.survey.R;
import com.bitrider.survey.listen.Report;
import com.bitrider.survey.listen.ReportViewModel;
import com.bitrider.survey.locations.Local;
import com.bitrider.survey.locations.LocationDao;
import com.bitrider.survey.locations.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.mapbox.turf.TurfClassification.nearestPoint;

public class NewReport extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private static final String TAG = "test";
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference ref;
    String it;
    String uid;
    private PlaceViewModel placeViewModel;
    private LocationViewModel locationViewModel;


    private static final String EXTRA_REPLY = "com.bitrider.survey.REPLY";

    private EditText mEditWordView;
    private EditText mEditTagView;
    private MultiLineRadioGroup multiLineRadioGroup;
    private static final int RESULT_NULL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        mEditTagView = findViewById(R.id.edit_tag);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mAuth = FirebaseAuth.getInstance();
        ViewGroup viewGroup;
        database = FirebaseDatabase.getInstance();
        TextView view19 = findViewById(R.id.textView19);
        int Tag = -1;

        if(mAuth.getCurrentUser() != null) {
            uid = mAuth.getCurrentUser().getUid();
            Report report = new Report();

            Log.i(TAG, "Trails of tears: " + QueryPreferences.getStopsDownloaded(this));
            placeViewModel = ViewModelProviders.of(NewReport.this).get(PlaceViewModel.class);

            placeViewModel.getmLocations().observe(this, new Observer<List<GeoBean>>() {
                @Override
                public void onChanged(@Nullable List<GeoBean> geoBeans) {

                    //mEditWordView.setText(Integer.toString(geoBeans.size()));
                }
            });

            multiLineRadioGroup = (MultiLineRadioGroup) findViewById(R.id.multi_line_radio_group);
            multiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ViewGroup group, RadioButton button) {
                    int i = button.getId();
                    while (i > 8) {
                        i = i - 8;
                    }
                    report.setTag(i);
                    view19.setText(button.getText());

                    Toast.makeText(getApplicationContext(), "The id is: " + i, Toast.LENGTH_SHORT).show();
                }
            });

            final Button button = findViewById(R.id.button_save);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent replyIntent = new Intent();
                    List<Local> locations = getterLocal();
                    Log.e(TAG, "Stored index: " + QueryPreferences.getStopsDownloaded(NewReport.this)
                    + " tag: " + report.getTag() + ": location size: " + locations.size());
                    if (report.getTag() < 1 | QueryPreferences.getStopsDownloaded(NewReport.this) == false | locations.size() == 0) {
                        //TODO: if getStoredIndex fails try to download
                        setResult(RESULT_CANCELED, replyIntent);
                    } else {
                        ref = database.getReference("/reports");

                        List<Point> points = new ArrayList<>();

                        GeoBean bb = new GeoBean();
                        Local ll = new Local();

                        List<GeoBean> beans = getterBean();

                        for (GeoBean bean : beans) {
                            Point location = Point.fromLngLat(bean.getLng(), bean.getLat());
                            points.add(location);
                        }

                        //Need better control
                        //TO-DO:
                        Local to = locations.get(0);
                        Local from = locations.get(locations.size() - 1);

                        //Point to_Point = Point.fromLngLat(to.getLongitude(), to.getLatitude());
                        //Point from_Point = Point.fromLngLat(from.getLongitude(), from.getLatitude());

                        Point to_Point = Point.fromLngLat(3.384300, 6.457234);
                        Point from_Point = Point.fromLngLat(3.349494, 6.479393);

                        GeoBean bean1 = getGeoBean(executeNearestPoint(to_Point, points));
                        GeoBean bean2 = getGeoBean(executeNearestPoint(from_Point, points));

                        report.setTo(bean1.getTitle());
                        report.setFrom(bean2.getTitle());
                        report.setLat(to_Point.latitude());
                        report.setLng(to_Point.longitude());
                        pushtoFirebase(report);
                        replyIntent.putExtra(EXTRA_REPLY, "works");
                        setResult(RESULT_OK, replyIntent);
                    }
                    finish();
                }
            });

            ReportViewModel reportViewModel = ViewModelProviders.of(this).get(ReportViewModel.class);
            LiveData<List<Report>> reportLiveData = reportViewModel.getReportLiveData();
            reportLiveData.observe(this, new Observer<List<Report>>() {
                @Override
                public void onChanged(@Nullable List<Report> reports) {
                    if (reports.size() != 0) {
                        Log.i(TAG, "new test" + reports.get(0).getTitle());
                        //mEditWordView.setText(reports.get(0).getTitle());
                    }
                }
            });
        } else {
            Intent replyIntent = new Intent();
            setResult(RESULT_NULL, replyIntent);
            finish();
            //sigin-in
        }
    }

    public Point executeNearestPoint(Point point1, List<Point> points){
        ExecutorService doer = Executors.newSingleThreadExecutor();
        Future<Point> pointFuture = doer.submit(new Callable<Point>() {
            @Override
            public Point call() throws Exception {
                nearestPoint(point1, points);
                return nearestPoint(point1, points);
            }
        });
        Point result = null;
        try{
            result = pointFuture.get();
            Log.i(TAG, "result finished");
        } catch (InterruptedException | ExecutionException e ) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public GeoBean getGeoBean(Point point){
        ExecutorService doer = Executors.newSingleThreadExecutor();
        //Log.i("getGeoBean", "Frankly"+ point.latitude());
        Future<GeoBean> future = doer.submit(new Callable<GeoBean>() {
            @Override
            public GeoBean call() throws Exception {
                GeoBean bean = null;
                PlacesDao dao = BitriderRoomDatabase.getDatabase(NewReport.this).placesDao();
                List<GeoBean> result = dao.getPlaceNames(point.longitude(), point.latitude());
                bean = result.get(0);
                return bean;
            }
        });
        GeoBean result = null;
        try{
            result = future.get();
        } catch (InterruptedException | ExecutionException e ) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<GeoBean> getterBean(){
        ExecutorService doer = Executors.newSingleThreadExecutor();
        //Log.i("getGeoBean", "Frankly"+ point.latitude());
        Future<List<GeoBean>> future = doer.submit(new Callable<List<GeoBean>>() {
                @Override
                public List<GeoBean> call() throws Exception {
                    PlacesDao dao = BitriderRoomDatabase.getDatabase(NewReport.this).placesDao();
                    List<GeoBean> beans = dao.fetchAll();
                    return beans;
                }
            });
        List<GeoBean> result = null;
        try{
            result = future.get();
        } catch (InterruptedException | ExecutionException e ) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public List<Local> getterLocal(){
        ExecutorService doer = Executors.newSingleThreadExecutor();
        //Log.i("getGeoBean", "Frankly"+ point.latitude());
        Future<List<Local>> future = doer.submit(new Callable<List<Local>>() {
            @Override
            public List<Local> call() throws Exception {
                LocationDao dao = BitriderRoomDatabase.getDatabase(NewReport.this).locationDao();
                List<Local> locals = dao.getLastFiveRows();
                return locals;
            }
        });
        List<Local> locals = null;
        try{
            locals = future.get();
        } catch (InterruptedException | ExecutionException e ) {
            throw new RuntimeException(e);
        }
        return locals;
    }

    public void pushtoFirebase(Report report) {
        if (uid != null) {
            Log.e(TAG, "uid is fine");
            Map<String, Object> push = new HashMap<String, Object>();
            int time = (int) System.currentTimeMillis();
            String id = time + uid;
            report.setTime(time);
            report.setId(id);
            push.put(id, report);
            ref.updateChildren(push);
        } else {
            Log.e(TAG, "uid is null");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
