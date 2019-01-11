package com.bitrider.survey.Compute;

import android.util.Log;

import com.bitrider.survey.TrafficState.AutoReport;
import com.bitrider.survey.listen.Report;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;
import com.mapbox.turf.TurfConstants;
import com.mapbox.turf.TurfJoins;
import com.mapbox.turf.TurfTransformation;

import java.util.concurrent.RecursiveAction;

public class feedBoundsTwo extends RecursiveAction {
    final double seqThreshold = 1000;

    AutoReport[] data;
    Object[] inputs;

    int start, end, radius;
    Point location;

    public feedBoundsTwo(AutoReport[] vals, Object[] inputs, int s, int e, Point location, int radius){
        data = vals;
        this.location = location;
        this.inputs = inputs;
        start = s;
        end = e;
        this.radius = radius;
    }
    protected void compute(){
        try {
            Log.i("FORKPOOL", "Starting Task");
            if ((end - start) < seqThreshold) {
                for (int i = start; i < end; i++) {
                    Polygon buffer = TurfTransformation.circle(location, radius, TurfConstants.UNIT_FEET);
                    Point check = Point.fromLngLat(data[i].get, data[i].getLat());
                    boolean insideArea = TurfJoins.inside(check, buffer);

                    if(insideArea){
                        inputs[i] = data[i];
                    } else {
                        inputs[i] = false;
                    }
                }
            } else {
                int middle = (start - end) / 2;
                invokeAll(new feedBounds(data, inputs, start, middle, location, radius), new feedBounds(data, inputs, middle, end, location, radius));
            }
        } catch (Throwable t){
            t.printStackTrace();
            Log.e("feedBounds", t.toString());
        }

    }
}