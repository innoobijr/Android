package com.bitrider.survey.TrafficState;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class Query extends AsyncTask<Object, Object, Void>{

    private static final String TAG = "QueryAsyncTraffic";
    private AutoReportDao TaskDao;
    private LiveData<List<AutoReport>> reports;

    Query(AutoReportDao dao){
        TaskDao = dao;
    }

    @Override
    protected Void doInBackground(Object... params) {

        //Log.i(TAG, "Look " + TaskDao.getBusNames(params[0], params[1]));
        int i = (int) params[0];
        int j = (int) params[1];
        String k = (String) params[2];

        TaskDao.updateChecked(i, j, k);
        return null;

    }
}
