package com.bitrider.survey.TrafficState;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.bitrider.survey.listen.Report;

import java.util.List;

@Dao
public interface AutoReportDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertReport(AutoReport... report);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultipleReports(List<AutoReport> reports);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOnlySingleResponse(AutoReport report);

    @Query("SELECT * FROM autoreport")
    LiveData<List<AutoReport>> fetchAllData();

    @Query("SELECT * FROM autoreport ORDER BY id DESC LIMIT 7")
    List<AutoReport> fetchAllRegData();

    @Query("DELETE FROM autoreport WHERE id NOT IN (SELECT MAX(id) id FROM report)")
    void deleleAllButLast();

    @Query("UPDATE autoreport SET checked = :checked, tag = :likes WHERE id = :key")
    void updateChecked(int checked, int likes, String key);

    @Query("UPDATE autoreport SET tag = :tag, likes = :likes WHERE id = :key")
    void updateFields(int tag, int likes, String key);

    @Query("DELETE FROM autoreport")
    void deleteAll();

    @Query("SELECT * FROM autoreport ORDER BY id DESC LIMIT 1")
    AutoReport getLastRow();

    @Update
    void updateReport(AutoReport report);
}

