package com.bitrider.survey.TrafficState;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;

import com.bitrider.survey.BitriderRoomDatabase;
import com.bitrider.survey.GeoDB.GeoBean;
import com.bitrider.survey.listen.FirebaseQueryLiveData;
import com.bitrider.survey.listen.Report;
import com.bitrider.survey.listen.ReportDao;
import com.bitrider.survey.listen.ReportViewModel;
import com.bitrider.survey.listen.ReportsRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrafficReportViewModel extends AndroidViewModel{
    private static final String TAG = "sWitch";
    private AutoRepository rRepo;
    LiveData<List<AutoReport>> mAllResults;
    List<AutoReport> results;

    private static final DatabaseReference REPORTS =
            FirebaseDatabase.getInstance().getReference("trafficreports");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(REPORTS);

    public TrafficReportViewModel(Application application){
        super(application);
        rRepo = new AutoRepository(application);
        mAllResults = rRepo.getReports();
    }

    private final LiveData<List<AutoReport>> reportLiveData =
            Transformations.map(liveData, new TrafficReportViewModel.Deserializer());

    private class Deserializer implements Function<DataSnapshot, List<AutoReport>> {
        @Override
        public List<AutoReport> apply(DataSnapshot dataSnapshot) {
            List<AutoReport> ReportList = new ArrayList<>();
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                ReportList.add(postSnapshot.getValue(AutoReport.class));
            }
            //Log.i(TAG, "SOMETHING" + ReportList.get(0).getTitle());
            AutoReportDao dao = BitriderRoomDatabase.getDatabase(getApplication().getApplicationContext()).autoReportDao();
            ExecutorService doer = Executors.newSingleThreadExecutor();
            doer.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //TODO: Change this to check here and in the DAO
                        for (AutoReport report: ReportList){
                            dao.updateFields(report.getTag(), report.getLikes(), report.getId());
                        }

                        dao.insertMultipleReports(ReportList);
                        Log.i(TAG, "The results have been pushed!");

                        results = dao.fetchAllRegData();
                        Log.i(TAG, "The size is " + results.size());
                    } catch (Throwable t){
                        t.printStackTrace();
                        Log.e(TAG, "Firebase Error");
                    }

                }
            });

            return ReportList;


        }
    }

    //@NonNull
    public LiveData<List<AutoReport>> getReports(){ return mAllResults;}

    public void insert(List<AutoReport> report){ rRepo.insert(report);}

    public void update(int i, int j, String k){ rRepo.update(i, j, k);}


    public LiveData<List<AutoReport>> getReportLiveData() { return reportLiveData;
    }

    public LiveData<List<AutoReport>> query(ProgressBar p, Context context) {
        return rRepo.query(p, context);
    }
}

