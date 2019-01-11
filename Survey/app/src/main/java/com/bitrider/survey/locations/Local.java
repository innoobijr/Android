package com.example.transporttracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "location")
public class Local {

    @PrimaryKey(autoGenerate = true)
    private long id;

    Local(){}

    Local(double lo, double lt){
        this.latitude = lt;
        this.longitude = lo;
    }

    Local(double lo, double lt, float speed){
        this.latitude = lt;
        this.longitude = lo;
        this.speed = speed;
    }

    @NonNull
    private double longitude;
    private double latitude;
    private float speed;

    @NonNull
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NonNull double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
