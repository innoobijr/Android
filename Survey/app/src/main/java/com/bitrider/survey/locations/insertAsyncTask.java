package com.example.transporttracker;

import android.os.AsyncTask;

import java.util.List;

import retrofit2.http.GET;

public class insertAsyncTask extends AsyncTask<Object, Void, Void> {
    private LocationDao mAsyncTaskDao;
    private PlacesDao TaskDao;

    insertAsyncTask(LocationDao dao){
        mAsyncTaskDao = dao;
    }
    insertAsyncTask(PlacesDao dao){
        TaskDao = dao;
    }


    @Override
    protected Void doInBackground(final Object... params){
        if (params[0].getClass().equals(Local.class)){
            Local param = (Local) params[0];
            mAsyncTaskDao.insertLocation(param);
        }

        if (params[0].getClass().equals(GeoBean.class)){
            GeoBean param = (GeoBean) params[0];
            TaskDao.insertPlace(param);
        }

        return null;
    }
}
