package com.bitrider.survey.TrafficState;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName="autoreport")
public class AutoReport {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    String id;


    private String from;

    private String to;

    @NonNull
    private int tag;

    private long time;

    private int checked;

    private int likes;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom( String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTag() {
        return tag;
    }

    public void setTag( int tag) {
        this.tag = tag;
    }

    public long getTime() {
        return time;
    }

    public void setTime( long time) {
        this.time = time;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }
}