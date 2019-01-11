package com.bitrider.survey.NewReport;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class NewReportSecStepFragment extends Fragment implements Step {
    private MapView mapView;
    private Marker featureMarker;
    private Spinner spinner;
    private MapboxMap mapboxMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_offline_simple, container, false);
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        TextView txt = v.findViewById(R.id.description);
        spinner = (Spinner) v.findViewById(R.id.spinner);
        final List<String> alerts = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.alerts)));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, alerts){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.alerts,
        //        android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(getContext());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    txt.setVisibility(View.VISIBLE);
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    public OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(MapboxMap mapboxMap) {
            featureMarker = mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(6.429046, 3.424411))
                    .title("Lagos")
                    .snippet("Lagos"));

            mapboxMap.addOnMapClickListener(new MapboxMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng point) {
                    //Toast.makeText(getContext(), "Point: " + point.getLatitude(), Toast.LENGTH_SHORT).show();
                    if (featureMarker != null) {
                        mapboxMap.removeMarker(featureMarker);
                    }

                    final PointF pixel = mapboxMap.getProjection().toScreenLocation(point);
                    List<Feature> features = mapboxMap.queryRenderedFeatures(pixel);

                    if (features.size() > 0) {
                        Feature feature = features.get(0);

                        String property;

                        StringBuilder stringBuilder = new StringBuilder();
                        if (feature.properties() != null) {
                            for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                                stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                                stringBuilder.append(System.getProperty("line.separator"));
                            }

                            featureMarker = mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title("Hey")
                                    .snippet(stringBuilder.toString())
                            );

                        } else {
                            property = "Hey";
                            featureMarker = mapboxMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .snippet(property)
                            );
                        }
                    } else {
                        featureMarker = mapboxMap.addMarker(new MarkerOptions()
                                .position(point)
                                .snippet("Hey")
                        );
                    }
                    mapboxMap.selectMarker(featureMarker);
                }
            });
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(6.429046, 3.424411)) // Sets the new camera position
                    .zoom(17) // Sets the zoom
                    .bearing(180) // Rotate the camera
                    .tilt(30) // Set the camera tilt
                    .build(); // Creates a CameraPosition from the builder

            mapboxMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position), 7000);
        }
    };

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected(){
        Mapbox.getInstance(getContext(), "pk.eyJ1Ijoibm9kZXRyYWZmaWMiLCJhIjoiY2poMG9ma2Z1MHRsZTMzbzFqcGhtbGE3aSJ9.N4As8KJSgQQakQepEm2TDw");
        mapView.getMapAsync(mapReadyCallback);

    }

    @Override
    public void onError(@NonNull VerificationError error){

    }


}
