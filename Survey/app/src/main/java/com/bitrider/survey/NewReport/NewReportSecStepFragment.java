package com.bitrider.survey.NewReport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitrider.survey.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class NewReportFragment extends Fragment implements Step {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.innogroup, container, false);

        return v;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected(){

    }

    @Override
    public void onError(@NonNull VerificationError error){

    }


}
