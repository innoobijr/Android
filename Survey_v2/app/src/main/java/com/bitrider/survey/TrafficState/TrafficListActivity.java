package com.bitrider.survey.TrafficState;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.bitrider.survey.R;
import com.bitrider.survey.Recyclers.ReportListFragment;

public class TrafficListActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container2);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container2, fragment)
                    .commit();
        }

    }

    protected Fragment createFragment(){
        return new AutoReportFragment();
    }

}
