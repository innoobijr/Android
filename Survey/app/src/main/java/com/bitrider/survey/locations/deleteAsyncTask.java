package com.example.transporttracker;


import android.os.AsyncTask;

public class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

    private LocationDao resAsyncTaskDao;

    deleteAsyncTask(LocationDao dao) {
        resAsyncTaskDao = dao;
    }

    @Override
    protected Void doInBackground(final Void... params) {
        resAsyncTaskDao.deleteAllButLast();
        return null;
    }
}
