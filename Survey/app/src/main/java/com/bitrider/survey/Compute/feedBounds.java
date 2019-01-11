package com.bitrider.survey.Compute;

import android.util.Log;

import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfMeasurement;

import java.util.concurrent.RecursiveAction;

public class feedBounds extends RecursiveAction {
    final double seqThreshold = 1000;

    Point[] data;
    double[] distance;

    int start, end;
    Point location;

    public feedBounds(Point[] vals, double[] dist, int s, int e, Point location){
        data = vals;
        this.location = location;
        distance = dist;
        start = s;
        end = e;
    }
    protected void compute(){
        try {
            Log.i("FORKPOOL", "Starting Task");
            if ((end - start) < seqThreshold) {
                for (int i = start; i < end; i++) {

                    distance[i] = TurfMeasurement.distance(location, data[i], TurfConstants.UNIT_FEET);
                }
            } else {
                int middle = (start - end) / 2;
                invokeAll(new feedBounds(data, distance, start, middle, location), new feedBounds(data, distance, middle, end, location));
            }
        } catch (Throwable t){
            t.printStackTrace();
            Log.e("TrackerService", t.toString());
        }

    }
}
