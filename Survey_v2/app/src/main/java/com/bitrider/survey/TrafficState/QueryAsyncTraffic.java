package com.bitrider.survey.TrafficState;

import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;

import com.bitrider.survey.GeoDB.AsyncResponse;
import com.bitrider.survey.GeoDB.GeoBean;
import com.bitrider.survey.GeoDB.PlacesDao;

import java.util.List;

public class QueryAsyncTraffic extends  AsyncTask<Object, Object, LiveData<List<AutoReport>>> {

    private static final String TAG = "QueryAsyncTraffic";
    public AsyncResponseTraffic delegate = null;
    private AutoReportDao TaskDao;
    private LiveData<List<AutoReport>> reports;

    ProgressBar progressBar;
    Context context;

    QueryAsyncTraffic(AutoReportDao dao, ProgressBar p, Context c){
        TaskDao = dao;
        progressBar= p;
        context = c;
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected LiveData<List<AutoReport>> doInBackground(Object... params) {
        //Log.i(TAG, "Look " + TaskDao.getBusNames(params[0], params[1]));

        return TaskDao.fetchAllData();

    }

    @Override
    public void onProgressUpdate(Object... ints){
        progressBar.setProgress(50);
    }

    @Override
    protected void onPostExecute(LiveData<List<AutoReport>> result) {
        //progressDialog.dismiss();
        delegate.processFinish(result);
    }

}