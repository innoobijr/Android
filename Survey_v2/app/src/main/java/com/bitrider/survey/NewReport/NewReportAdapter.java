package com.bitrider.survey.NewReport;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.bitrider.survey.MapFragment;
import com.bitrider.survey.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class NewReportAdapter extends AbstractFragmentStepAdapter{

    public static final String CURRENT_STEP_POSITION_KEY = "position_key";

    public NewReportAdapter(FragmentManager fm, Context context){
        super(fm, context);
    }

    @Override
    public Step createStep(int position){
        final NewReportFragment step1 = new NewReportFragment();
        final NewReportSecStepFragment step2 = new NewReportSecStepFragment();
        //final MapFragment step2 = new MapFragment();

        if(position == 0) {
            Bundle b = new Bundle();
            b.putInt(CURRENT_STEP_POSITION_KEY, position);
            step1.setArguments(b);
            return step1;
        } else {
            Bundle b = new Bundle();
            b.putInt(CURRENT_STEP_POSITION_KEY, position);
            step2.setArguments(b);
            return step2;

        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from =0) int position){
        StepViewModel.Builder builder = new StepViewModel.Builder(context);

        switch (position){
            case 0:
                builder.setTitle("CHOOSE");
                break;
            case 1:
                builder.setTitle("DESCRIBE");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }


}
