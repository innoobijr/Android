package com.bitrider.survey.TrafficState;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bitrider.survey.R;
import com.bitrider.survey.Survey;
import com.bitrider.survey.SurveyViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class ReportActivityTwo extends Fragment {

    private static final String TAG = "ReportActivity2";
    private DatabaseReference ref;
    TrafficReportViewModel reportViewModel;
    private View v;
    private RecyclerView mAutoRecyclerView;
    AutoReportListAdapter adapter;

    String key;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_survey_list2, container, false);
        //Recycler();

        TextView textView = (TextView) v.findViewById(R.id.textView18);
        RadioGroup buttonGroup = (RadioGroup) v.findViewById(R.id.trafficButtonGroup);
        RadioButton buttonTwo = (RadioButton) v.findViewById(R.id.radioButton2);
        buttonTwo.setId((int) 1);
        RadioButton buttonThree = (RadioButton) v.findViewById(R.id.radioButton3);
        buttonThree.setId((int) 2);

        TrafficReportViewModel reportViewModelOne = ViewModelProviders.of(this).get(TrafficReportViewModel.class);
        LiveData<List<AutoReport>> reportLiveDataOne = reportViewModelOne.getReportLiveData();
        reportLiveDataOne.observe(this, new Observer<List<AutoReport>>() {
            @Override
            public void onChanged(@Nullable List<AutoReport> autoReports) {

            }
        });
        return null;
    }

        /**reportViewModel = ViewModelProviders.of(this).get(TrafficReportViewModel.class);
        LiveData<List<AutoReport>> reportLiveData = reportViewModel.query();
        reportLiveData.observe(ReportActivityTwo.this, new Observer<List<AutoReport>>() {
            @Override
            public void onChanged(@Nullable List<AutoReport> reports) {
                if (reports.size() != 0) {
                    if(reports.get(0).getChecked() == 1){
                        buttonThree.setChecked(true);
                    }
                    if(reports.get(0).getChecked() == 2){
                        buttonTwo.setChecked(true);
                    }

                    key = reports.get(0).getId();
                    Log.i(TAG, "AutoReports " + reports.get(0).getId() + reports.get(0).getChecked());
                    String text = reports.get(0).getFrom().toUpperCase() + " > " + reports.get(0).getTo().toUpperCase();
                    textView.setText(text);

                }
            }
        });


        buttonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ref = FirebaseDatabase.getInstance().getReference("trafficreports/" + key);
                Toast.makeText(getContext(), "The id is: " + checkedId,  Toast.LENGTH_SHORT).show();
                onRadioChange(ref, checkedId, v);

            }
        });

        return v;
    }

    private void Recycler() {
        mAutoRecyclerView = (RecyclerView) v
                .findViewById(R.id.survey_recycler_view);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        mAutoRecyclerView.setItemAnimator(animator);
        //mSurveyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //updateUI();
        mAutoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAutoRecyclerView.setAdapter(adapter);


        reportViewModel = ViewModelProviders.of(this).get(TrafficReportViewModel.class);

        try {
            reportViewModel.getReports().observe(this, new Observer<List<AutoReport>>() {
                @Override
                public void onChanged(@Nullable final List<AutoReport> reports) {
                    // Update the cached copy of the words in the adapter.
                    adapter.setAutoReports(reports);
                    //adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {

        }
    }

    private void onRadioChange(DatabaseReference ref, int checkId, View v){
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                AutoReport p = mutableData.getValue(AutoReport.class);
                int i, j;
                String k;

                if(p == null){
                    return Transaction.success(mutableData);
                }

                if(checkId == 2) {
                    int new_tag = p.getTag()+1;
                    p.setTag(new_tag);
                    i = 1;
                    j = new_tag;
                    k = key;
                    reportViewModel.update(i, j, k);

                } else{
                    if(p.getTag() != 0){
                        int new_tag = p.getTag() - 1;
                        p.setTag(new_tag);
                        i = 2;
                        j = new_tag;
                        k = key;
                        reportViewModel.update(i, j, k);
                    }
                }

                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                //Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);

            }
        });

    }**/
}

