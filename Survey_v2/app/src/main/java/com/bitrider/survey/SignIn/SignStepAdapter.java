package com.bitrider.survey.SignIn;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.bitrider.survey.NewReport.NewReportFragment;
import com.bitrider.survey.NewReport.NewReportSecStepFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

public class SignStepAdapter extends AbstractFragmentStepAdapter{
    public static final String CURRENT_STEP_POSITION_KEY = "position_key";

    public SignStepAdapter(FragmentManager fm, Context context){
        super(fm, context);
    }

    @Override
    public Step createStep(int position){
        //TODO: Call SignInFragments
        switch (position){
            case 0:
               return SignStepNameFragment.newInstance();
            case 1:
                return SignStepStatusFragment.newInstance();
            case 2:
                return  SignStepPermissionsFragment.newInstance();
            default:
                return SignStepCompleteFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 4;
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
            case 2:
                builder.setTitle("DESCRIBE");
                break;
            case 3:
                builder.setTitle("DESCRIBE");
                break;
            default:
                throw new IllegalArgumentException("Unsupported position: " + position);
        }
        return builder.create();
    }


}


