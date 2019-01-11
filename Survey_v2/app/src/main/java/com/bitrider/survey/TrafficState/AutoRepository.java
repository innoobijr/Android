package com.bitrider.survey.TrafficState;

import android.app.Application;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.widget.ProgressBar;

import com.bitrider.survey.BitriderRoomDatabase;
import com.bitrider.survey.GeoDB.GeoBean;
import com.bitrider.survey.GeoDB.QueryAsyncTask;
import com.bitrider.survey.listen.Report;
import com.bitrider.survey.listen.ReportDao;
import com.bitrider.survey.locations.deleteAsyncTask;
import com.bitrider.survey.locations.insertAsyncTask;

import java.util.List;

public class AutoRepository implements AsyncResponseTraffic{

    private AutoReportDao autoReportDao;
    private LiveData<List<AutoReport>> autoReports;
    private LiveData<List<AutoReport>> results;
    private List<AutoReport> reports;
    QueryAsyncTraffic asyncTask;

    public AutoRepository(Application application){
        BitriderRoomDatabase db = BitriderRoomDatabase.getDatabase(application);

        autoReportDao= db.autoReportDao();
        autoReports = autoReportDao.fetchAllData();


    }

    LiveData<List<AutoReport>> getReports() {return autoReports; };

    public LiveData<List<AutoReport>> query (ProgressBar p, Context context){
        asyncTask = new QueryAsyncTraffic(autoReportDao, p, context);
        asyncTask.delegate = this;

        try {
            return asyncTask.execute().get();

        } catch(Exception e){
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public void processFinish(LiveData<List<AutoReport>> output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        results = output;
    }

    public void insert (List<AutoReport> report){
        new insertAsyncTask(autoReportDao).execute(report);
    }

    public void update(int i, int j, String k){ new Query(autoReportDao).execute(i, j, k);
    }

    public void delete (){
        new deleteAsyncTask(autoReportDao).execute();
    }


}
